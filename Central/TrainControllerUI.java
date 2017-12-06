package train;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/*
    Created by yih on 09/16/2017
*/


public class TrainControllerUI {
    private JPanel PanelMain;
    JComboBox trainSelector;
    private JRadioButton autoMode;
    private JRadioButton manualMode;
    private JTextField speedInput;
    private JButton speedOK;
    private JPanel systemErrorIndicator;
    private JLabel safeText;
    private JButton emergencyStopButton;
    private JButton leftDoorButton;
    private JButton rightDoorButton;
    private JPanel leftDoorIndicator;
    private JPanel rightDoorIndicator;
    private JLabel totalWeightText;
    private JLabel carStatusText;
    private JLabel brakeStatusText;
    private JLabel powerStatusText;
    private JLabel maxPowerText;
    private JLabel currentSpeedText;
    private JLabel currentAccelText;
    private JLabel currentPowerText;
    private JLabel currentBrakeText;
    private JLabel nextStationText;
    private JPanel powerLimitIndicator;
    private JPanel speedLimitIndicator;
    private JPanel authorityErrorIndicator;
    private JPanel lightIndicator;
    private JLabel commandSpeedText;
    private JLabel speedLimitText;
    private JLabel authorityText;
    private JLabel emergencyStopText;
    private JButton lightButton;
    private JButton emergencyStopAll;
    private JLabel inYardText;
    private JButton PIDInputButton;
    private JLabel targetSpeedText;
    private JButton serviceBrakeButton;
    private JLabel passengerNumText;
    private JFrame PIDSetter;
    /*TrainStatus currentTrain;
    private ArrayList<TrainControllerOutputReceiver> outputReceivers;
    private TrainWithController parent;*/

    TrainModel currentModel;
    TrainController currentController;
    // Index keeped consistent with index in trainSelector
    ArrayList<TrainModel> trains;

    //private TrainControllerOutputReceiver outputReceiver;
    static final int DEFAULT = 50;
    static final String DEFAULT_COLOR = "#FEFEFE";
    public static final String ON_COLOR = "#E8E70C";
    public static final String OFF_COLOR = "#E8E8E8";
    static final String ERROR_COLOR = "#E80018";
    static final String NORMAL_COLOR = "#00E828";

    public JPanel getPanelMain() {
        return PanelMain;
    }

    public TrainControllerUI()
    {

        // Set eventListeners for buttons, etc
        trainSelector.addItemListener(e -> {
            int index = trainSelector.getSelectedIndex();
            if(index >= trains.size()|| index <0)
            {
                return;
            }
            // switch to new train
            currentModel = trains.get(index);
            currentController = trains.get(index).controller;
            // update gui after switching to new train
            update();
        });
        autoMode.addActionListener(e -> {
            currentController.isAutoMode = true;
            currentController.update();
            currentController.authorityError = false;
            currentController.update();
            //updateUI() will be called in controller
        });
        manualMode.addActionListener(e -> {
            if(currentController.autoModeOverride)
            {
                currentController.authorityError = true;
            }else{
                currentController.isAutoMode = false;
                currentController.authorityError = false;
            }
            currentController.update();
            //updateUI() will be called in controller
        });

        leftDoorButton.addActionListener(e -> {
            currentController.setLeftDoorCommand(true);
            //updateUI() will be called in controller
        });
        rightDoorButton.addActionListener(e -> {
            currentController.setRightDoorComand(true);
            //updateUI() will be called in controller
        });
        lightButton.addActionListener(e -> {
            currentController.setLightCommand(true);
            //updateUI() will be called in controller
        });
        //Set target speed
        speedOK.addActionListener(e -> {
            currentController.setInputSpeed(Double.parseDouble(speedInput.getText()));
            //updateUI() will be called in controller
        });

        //Emergecy Stop
        emergencyStopButton.addActionListener(e -> {
            currentController.emergencyCommand();
            //updateUI() will be called in controller
        });
        serviceBrakeButton.addActionListener(e -> {
            currentController.serviceBrakeCommand = !currentController.serviceBrakeCommand;
            currentController.update();
        });

    }

    void linkToTrainPool(TrainPool trainPool)
    {
        this.trains = trainPool.trains;
        update();
    }

    void addTrainToSelector(Integer id)
    {
        trainSelector.addItem("Train "+id.toString());
    }

    void removeTrainFromSelector(Integer index)
    {
        if(trains.get(index) == currentModel)
        {
            currentModel = null;
        }
        trainSelector.removeItemAt(index);
        update();
    }

