import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

/*
    Created by yih on 09/16/2017
*/


public class TrainControllerUISwing {
    private JPanel PanelMain;
    JComboBox<String> trainSelector;
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

    DecimalFormat df = new DecimalFormat("0.00");

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

    public TrainControllerUISwing() {

        // Set eventListeners for buttons, etc
        trainSelector.addItemListener(e -> {
            int index = trainSelector.getSelectedIndex();
            if (index >= trains.size() || index < 0) {
                return;
            }
            // switch to new train
            currentModel = trains.get(index);
            currentController = trains.get(index).controller;
            // update gui after switching to new train
            update();
        });
        autoMode.addActionListener(e -> {
            if (currentController == null) {
                return;
            }
            currentController.isAutoMode = true;
            currentController.update();
            currentController.authorityError = false;
            currentController.update();
            //updateUI() will be called in controller
        });
        manualMode.addActionListener(e -> {
            if (currentController == null) {
                return;
            }
            if (currentController.autoModeOverride) {
                currentController.authorityError = true;
            } else {
                currentController.isAutoMode = false;
                currentController.authorityError = false;
            }
            currentController.update();
            //updateUI() will be called in controller
        });

        leftDoorButton.addActionListener(e -> {
            if (currentController == null) {
                return;
            }
            currentController.setLeftDoorCommand(true);
            //updateUI() will be called in controller
        });
        rightDoorButton.addActionListener(e -> {
            if (currentController == null) {
                return;
            }
            currentController.setRightDoorComand(true);
            //updateUI() will be called in controller
        });
        lightButton.addActionListener(e -> {
            if (currentController == null) {
                return;
            }
            currentController.setLightCommand(true);
            //updateUI() will be called in controller
        });
        //Set target speed
        speedOK.addActionListener(e -> {
            if (currentController == null) {
                return;
            }
            currentController.setInputSpeed(Double.parseDouble(speedInput.getText()));
            //updateUI() will be called in controller
        });

        //Emergecy Stop
        emergencyStopButton.addActionListener(e -> {
            if (currentController == null) {
                return;
            }
            currentController.emergencyCommand();
            //updateUI() will be called in controller
        });
        serviceBrakeButton.addActionListener(e -> {
            if (currentController == null) {
                return;
            }
            currentController.serviceBrakeCommand = !currentController.serviceBrakeCommand;
            currentController.update();
        });

    }

    void linkToTrainPool(TrainPool trainPool) {
        this.trains = trainPool.trains;
        update();
    }

    void addTrainToSelector(Integer id) {
        trainSelector.addItem("Train " + id.toString());
    }

    void removeTrainFromSelector(Integer index) {
        if (trains.get(index) == currentModel) {
            currentModel = null;
        }
        trainSelector.removeItemAt(index);
        update();
    }

