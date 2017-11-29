package train.trainctl;

import train.TrainStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import static TrainController.*;

public class TrainControllerTestbench implements TrainControllerOutputReceiver{

    private TrainController trainControl;
    private ArrayList<TrainStatus> Trains;
    private TrainStatus currentTrain;
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
    private JTextField commandAccelInput;
    private JTextField nextStationInput;
    private JRadioButton authorityEmergencyStopFalseRadioButton;
    private JRadioButton authorityEmergencyStopTrueRadioButton;
    private JButton currentSpeedSet;
    private JButton commandSpeedSet;
    private JButton commandAccelSet;
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

    public TrainControllerTestbench(TrainController trainCtl)
    {
        trainControl = trainCtl;
        Trains = TrainController.Trains;

        currentTrain = Trains.get(0);
        Trains.forEach(t -> trainSelector.addItem(t.getName()));
        update();
        trainSelector.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int index = trainSelector.getSelectedIndex();
                currentTrain = Trains.get(index);
                update();
            }
        });


        inYardTrueRatiodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setInYard(true);
                trainCtl.refresh();
            }
        });
        inYardFalseRatiodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setInYard(false);
                trainCtl.refresh();
            }
        });
        carErrorSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setCarInput(carErrorInput.getText());
                trainCtl.refresh();
            }
        });
        brakeOKRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setHasBrakeError(false);
                trainCtl.refresh();
            }
        });
        brakeErrorRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setHasBrakeError(true);
                trainCtl.refresh();
            }
        });
        powerOKRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setHasPowerError(false);
                trainCtl.refresh();
            }
        });
        powerErrorRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setHasPowerError(true);
                trainCtl.refresh();
            }
        });

        totalWeightSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setTotalWeight(Integer.parseInt(totalWeightInput.getText()));
                trainCtl.refresh();
            }
        });
        maxPowerSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setMaxPower(Integer.parseInt(maxPowerInput.getText()));
                trainCtl.refresh();
            }
        });
        speedLimitSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setSpeedLimit(Integer.parseInt(speedLimitInput.getText()));
                trainCtl.refresh();
            }
        });
        currentSpeedSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setCurrentSpeed(Double.parseDouble(currentSpeedInput.getText()));
                trainCtl.refresh();
            }
        });
        commandSpeedSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setInputSpeed(Integer.parseInt(commandSpeedInput.getText()));
                trainCtl.refresh();
            }
        });
        commandAccelSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setCurrentAccel(Double.parseDouble(commandAccelInput.getText()));
                trainCtl.refresh();
            }
        });
        nextStationSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setNextStation(nextStationInput.getText());
                trainCtl.refresh();
            }
        });

        leftDoorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setLeftDoorCommand(true);
                trainCtl.refresh();
            }
        });
        rightDoorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setRightDoorCommand(true);
                trainCtl.refresh();
            }
        });
        lightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setLightCommand(true);
                trainCtl.refresh();
            }
        });
        authorityEmergencyStopTrueRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setAuthorityEmergencyStop(true);
                trainCtl.refresh();
            }
        });
        authorityEmergencyStopFalseRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTrain.setAuthorityEmergencyStop(false);
                currentTrain.setAuthorityError(false);
                trainCtl.refresh();
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainControl.reset();
                currentTrain = Trains.get(0);
                trainSelector.setSelectedIndex(0);
                System.out.println("AS:"+currentTrain.getAuthorityEmergencyStop().toString());
                update();
            }
        });
        calcButton.addActionListener(e -> {
            trainControl.Calc();
        });
    }

    @Override
    public void update() {
        if(currentTrain.getInYard())
        {
            inYardTrueRatiodButton.setSelected(true);
        }else{
            inYardFalseRatiodButton.setSelected(true);
        }
        if(!currentTrain.getHasBrakeError())
        {
            brakeOKRadioButton.setSelected(true);
        }else{
            brakeErrorRadioButton.setSelected(true);
        }
        if(!currentTrain.getHasPowerError())
        {
            powerOKRadioButton.setSelected(true);
        }else{
            powerErrorRadioButton.setSelected(true);
        }
        if(currentTrain.getAuthorityEmergencyStop())
        {
            authorityEmergencyStopTrueRadioButton.setSelected(true);
        }else{
            authorityEmergencyStopFalseRadioButton.setSelected(true);
        }
        powerText.setText(currentTrain.getCurrentPower().toString()+" Watt");
        if(!currentTrain.getAutoMode()) {
            manualModeIndicator.setBackground(Color.decode(onIndicatorColor));
        } else {
            manualModeIndicator.setBackground(Color.decode(indicatorOffColor));
        }
        if(currentTrain.getLeftDoorOpen()) {
            leftDoorIndicator.setBackground(Color.decode(onIndicatorColor));
        } else {
            leftDoorIndicator.setBackground(Color.decode(indicatorOffColor));
        }
        if(currentTrain.getRightDoorOpen()) {
            rightDoorIndicator.setBackground(Color.decode(onIndicatorColor));
        } else {
            rightDoorIndicator.setBackground(Color.decode(indicatorOffColor));
        }
        if(currentTrain.getLightOn()) {
            lightIndicator.setBackground(Color.decode(onIndicatorColor));
        } else {
            lightIndicator.setBackground(Color.decode(indicatorOffColor));
        }
        if(currentTrain.getSpeedLimitReached()) {
            speedLimitIndicator.setBackground(Color.decode(errorColor));
        } else {
            speedLimitIndicator.setBackground(Color.decode(normalColor));
        }
        if(currentTrain.getPowerLimitReached()) {
            powerLimitIndicator.setBackground(Color.decode(errorColor));
        } else {
            powerLimitIndicator.setBackground(Color.decode(normalColor));
        }
        if(currentTrain.getEmergencyStop()) {
            emergencyStopIndicator.setBackground(Color.decode(errorColor));
        } else {
            emergencyStopIndicator.setBackground(Color.decode(indicatorOffColor));
        }
        if(currentTrain.getServiceBrake()) {
            serviceBrakeIndicator.setBackground(Color.decode(errorColor));
        } else {
            serviceBrakeIndicator.setBackground(Color.decode(indicatorOffColor));
        }
    }
}