    public void update()
    {
        if(this.currentModel == null)
        {
            currentController = null;
            totalWeightText.setText("N/A");
            carStatusText.setText("N/A");
            brakeStatusText.setText("N/A");
            powerStatusText.setText("N/A");
            maxPowerText.setText("N/A");
            inYardText.setText("N/A");
            authorityText.setText("N/A");

            currentSpeedText.setText("N/A");
            currentPowerText.setText("N/A");
            currentBrakeText.setText("N/A");
            passengerNumText.setText("N/A");
            nextStationText.setText("N/A");

            //speedLimitText.setText("N/A");
            targetSpeedText.setText("N/A");
            commandSpeedText.setText("N/A");
            authorityText.setText("N/A");
            emergencyStopText.setText("N/A");

            leftDoorButton.setText("N/A");
            rightDoorButton.setText("N/A");
            lightButton.setText("N/A");

            autoMode.setEnabled(false);
            manualMode.setEnabled(false);
            speedInput.setEnabled(false);
            speedOK.setEnabled(false);
            leftDoorButton.setEnabled(false);
            rightDoorButton.setEnabled(false);
            lightButton.setEnabled(false);
            return;
        }

        autoMode.setEnabled(true);
        manualMode.setEnabled(true);
        // Train data
        totalWeightText.setText(this.currentModel.totalWeight.toString());
        carStatusText.setText(this.currentModel.carErrorString);
        brakeStatusText.setText(this.currentModel.hasBrakeError?"Error":"OK");
        powerStatusText.setText(this.currentModel.hasPowerError?"Error":"OK");
        maxPowerText.setText(this.currentModel.maxPower.toString()+" kW");
        inYardText.setText(this.currentModel.inYard?"Yes":"No");
        authorityText.setText(this.currentModel.displayAuthority.toString());

        //Current Status
        currentSpeedText.setText(this.currentModel.displayCurrentSpeed.toString()+" Mph");
        currentAccelText.setText(this.currentModel.currentAccel.toString()+" m/s2");
        currentPowerText.setText(this.currentModel.currentPower.toString()+" kW");
        if(currentModel.emergencyStopActive) {
            currentBrakeText.setText("Emergency");
        }else if(currentModel.serviceBrakeActive) {
            currentBrakeText.setText("Service");
        }else{
            currentBrakeText.setText("None");
        }
        passengerNumText.setText(this.currentModel.passengerNum.toString());
        //nextStation

        //Authority
        //speedLimitText.setText(this.currentController.displaySpeedLimit.toString()+" Mph");
        targetSpeedText.setText(this.currentController.displayTargetSpeed.toString()+" Mph");
        commandSpeedText.setText(this.currentController.displayCommandSpeed.toString()+" Mph");
        authorityText.setText(this.currentModel.displayAuthority.toString()+" Mi");
        if(currentController.authorityEmergencyStop)
        {
            emergencyStopText.setText("Detected!");
        } else {
            emergencyStopText.setText("Not Detected");
        }

        // Doors and Light
        if(this.currentModel.leftDoorOpen) {
            leftDoorButton.setText("Close Left Door");
            leftDoorIndicator.setBackground(Color.decode(ON_COLOR));
        } else {
            leftDoorButton.setText("Open Left Door");
            leftDoorIndicator.setBackground(Color.decode(OFF_COLOR));
        }
        if(this.currentModel.rightDoorOpen) {
            rightDoorButton.setText("Close Left Door");
            rightDoorIndicator.setBackground(Color.decode(ON_COLOR));
        } else {
            rightDoorButton.setText("Open Left Door");
            rightDoorIndicator.setBackground(Color.decode(OFF_COLOR));
        }
        if(this.currentModel.lightOn) {
            lightButton.setText("Light Off");
            lightIndicator.setBackground(Color.decode(ON_COLOR));
        } else {
            lightButton.setText("Light On");
            lightIndicator.setBackground(Color.decode(OFF_COLOR));
        }
        this.systemErrorIndicator.setBackground(Color.decode(this.currentController.systemError?ERROR_COLOR:NORMAL_COLOR));
        this.powerLimitIndicator.setBackground(Color.decode(this.currentModel.powerLimitReached?ERROR_COLOR:NORMAL_COLOR));
        this.speedLimitIndicator.setBackground(Color.decode(this.currentController.exceedSpeedLimit?ERROR_COLOR:NORMAL_COLOR));
        this.authorityErrorIndicator.setBackground(Color.decode(this.currentController.authorityError?ERROR_COLOR:NORMAL_COLOR));
        emergencyStopButton.setForeground(Color.decode(this.currentController.emergencyBrakeCommand ?ON_COLOR:DEFAULT_COLOR));
        serviceBrakeButton.setForeground(Color.decode(this.currentController.serviceBrakeCommand ?ON_COLOR:DEFAULT_COLOR));

        if(this.currentController == null)
        {
            return;
        }
        if(this.currentController.isAutoMode)
        {
            autoMode.setSelected(true);
            speedInput.setEnabled(false);
            speedOK.setEnabled(false);
            leftDoorButton.setEnabled(false);
            rightDoorButton.setEnabled(false);
            lightButton.setEnabled(false);
        } else {
            manualMode.setSelected(true);
            speedInput.setEnabled(true);
            speedOK.setEnabled(true);
            leftDoorButton.setEnabled(true);
            rightDoorButton.setEnabled(true);
            lightButton.setEnabled(true);
        }
    }

