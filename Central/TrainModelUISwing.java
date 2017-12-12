import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class TrainModelUISwing {
    public static final String DEFAULT_COLOR = "#FEFEFE";
    public static final String ON_COLOR = "#E8E70C";
    public static final String OFF_COLOR = "#E8E8E8";
    public static final String ERROR_COLOR = "#E80018";
    public static final String NORMAL_COLOR = "#00E828";

    JLabel commandSpeedText;
    JLabel currentPowerText;
    JLabel slopeText;
    JLabel currentSpeedText;
    JLabel totalWeightText;
    JComboBox<String> trainSelector;
    JButton emergencyStop;
    private JPanel panelMain;
    JPanel leftDoorIndicator;
    JPanel rightDoorIndicator;
    JPanel lightIndicator;
    JLabel nextStopText;
    JLabel carFailureText;
    JLabel outdoorTempText;
    JLabel indoorTempText;
    JLabel passengerText;
    private JLabel authorityText;
    private JCheckBox powerErrorCheckBox;
    private JCheckBox carErrorCheckBox;
    private JCheckBox brakeErrorCheckBox;
    private TrainModel currentModel;

    DecimalFormat df = new DecimalFormat("0.00");

    // Index keeped consistent with index in trainSelector
    ArrayList<TrainModel> trains;

    JPanel getPanelMain() {
        return panelMain;
    }

    public TrainModelUISwing() {
        System.out.println("Attempting to start the train model");
        trainSelector.setPrototypeDisplayValue("Train 000");
        trainSelector.addItemListener(e -> {
            int index = trainSelector.getSelectedIndex();
            if (index >= trains.size() || index < 0) {
                return;
            }
            // switch to new train
            currentModel = trains.get(index);
            // update gui after switching to new train
            update();
        });

        emergencyStop.addActionListener(e -> {
            if (currentModel == null) {
                return;
            }
            currentModel.emegencyCommand();
        });

        powerErrorCheckBox.addActionListener(e -> {
            if (currentModel == null) {
                return;
            }
            currentModel.setHasPowerError(!currentModel.hasPowerError);
        });
        brakeErrorCheckBox.addActionListener(e -> {
            if (currentModel == null) {
                return;
            }
            currentModel.setHasBrakeError(!currentModel.hasBrakeError);
        });
        carErrorCheckBox.addActionListener(e -> {
            if (currentModel == null) {
                return;
            }
            currentModel.setHasCarError(!currentModel.hasCarError, "Error");
        });
    }

    void linkToTrainPool(TrainPool trainPool) {
        this.trains = trainPool.trains;
        update();
    }

    public void addTrainToSelector(Integer id) {
        trainSelector.addItem("Train " + id.toString());
    }

    public void removeTrainFromSelector(Integer index) {
        if (trains.get(index) == currentModel) {
            currentModel = null;
        }
        trainSelector.removeItemAt(index);
    }

    public void update() {
        if (this.currentModel == null) {
            this.currentSpeedText.setText("Not Available");
            this.commandSpeedText.setText("Not Available");
            this.currentPowerText.setText("Not Available");
            this.slopeText.setText("Not Available");
            this.carFailureText.setText("Not Available");
            this.authorityText.setText("Not Available");
            this.passengerText.setText("Not Available");
            this.nextStopText.setText("Not Available");
            this.totalWeightText.setText("Not Available");
            return;
        }
        this.currentSpeedText.setText(df.format(this.currentModel.displayCurrentSpeed) + " Mph");
        if (this.currentModel.controller == null) {
            this.commandSpeedText.setText("Not Available");
        } else {
            this.commandSpeedText.setText(df.format(this.currentModel.controller.displayCommandSpeed) + " Mph");

        }
        this.currentPowerText.setText(df.format(this.currentModel.currentPower) + " kW");
        this.slopeText.setText(df.format(this.currentModel.displaySlope));
        this.carFailureText.setText(this.currentModel.carErrorString);
        this.authorityText.setText(df.format(this.currentModel.displayAuthority) + " Mi");
        this.passengerText.setText(this.currentModel.passengerNum.toString());
        this.nextStopText.setText(this.currentModel.nextStation);
        this.totalWeightText.setText(df.format(this.currentModel.totalWeight) + " Tons");
        this.leftDoorIndicator.setBackground(Color.decode(this.currentModel.leftDoorOpen ? ON_COLOR : OFF_COLOR));
        this.rightDoorIndicator.setBackground(Color.decode(this.currentModel.rightDoorOpen ? ON_COLOR : OFF_COLOR));
        this.lightIndicator.setBackground(Color.decode(this.currentModel.lightOn ? ON_COLOR : OFF_COLOR));
        this.emergencyStop.setForeground(Color.decode(this.currentModel.emergencyBrakeCommand ? ON_COLOR : DEFAULT_COLOR));

        this.powerErrorCheckBox.setSelected(currentModel.hasPowerError);
        this.brakeErrorCheckBox.setSelected(currentModel.hasBrakeError);
        this.carErrorCheckBox.setSelected(currentModel.hasCarError);
    }

    public static TrainModelUISwing createTrainModelGUI() {
        TrainModelUISwing ui = new TrainModelUISwing();
        JFrame frame = new JFrame("Train Model");
        frame.setContentPane(ui.getPanelMain());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        return ui;
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
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(6, 3, new Insets(3, 3, 3, 3), -1, -1));
        panelMain.add(panel1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        final JLabel label1 = new JLabel();
        label1.setText("Current Power:");
        panel1.add(label1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        commandSpeedText = new JLabel();
        commandSpeedText.setText("Label");
        panel1.add(commandSpeedText, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentPowerText = new JLabel();
        currentPowerText.setText("Label");
        panel1.add(currentPowerText, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Grade:");
        panel1.add(label2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        slopeText = new JLabel();
        slopeText.setText("Label");
        panel1.add(slopeText, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Current Speed:");
        panel1.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Command Speed:");
        panel1.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentSpeedText = new JLabel();
        currentSpeedText.setText("Label");
        panel1.add(currentSpeedText, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 1, 6, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Authority:");
        panel1.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        totalWeightText = new JLabel();
        totalWeightText.setText("Label");
        panel1.add(totalWeightText, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Total Weight:");
        panel1.add(label6, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        authorityText = new JLabel();
        authorityText.setText("Label");
        panel1.add(authorityText, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panelMain.add(spacer2, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(14, 21), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        trainSelector = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        trainSelector.setModel(defaultComboBoxModel1);
        panel2.add(trainSelector, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(5, 3, new Insets(3, 3, 3, 3), -1, -1));
        panelMain.add(panel3, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        final JLabel label7 = new JLabel();
        label7.setText("Indoor Temp.");
        panel3.add(label7, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        outdoorTempText = new JLabel();
        outdoorTempText.setText("Label");
        panel3.add(outdoorTempText, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        indoorTempText = new JLabel();
        indoorTempText.setText("Label");
        panel3.add(indoorTempText, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Passenger:");
        panel3.add(label8, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passengerText = new JLabel();
        passengerText.setText("Label");
        panel3.add(passengerText, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Car Failure:");
        panel3.add(label9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Outdoor Temp.:");
        panel3.add(label10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        carFailureText = new JLabel();
        carFailureText.setText("Label");
        panel3.add(carFailureText, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Next Stop:");
        panel3.add(label11, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nextStopText = new JLabel();
        nextStopText.setText("Label");
        panel3.add(nextStopText, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(0, 1, 5, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setHorizontalAlignment(0);
        label12.setHorizontalTextPosition(0);
        label12.setText("Train Status");
        panelMain.add(label12, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        emergencyStop = new JButton();
        emergencyStop.setBackground(new Color(-5762800));
        emergencyStop.setForeground(new Color(-65794));
        emergencyStop.setText("EMERGENCY STOP");
        panelMain.add(emergencyStop, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel4, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Error"));
        powerErrorCheckBox = new JCheckBox();
        powerErrorCheckBox.setText("Power Error");
        panel4.add(powerErrorCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel4.add(spacer4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        carErrorCheckBox = new JCheckBox();
        carErrorCheckBox.setText("Car Error");
        panel4.add(carErrorCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        brakeErrorCheckBox = new JCheckBox();
        brakeErrorCheckBox.setText("Brake Error");
        panel4.add(brakeErrorCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(4, 1, new Insets(3, 3, 3, 3), -1, -1));
        panelMain.add(panel5, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        leftDoorIndicator = new JPanel();
        leftDoorIndicator.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        leftDoorIndicator.setBackground(new Color(-1513240));
        panel5.add(leftDoorIndicator, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Left");
        leftDoorIndicator.add(label13, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel5.add(spacer5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        rightDoorIndicator = new JPanel();
        rightDoorIndicator.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        rightDoorIndicator.setBackground(new Color(-1513240));
        panel5.add(rightDoorIndicator, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("Right");
        rightDoorIndicator.add(label14, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lightIndicator = new JPanel();
        lightIndicator.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        lightIndicator.setBackground(new Color(-1513240));
        panel5.add(lightIndicator, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        label15.setText("Light");
        lightIndicator.add(label15, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }
}
