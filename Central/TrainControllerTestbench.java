package train;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;


public class TrainControllerTestbench{

    private JPanel leftDoorIndicator;
    private JComboBox trainSelector;
    public JPanel PanelMain;
    private JTextField totalWeightInput;
    private JButton totalWeightSet;
    private JButton carErrorSet;
    private JRadioButton brakeOKRadioButton;
    private JRadioButton brakeErrorRadioButton;
    private JTextField carErrorInput;
    private JRadioButton powerOKRadioButton;
    private JRadioButton powerErrorRadioButton;
    private JTextField maxPowerInput;
    private JButton maxPowerSet;
    private JTextField currentSpeedInput;
    private JTextField commandSpeedInput;
    private JTextField AuthorityInput;
    private JTextField nextStationInput;
    private JRadioButton authorityEmergencyStopFalseRadioButton;
    private JRadioButton authorityEmergencyStopTrueRadioButton;
    private JButton currentSpeedSet;
    private JButton commandSpeedSet;
    private JButton AuthoritySet;
    private JButton nextStationSet;
    private JPanel systemErrorIndicator;
    private JLabel safeText;
    private JPanel rightDoorIndicator;
    private JPanel manualModeIndicator;
    private JPanel lightIndicator;
    private JPanel powerLimitIndicator;
    private JPanel speedLimitIndicator;
    private JPanel emergencyStopIndicator;
    private JPanel serviceBrakeIndicator;
    private JTextField powerText;
    private JTextField speedLimitInput;
    private JButton speedLimitSet;
    private JButton leftDoorButton;
    private JButton rightDoorButton;
    private JButton lightButton;
    private JButton resetButton;
    private JRadioButton inYardFalseRatiodButton;
    private JRadioButton inYardTrueRatiodButton;
    private JButton calcButton;
    private JButton createTrainButton;
    private JButton deleteTrainButton;

    private ArrayList<TrainModel> trains;
    private TrainPool trainPool;
    private TrainModel currentTrain;

    Integer idCnt = 0;
    Integer currentTrainId;
    static final int DEFAULT = 50;
    static final String defaultColor = "#FEFEFE";
    public static final String onIndicatorColor = "#E8E70C";
    public static final String indicatorOffColor = "#E8E8E8";
    static final String errorColor = "#E80018";
    static final String normalColor = "#00E828";