    public static TrainControllerUI createTrainControllerGUI()
    {
        TrainControllerUI ui = new TrainControllerUI();
        JFrame frame = new JFrame("Train Controller");
        frame.setContentPane(ui.getPanelMain());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        return ui;
    }


    /*public static ArrayList<TrainStatus> Trains;

    public void addOutputReceiver(TrainControllerOutputReceiver outputReceiver) {
        this.outputReceivers.add(outputReceiver);
    }

    public void refresh(){

        Trains.forEach(TrainStatus::update);

        TrainStatus ts = currentTrain;
        if(ts.getAutoMode())
        {
            autoMode.setSelected(true);
            speedInput.setEnabled(false);
            speedOK.setEnabled(false);
            leftDoorButton.setEnabled(false);
            rightDoorButton.setEnabled(false);
            lightButton.setEnabled(false);
            serviceBrakeButton.setEnabled(false);
        } else {
            manualMode.setSelected(true);
            speedInput.setEnabled(true);
            speedOK.setEnabled(true);
            leftDoorButton.setEnabled(true);
            rightDoorButton.setEnabled(true);
            lightButton.setEnabled(true);
            serviceBrakeButton.setEnabled(true);
        }
        if(ts.getLeftDoorOpen()) {
            leftDoorButton.setText("Close Left Door");
            leftDoorIndicator.setBackground(Color.decode(onIndicatorColor));
        } else {
            leftDoorButton.setText("Open Left Door");
            leftDoorIndicator.setBackground(Color.decode(indicatorOffColor));
        }
        if(ts.getRightDoorOpen()) {
            rightDoorButton.setText("Close Right Door");
            rightDoorIndicator.setBackground(Color.decode(onIndicatorColor));
        } else {
            rightDoorButton.setText("Open Right Door");
            rightDoorIndicator.setBackground(Color.decode(indicatorOffColor));
        }
        if(ts.getLightOn()) {
            lightButton.setText("Light Off");
            lightIndicator.setBackground(Color.decode(onIndicatorColor));
        } else {
            lightButton.setText("Light On");
            lightIndicator.setBackground(Color.decode(indicatorOffColor));
        }
        totalWeightText.setText(ts.getTotalWeight().toString()+" Tons");
        carStatusText.setText(ts.getCarStatus());
        brakeStatusText.setText(ts.getBrakeStatus());
        powerStatusText.setText(ts.getPowerStatus());
        maxPowerText.setText(ts.getMaxPower().toString()+" Watt");
        inYardText.setText(ts.getInYard()?"Yes":"No");
        currentSpeedText.setText(ts.getCurrentSpeed().toString()+" Mph");
        currentAccelText.setText(ts.getCurrentAccel().toString()+" m/s2");
        currentPowerText.setText(ts.getCurrentPower().toString()+" Watt");
        currentBrakeText.setText(ts.getCurrentBrakeRate().toString()+" Lbf");
        nextStationText.setText(ts.getNextStation());
        speedLimitText.setText(ts.getSpeedLimit().toString()+" Mph");
        commandSpeedText.setText(ts.getCommandSpeed().toString()+" Mph");
        authorityText.setText(ts.getAuthority().toString()+" Ml");
        if(ts.getInYard())
        {
            PIDInputButton.setEnabled(true);
        }else{
            PIDInputButton.setEnabled(false);
        }
        if(ts.getAuthorityEmergencyStop())
        {
            emergencyStopText.setText("Detected!");
        } else {
            emergencyStopText.setText("Not Detected");
        }
        if(ts.getSystemError()) {
            systemErrorIndicator.setBackground(Color.decode(errorColor));
        } else {
            systemErrorIndicator.setBackground(Color.decode(normalColor));
        }
        if(ts.getPowerLimitReached()) {
            powerLimitIndicator.setBackground(Color.decode(errorColor));
        } else {
            powerLimitIndicator.setBackground(Color.decode(normalColor));
        }
        if(ts.getSpeedLimitReached()) {
            speedLimitIndicator.setBackground(Color.decode(errorColor));
        } else {
            speedLimitIndicator.setBackground(Color.decode(normalColor));
        }
        if(ts.getAuthorityError()) {
            authorityErrorIndicator.setBackground(Color.decode(errorColor));
        } else {
            authorityErrorIndicator.setBackground(Color.decode(normalColor));
        }
        if(ts.getEmergencyStop()) {
            emergencyStopButton.setForeground(Color.decode(onIndicatorColor));
        } else {
            emergencyStopButton.setForeground(Color.decode(defaultColor));
        }
        if(ts.getServiceBrake()) {
            serviceBrakeButton.setForeground(Color.decode(onIndicatorColor));
        } else {
            serviceBrakeButton.setForeground(Color.decode(defaultColor));
        }
        //if(outputReceiver != null)
        //{
        outputReceivers.forEach(TrainControllerOutputReceiver::update);
        //    outputReceiver.update();
        //}
    }

    void Calc()
    {
        Trains.forEach(t -> t.Calc());
        //outputReceivers.forEach(TrainControllerOutputReceiver::update);
        refresh();
    }*/

