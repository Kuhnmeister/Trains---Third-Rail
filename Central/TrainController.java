import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/*
    Created by yih on 09/16/2017
*/


public class TrainController {
    private JPanel PanelMain;
    private JComboBox trainSelector;
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
    private JButton serviceBrakeButton;
    private JLabel totalWeightText;
    private JLabel carStatusText;
    private JLabel brakeStatusText;
    private JLabel powerStatusText;
    private JLabel maxPowerText;
    private JLabel currentSpeedText;
    private JLabel currentAccelText;
    private JLabel currentPowerText;
    private JLabel currentBrakeRateText;
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
    private JFrame PIDSetter;
    TrainStatus currentTrain;
    private ArrayList<TrainControllerOutputReceiver> outputReceivers;
    private TrainWithController parent;

    //private TrainControllerOutputReceiver outputReceiver;
    static final int DEFAULT = 50;
    static final String defaultColor = "#FEFEFE";
    public static final String onIndicatorColor = "#E8E70C";
    public static final String indicatorOffColor = "#E8E8E8";
    static final String errorColor = "#E80018";
    static final String normalColor = "#00E828";

    public JPanel getPanelMain() {
        return PanelMain;
    }

    public static ArrayList<TrainStatus> Trains;

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
        currentBrakeRateText.setText(ts.getCurrentBrakeRate().toString()+" Lbf");
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
    }

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

    public TrainController(TrainWithController parent) {
        this.parent = parent;
        TrainController self = this;

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

    }

    public static void main(String[] args) {
        System.out.println("Run the main!");
        /*Trains = new ArrayList<TrainStatus>();
        Trains.add(new TrainStatus("Train 1"));
        Trains.add(new TrainStatus("Train 2"));
        Trains.add(new TrainStatus("Train 3"));


        TrainController trainControl = new TrainController();
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