    public TrainControllerTestbench(TrainPool trainPool)
    {
        this.trainPool = trainPool;
        this.trains = trainPool.trains;
        currentTrainId = -1;

        createUI();

        /*Trains.forEach(t -> trainSelector.addItem(t.getName()));*/
        update();
        trainSelector.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int index = trainSelector.getSelectedIndex();
                if(index >= trains.size() || index==-1)
                {
                    currentTrain = null;
                    currentTrainId = -1;
                    return;
                }
                currentTrain = trains.get(index);
                currentTrainId = currentTrain.id;
                update();
            }
        });

        createTrainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.createNewTrain(idCnt);
                trainSelector.addItem("Train "+idCnt.toString());
                idCnt++;
            }
        });

        deleteTrainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.removeTrain(currentTrainId);
                trainSelector.removeItemAt(trainSelector.getSelectedIndex());
            }
        });

        inYardTrueRatiodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.setInYard(currentTrainId, true);
            }
        });
        inYardFalseRatiodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.setInYard(currentTrainId, false);
            }
        });
        carErrorSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.setCarError(currentTrainId, carErrorInput.getText());
            }
        });
        brakeOKRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.setBrakeError(currentTrainId, false);
                //currentTrain.setHasBrakeError(false);
                //trainCtl.refresh();
            }
        });
        brakeErrorRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.setBrakeError(currentTrainId, true);
                //currentTrain.setHasBrakeError(true);
                //trainCtl.refresh();
            }
        });
        powerOKRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.setPowerError(currentTrainId, false);
                //currentTrain.setHasPowerError(false);
                //trainCtl.refresh();
            }
        });
        powerErrorRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.setPowerError(currentTrainId, true);
                //currentTrain.setHasPowerError(true);
                //trainCtl.refresh();
            }
        });

        totalWeightSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.setTotalWeight(currentTrainId, Double.parseDouble(totalWeightInput.getText()));
                //currentTrain.setTotalWeight(Double.parseDouble(totalWeightInput.getText()));
                //trainCtl.refresh();
            }
        });
        maxPowerSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.setMaxPower(currentTrainId, Double.parseDouble(maxPowerInput.getText()));
                //currentTrain.setMaxPower(Integer.parseInt(maxPowerInput.getText()));
                //trainCtl.refresh();
            }
        });
        speedLimitSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.setSpeedLimit(currentTrainId, Double.parseDouble(speedLimitInput.getText()));
            }
        });
        currentSpeedSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.currentSpeed = Double.parseDouble(currentSpeedInput.getText())*TrainController.MPH2MS;
                currentTrain.controller.update();
            }
        });
        commandSpeedSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.setCommandSpeed(currentTrainId, Double.parseDouble(commandSpeedInput.getText()));
            }
        });
        AuthoritySet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.setAuthority(currentTrainId, Double.parseDouble(AuthorityInput.getText()));
            }
        });
        nextStationSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              //  currentTrain.setNextStation(nextStationInput.getText());
              //  trainCtl.refresh();
            }
        });

        leftDoorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.openLeftDoor(currentTrainId);
                //trainCtl.refresh();
            }
        });
        rightDoorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.openRightDoor(currentTrainId);
                //trainCtl.refresh();
            }
        });
        lightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.openLights(currentTrainId);
                //trainCtl.refresh();
            }
        });
        authorityEmergencyStopTrueRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.emegencyStop(currentTrainId, true);
                //trainCtl.refresh();
            }
        });
        authorityEmergencyStopFalseRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainPool.emegencyStop(currentTrainId, false);
                //currentTrain.setAuthorityEmergencyStop(false);
                //currentTrain.setAuthorityError(false);
                //trainCtl.refresh();
            }
        });
        /*resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //trainControl.reset();
                currentTrain = Trains.get(0);
                trainSelector.setSelectedIndex(0);
                System.out.println("AS:"+currentTrain.getAuthorityEmergencyStop().toString());
                update();
            }
        });*/
        calcButton.addActionListener(e -> {
            trainPool.Step();
        });
    }

    public void removeTrainFromSelector(Integer index)
    {
        if(trains.get(index) == currentTrain)
        {
            currentTrain = null;
        }
        trainSelector.removeItemAt(index);
        update();
    }

    void createUI()
    {
        JFrame frame = new JFrame("Testbench");
        frame.setContentPane(this.PanelMain);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void update() {
        if(currentTrain == null)
        {
            return;
        }
        if(currentTrain.inYard)
        {
            inYardTrueRatiodButton.setSelected(true);
        }else{
            inYardFalseRatiodButton.setSelected(true);
        }
        if(!currentTrain.hasBrakeError)
        {
            brakeOKRadioButton.setSelected(true);
        }else{
            brakeErrorRadioButton.setSelected(true);
        }
        if(!currentTrain.hasPowerError)
        {
            powerOKRadioButton.setSelected(true);
        }else{
            powerErrorRadioButton.setSelected(true);
        }
        if(currentTrain.controller.authorityEmergencyStop)
        {
            authorityEmergencyStopTrueRadioButton.setSelected(true);
        }else{
            authorityEmergencyStopFalseRadioButton.setSelected(true);
        }
        powerText.setText(currentTrain.currentPower.toString()+" kWatt");
        if(!currentTrain.controller.isAutoMode) {
            manualModeIndicator.setBackground(Color.decode(onIndicatorColor));
        } else {
            manualModeIndicator.setBackground(Color.decode(indicatorOffColor));
        }
        if(currentTrain.leftDoorOpen) {
            leftDoorIndicator.setBackground(Color.decode(onIndicatorColor));
        } else {
            leftDoorIndicator.setBackground(Color.decode(indicatorOffColor));
        }
        if(currentTrain.rightDoorOpen) {
            rightDoorIndicator.setBackground(Color.decode(onIndicatorColor));
        } else {
            rightDoorIndicator.setBackground(Color.decode(indicatorOffColor));
        }
        if(currentTrain.lightOn) {
            lightIndicator.setBackground(Color.decode(onIndicatorColor));
        } else {
            lightIndicator.setBackground(Color.decode(indicatorOffColor));
        }
        if(currentTrain.controller.exceedSpeedLimit) {
            speedLimitIndicator.setBackground(Color.decode(errorColor));
        } else {
            speedLimitIndicator.setBackground(Color.decode(normalColor));
        }
        if(currentTrain.powerLimitReached) {
            powerLimitIndicator.setBackground(Color.decode(errorColor));
        } else {
            powerLimitIndicator.setBackground(Color.decode(normalColor));
        }
        if(currentTrain.emergencyStopActive) {
            emergencyStopIndicator.setBackground(Color.decode(errorColor));
        } else {
            emergencyStopIndicator.setBackground(Color.decode(indicatorOffColor));
        }
        if(currentTrain.serviceBrakeActive) {
            serviceBrakeIndicator.setBackground(Color.decode(errorColor));
        } else {
            serviceBrakeIndicator.setBackground(Color.decode(indicatorOffColor));
        }
    }

}
