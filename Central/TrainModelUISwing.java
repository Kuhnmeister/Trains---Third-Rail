import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TrainModelUISwing {
    public static final String DEFAULT_COLOR = "#FEFEFE";
    public static final String ON_COLOR = "#E8E70C";
    public static final String OFF_COLOR = "#E8E8E8";
    public static final String ERROR_COLOR = "#E80018";
    public static final String NORMAL_COLOR = "#00E828";

    JLabel commandSpeedText= new JLabel();
    JLabel currentPowerText= new JLabel();
    JLabel slopeText= new JLabel();
    JLabel currentSpeedText = new JLabel();
    JLabel totalWeightText= new JLabel();
    JComboBox<String> trainSelector= new JComboBox<String>();
    JButton emergencyStop = new JButton();
    private JPanel panelMain;
    JPanel leftDoorIndicator;
    JPanel rightDoorIndicator;
    JPanel lightIndicator;
    JLabel nextStopText= new JLabel();
    JLabel carFailureText= new JLabel();
    JLabel outdoorTempText= new JLabel();
    JLabel indoorTempText= new JLabel();
    JLabel passengerText= new JLabel();
    private JLabel authorityText= new JLabel();
    private JCheckBox powerErrorCheckBox = new JCheckBox();
    private JCheckBox carErrorCheckBox = new JCheckBox();
    private JCheckBox brakeErrorCheckBox = new JCheckBox();
    private TrainModel currentModel;
    // Index keeped consistent with index in trainSelector
    ArrayList<TrainModel> trains;

    JPanel getPanelMain() {
        return panelMain;
    }

    public TrainModelUISwing()
    {
        System.out.println(trainSelector.toString());
        trainSelector.addItemListener(e -> {
            int index = trainSelector.getSelectedIndex();
            if(index >= trains.size() || index < 0)
            {
                return;
            }
            // switch to new train
            currentModel = trains.get(index);
            // update gui after switching to new train
            update();
        });

        emergencyStop.addActionListener(e -> {
            if(currentModel == null)
            {
                return;
            }
            currentModel.emegencyCommand();
        });

        powerErrorCheckBox.addActionListener(e -> {
            if(currentModel == null)
            {
                return;
            }
            currentModel.setHasPowerError(!currentModel.hasPowerError);
        });
        brakeErrorCheckBox.addActionListener(e -> {
            if(currentModel == null)
            {
                return;
            }
            currentModel.setHasBrakeError(!currentModel.hasBrakeError);
        });
        carErrorCheckBox.addActionListener(e -> {
            if(currentModel == null)
            {
                return;
            }
            currentModel.setHasCarError(!currentModel.hasCarError, "Error");
        });
    }

    void linkToTrainPool(TrainPool trainPool)
    {
        this.trains = trainPool.trains;
        update();
    }

    public void addTrainToSelector(Integer id)
    {
        trainSelector.addItem("Train "+id.toString());
    }

    public void removeTrainFromSelector(Integer index)
    {
        if(trains.get(index) == currentModel)
        {
            currentModel = null;
        }
        trainSelector.removeItemAt(index);
    }

    public void update()
    {
        if(this.currentModel == null)
        {
            this.currentSpeedText.setText("N/A");
            this.commandSpeedText.setText("N/A");
            this.currentPowerText.setText("N/A");
            this.slopeText.setText("N/A");
            this.carFailureText.setText("N/A");
            this.authorityText.setText("N/A");
            this.passengerText.setText("N/A");
            this.nextStopText.setText("N/A");
            this.totalWeightText.setText("N/A");
            return;
        }
        this.currentSpeedText.setText(this.currentModel.displayCurrentSpeed.toString()+" Mph");
        if(this.currentModel.controller == null)
        {
            this.commandSpeedText.setText("N/A");
        } else {
            this.commandSpeedText.setText(this.currentModel.controller.displayCommandSpeed.toString());

        }
        this.currentPowerText.setText(this.currentModel.currentPower.toString());
        this.slopeText.setText(this.currentModel.displaySlope.toString());
        this.carFailureText.setText(this.currentModel.carErrorString);
        this.authorityText.setText(this.currentModel.displayAuthority.toString());
        this.passengerText.setText(this.currentModel.passengerNum.toString());
        this.nextStopText.setText(this.currentModel.nextStation);
        this.totalWeightText.setText(this.currentModel.totalWeight.toString());
        this.leftDoorIndicator.setBackground(Color.decode(this.currentModel.leftDoorOpen?ON_COLOR:OFF_COLOR));
        this.rightDoorIndicator.setBackground(Color.decode(this.currentModel.rightDoorOpen?ON_COLOR:OFF_COLOR));
        this.lightIndicator.setBackground(Color.decode(this.currentModel.lightOn?ON_COLOR:OFF_COLOR));
        this.emergencyStop.setForeground(Color.decode(this.currentModel.emergencyBrakeCommand?ON_COLOR:DEFAULT_COLOR));

        this.powerErrorCheckBox.setSelected(currentModel.hasPowerError);
        this.brakeErrorCheckBox.setSelected(currentModel.hasBrakeError);
        this.carErrorCheckBox.setSelected(currentModel.hasCarError);
    }

    public static TrainModelUISwing createTrainModelGUI()
    {
        TrainModelUISwing ui = new TrainModelUISwing();
        JFrame frame = new JFrame("Train Model");
        frame.setContentPane(ui.getPanelMain());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        return ui;
    }
}