    public void update() {
        if (this.currentModel == null) {
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
        totalWeightText.setText(df.format(this.currentModel.totalWeight) + " Tons");
        carStatusText.setText(this.currentModel.carErrorString);
        brakeStatusText.setText(this.currentModel.hasBrakeError ? "Error" : "OK");
        powerStatusText.setText(this.currentModel.hasPowerError ? "Error" : "OK");
        maxPowerText.setText(df.format(this.currentModel.maxPower) + " kW");
        inYardText.setText(this.currentModel.inYard ? "Yes" : "No");
        authorityText.setText(df.format(this.currentModel.displayAuthority));

        //Current Status
        currentSpeedText.setText(df.format(this.currentModel.displayCurrentSpeed) + " Mph");
        currentAccelText.setText(df.format(this.currentModel.currentAccel) + " m/s2");
        currentPowerText.setText(df.format(this.currentModel.currentPower) + " kW");
        if (currentModel.emergencyStopActive) {
            currentBrakeText.setText("Emergency");
        } else if (currentModel.serviceBrakeActive) {
            currentBrakeText.setText("Service");
        } else {
            currentBrakeText.setText("None");
        }
        passengerNumText.setText(this.currentModel.passengerNum.toString());
        nextStationText.setText(this.currentModel.nextStation);
        //nextStation

        //Authority
        //speedLimitText.setText(this.currentController.displaySpeedLimit.toString()+" Mph");
        targetSpeedText.setText(df.format(this.currentController.displayTargetSpeed) + " Mph");
        commandSpeedText.setText(df.format(this.currentController.displayCommandSpeed) + " Mph");
        authorityText.setText(df.format(this.currentModel.displayAuthority) + " Mi");
        if (currentController.authorityEmergencyStop) {
            emergencyStopText.setText("Detected!");
        } else {
            emergencyStopText.setText("Not Detected");
        }

        // Doors and Light
        if (this.currentModel.leftDoorOpen) {
            leftDoorButton.setText("Close Left Door");
            leftDoorIndicator.setBackground(Color.decode(ON_COLOR));
        } else {
            leftDoorButton.setText("Open Left Door");
            leftDoorIndicator.setBackground(Color.decode(OFF_COLOR));
        }
        if (this.currentModel.rightDoorOpen) {
            rightDoorButton.setText("Close Left Door");
            rightDoorIndicator.setBackground(Color.decode(ON_COLOR));
        } else {
            rightDoorButton.setText("Open Left Door");
            rightDoorIndicator.setBackground(Color.decode(OFF_COLOR));
        }
        if (this.currentModel.lightOn) {
            lightButton.setText("Light Off");
            lightIndicator.setBackground(Color.decode(ON_COLOR));
        } else {
            lightButton.setText("Light On");
            lightIndicator.setBackground(Color.decode(OFF_COLOR));
        }
        this.systemErrorIndicator.setBackground(Color.decode(this.currentController.systemError ? ERROR_COLOR : NORMAL_COLOR));
        this.powerLimitIndicator.setBackground(Color.decode(this.currentModel.powerLimitReached ? ERROR_COLOR : NORMAL_COLOR));
        this.speedLimitIndicator.setBackground(Color.decode(this.currentController.exceedSpeedLimit ? ERROR_COLOR : NORMAL_COLOR));
        this.authorityErrorIndicator.setBackground(Color.decode(this.currentController.authorityError ? ERROR_COLOR : NORMAL_COLOR));
        emergencyStopButton.setForeground(Color.decode(this.currentController.emergencyBrakeCommand ? ON_COLOR : DEFAULT_COLOR));
        serviceBrakeButton.setForeground(Color.decode(this.currentController.serviceBrakeCommand ? ON_COLOR : DEFAULT_COLOR));

        if (this.currentController == null) {
            return;
        }
        if (this.currentController.isAutoMode) {
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

    public static TrainControllerUISwing createTrainControllerGUI() {
        TrainControllerUISwing ui = new TrainControllerUISwing();
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

    /*public TrainControllerUISwing(TrainWithController parent) {
        this.parent = parent;
        TrainControllerUISwing self = this;

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


        TrainControllerUISwing trainControl = new TrainControllerUISwing();
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


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        PanelMain = new JPanel();
        PanelMain.setLayout(new GridLayoutManager(6, 8, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        PanelMain.add(spacer1, new GridConstraints(0, 3, 6, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        PanelMain.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        trainSelector = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        trainSelector.setModel(defaultComboBoxModel1);
        panel1.add(trainSelector, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        autoMode = new JRadioButton();
        autoMode.setSelected(false);
        autoMode.setText("Auto");
        PanelMain.add(autoMode, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(7, 3, new Insets(3, 3, 3, 3), -1, -1));
        PanelMain.add(panel2, new GridConstraints(2, 0, 4, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(1, 1, 6, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        label1.setText("Train Status");
        panel2.add(label1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Brake Status:");
        panel2.add(label2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        carStatusText = new JLabel();
        carStatusText.setText("Label");
        panel2.add(carStatusText, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        brakeStatusText = new JLabel();
        brakeStatusText.setText("Label");
        panel2.add(brakeStatusText, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Power Status:");
        panel2.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Total Weight:");
        panel2.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Car Status:");
        panel2.add(label5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        totalWeightText = new JLabel();
        totalWeightText.setText("Label");
        panel2.add(totalWeightText, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        maxPowerText = new JLabel();
        maxPowerText.setText("Label");
        panel2.add(maxPowerText, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        powerStatusText = new JLabel();
        powerStatusText.setText("Label");
        panel2.add(powerStatusText, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Max Power:");
        panel2.add(label6, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("In Yard:");
        panel2.add(label7, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        inYardText = new JLabel();
        inYardText.setText("Label");
        panel2.add(inYardText, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        PanelMain.add(spacer3, new GridConstraints(0, 7, 6, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(6, 4, new Insets(10, 10, 10, 10), -1, -1));
        PanelMain.add(panel3, new GridConstraints(2, 4, 4, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        speedInput = new JTextField();
        panel3.add(speedInput, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        speedOK = new JButton();
        speedOK.setText("OK");
        panel3.add(speedOK, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Speed Input");
        panel3.add(label8, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        emergencyStopButton = new JButton();
        emergencyStopButton.setBackground(new Color(-5762800));
        emergencyStopButton.setForeground(new Color(-65794));
        emergencyStopButton.setText("EMERGENCY STOP ");
        panel3.add(emergencyStopButton, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        leftDoorButton = new JButton();
        leftDoorButton.setText("Open Left Door");
        panel3.add(leftDoorButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rightDoorButton = new JButton();
        rightDoorButton.setText(" Open Right Door");
        panel3.add(rightDoorButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        speedLimitIndicator = new JPanel();
        speedLimitIndicator.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        speedLimitIndicator.setBackground(new Color(-16717784));
        panel3.add(speedLimitIndicator, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Speed Limit");
        speedLimitIndicator.add(label9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        leftDoorIndicator = new JPanel();
        leftDoorIndicator.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        leftDoorIndicator.setBackground(new Color(-1513240));
        panel3.add(leftDoorIndicator, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Left");
        leftDoorIndicator.add(label10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rightDoorIndicator = new JPanel();
        rightDoorIndicator.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        rightDoorIndicator.setBackground(new Color(-1513240));
        panel3.add(rightDoorIndicator, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Right");
        rightDoorIndicator.add(label11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lightButton = new JButton();
        lightButton.setText("Light On");
        panel3.add(lightButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lightIndicator = new JPanel();
        lightIndicator.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        lightIndicator.setBackground(new Color(-1513240));
        panel3.add(lightIndicator, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Light");
        lightIndicator.add(label12, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        authorityErrorIndicator = new JPanel();
        authorityErrorIndicator.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        authorityErrorIndicator.setBackground(new Color(-16717784));
        panel3.add(authorityErrorIndicator, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Authority");
        authorityErrorIndicator.add(label13, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        systemErrorIndicator = new JPanel();
        systemErrorIndicator.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        systemErrorIndicator.setBackground(new Color(-16717784));
        panel3.add(systemErrorIndicator, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        safeText = new JLabel();
        safeText.setText("System Error");
        systemErrorIndicator.add(safeText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        powerLimitIndicator = new JPanel();
        powerLimitIndicator.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        powerLimitIndicator.setBackground(new Color(-16717784));
        panel3.add(powerLimitIndicator, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("Power Limit");
        powerLimitIndicator.add(label14, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        serviceBrakeButton = new JButton();
        serviceBrakeButton.setBackground(new Color(-13214232));
        serviceBrakeButton.setForeground(new Color(-65794));
        serviceBrakeButton.setText("Service Brake");
        panel3.add(serviceBrakeButton, new GridConstraints(5, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        manualMode = new JRadioButton();
        manualMode.setSelected(false);
        manualMode.setText("Manual");
        PanelMain.add(manualMode, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(7, 3, new Insets(3, 3, 3, 3), -1, -1));
        PanelMain.add(panel4, new GridConstraints(2, 1, 4, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        final JLabel label15 = new JLabel();
        label15.setHorizontalAlignment(0);
        label15.setHorizontalTextPosition(0);
        label15.setText("Current Status");
        panel4.add(label15, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label16 = new JLabel();
        label16.setText("Current Power:");
        panel4.add(label16, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentAccelText = new JLabel();
        currentAccelText.setText("Label");
        panel4.add(currentAccelText, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentPowerText = new JLabel();
        currentPowerText.setText("Label");
        panel4.add(currentPowerText, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label17 = new JLabel();
        label17.setText("Current Brake:");
        panel4.add(label17, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentBrakeText = new JLabel();
        currentBrakeText.setText("Label");
        panel4.add(currentBrakeText, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label18 = new JLabel();
        label18.setText("Current Speed:");
        panel4.add(label18, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label19 = new JLabel();
        label19.setText("Current Accel.:");
        panel4.add(label19, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentSpeedText = new JLabel();
        currentSpeedText.setText("Label");
        panel4.add(currentSpeedText, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label20 = new JLabel();
        label20.setText("Next Station:");
        panel4.add(label20, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nextStationText = new JLabel();
        nextStationText.setText("Label");
        panel4.add(nextStationText, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel4.add(spacer4, new GridConstraints(1, 1, 6, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label21 = new JLabel();
        label21.setText("Passengers Num:");
        panel4.add(label21, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passengerNumText = new JLabel();
        passengerNumText.setText("Label");
        panel4.add(passengerNumText, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(5, 3, new Insets(3, 3, 3, 3), -1, -1));
        PanelMain.add(panel5, new GridConstraints(2, 2, 4, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        final Spacer spacer5 = new Spacer();
        panel5.add(spacer5, new GridConstraints(1, 1, 4, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label22 = new JLabel();
        label22.setHorizontalAlignment(0);
        label22.setHorizontalTextPosition(0);
        label22.setText("Authority");
        panel5.add(label22, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label23 = new JLabel();
        label23.setText("Target Speed:");
        panel5.add(label23, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        targetSpeedText = new JLabel();
        targetSpeedText.setText("Label");
        panel5.add(targetSpeedText, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label24 = new JLabel();
        label24.setText("Command Speed:");
        panel5.add(label24, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        commandSpeedText = new JLabel();
        commandSpeedText.setText("Label");
        panel5.add(commandSpeedText, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label25 = new JLabel();
        label25.setText("Authority:");
        panel5.add(label25, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        authorityText = new JLabel();
        authorityText.setText("Label");
        panel5.add(authorityText, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label26 = new JLabel();
        label26.setText("Emergency Stop:");
        panel5.add(label26, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        emergencyStopText = new JLabel();
        emergencyStopText.setText("Label");
        panel5.add(emergencyStopText, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        PanelMain.add(spacer6, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        emergencyStopAll = new JButton();
        emergencyStopAll.setBackground(new Color(-5762800));
        emergencyStopAll.setForeground(new Color(-65794));
        emergencyStopAll.setText("EMERGENCY STOP ALL");
        PanelMain.add(emergencyStopAll, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        PIDInputButton = new JButton();
        PIDInputButton.setText("PID Set");
        PanelMain.add(PIDInputButton, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(autoMode);
        buttonGroup.add(manualMode);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return PanelMain;
    }
}