    /*void reset() {
        Trains.clear();
        Trains.add(new TrainStatus("Train 1"));
        Trains.add(new TrainStatus("Train 2"));
        Trains.add(new TrainStatus("Train 3"));
        currentTrain = Trains.get(0);
        trainSelector.setSelectedIndex(0);
        refresh();
    }*/

    void closePIDSetter() {
        PIDSetter.dispose();
    }

    /*public TrainControllerUI(TrainWithController parent) {
        this.parent = parent;
        TrainControllerUI self = this;

        for (TrainStatus Train : Trains) {
            trainSelector.addItem(Train.getName());
        }

        outputReceivers = new ArrayList<>();

        currentTrain = Trains.get(0);
        refresh();
        trainSelector.addItemListener(e -> {
            int index = trainSelector.getSelectedIndex();
            currentTrain = Trains.get(index);
            refresh();
        });
        autoMode.addActionListener(e -> {
            currentTrain.setAutoMode(true);
            refresh();
        });
        manualMode.addActionListener(e -> {
            if(currentTrain.getAuthorityEmergencyStop())
            {
                currentTrain.setAuthorityError(true);
            } else if(!currentTrain.getHasError()) {
                currentTrain.setAutoMode(false);
            }
            refresh();
        });
        leftDoorButton.addActionListener(e -> {
            currentTrain.setLeftDoorCommand(true);
            refresh();
        });
        rightDoorButton.addActionListener(e -> {
            currentTrain.setRightDoorCommand(true);
            refresh();
        });
        lightButton.addActionListener(e -> {
            currentTrain.setLightCommand(true);
            refresh();
        });
        emergencyStopButton.addActionListener(e -> {
            currentTrain.setEmergencyCommand(true);
            refresh();
        });
        serviceBrakeButton.addActionListener(e -> {
            currentTrain.setServiceBrake(!currentTrain.getServiceBrake());
            parent.triggerServiceBrake(currentTrain.id, currentTrain.getServiceBrake());
            refresh();
        });
        speedOK.addActionListener(e -> {
            currentTrain.setInputSpeed(Double.parseDouble(speedInput.getText()));
            refresh();
        });
        emergencyStopAll.addActionListener(e -> {
            for (TrainStatus Train : Trains) {
                Train.setEmergencyCommand(true);
            }
            refresh();
        });
        PIDInputButton.addActionListener(e -> {
            JFrame pidsetter = new JFrame("Parameter Setting");
            PIDSetter setter = new PIDSetter(self);
            setter.OKButton.addActionListener(e1 -> {
                currentTrain.Kp = Double.parseDouble(setter.KpInput.getText());
                currentTrain.Ki = Double.parseDouble(setter.KiInput.getText());
                currentTrain.resetPID();
                pidsetter.dispose();
            });
            pidsetter.setContentPane(setter.PanelMain);
            pidsetter.pack();
            pidsetter.setVisible(true);
        });

    }*/

    public static void main(String[] args) {
        System.out.println("Run the main!");
        /*Trains = new ArrayList<TrainStatus>();
        Trains.add(new TrainStatus("Train 1"));
        Trains.add(new TrainStatus("Train 2"));
        Trains.add(new TrainStatus("Train 3"));


        TrainControllerUI trainControl = new TrainControllerUI();
        JFrame frame = new JFrame("Train Controller");
        frame.setContentPane(trainControl.PanelMain);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);*/

        /*TrainControllerTestbench testbench = new TrainControllerTestbench(trainControl);
        trainControl.addOutputReceiver(testbench);
        JFrame frame2 = new JFrame("Testbench");
        frame2.setContentPane(testbench.PanelMain);
        frame2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame2.pack();
        frame2.setVisible(true);*/

    }


}
