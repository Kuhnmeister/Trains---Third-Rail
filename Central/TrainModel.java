import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class TrainModel implements TrainControllerOutputReceiver {
    public static final String onIndicatorColor = "#E8E70C";
    public static final String indicatorOffColor = "#E8E8E8";
    private JFrame frameMain;
    private TrainModelUI ui;
    private static ArrayList<TrainStatus> Trains;
    private TrainStatus currentTrain;

    public TrainModel(ArrayList<TrainStatus> trains)
    {
        Trains = trains;
        CreateGUI();
        Trains.forEach(t -> ui.trainSelector.addItem(t.getName()));
        currentTrain = Trains.get(0);
        ui.trainSelector.addActionListener(e ->{
            int index = ui.trainSelector.getSelectedIndex();
            currentTrain = Trains.get(index);
        });
        update();
    }

    private void CreateGUI()
    {
        ui = new TrainModelUI();
        frameMain = new JFrame("Train Model");
        frameMain.setContentPane(ui.getPanelMain());
        frameMain.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameMain.pack();
        frameMain.setVisible(true);
        ui.emergencyStop.addActionListener(e -> {
            currentTrain.setEmergencyCommand(true);
            currentTrain.update();
        });
    }

    @Override
    public void update() {
        ui.currentPowerText.setText(currentTrain.getCurrentPower().toString());
        //System.out.println("cspeed:"+currentTrain.getCommandSpeed().toString());
        ui.commandSpeedText.setText(currentTrain.getCommandSpeed().toString());
        ui.currentSpeedText.setText(currentTrain.getCurrentSpeed().toString());
        ui.totalWeightText.setText(currentTrain.getTotalWeight().toString());
        ui.nextStopText.setText(currentTrain.getNextStation());
        ui.carFailureText.setText(currentTrain.getCarStatus());
        if(currentTrain.getLeftDoorOpen()) {
            ui.leftDoorIndicator.setBackground(Color.decode(onIndicatorColor));
        } else {
            ui.leftDoorIndicator.setBackground(Color.decode(indicatorOffColor));
        }
        if(currentTrain.getRightDoorOpen()) {
            ui.rightDoorIndicator.setBackground(Color.decode(onIndicatorColor));
        } else {
            ui.rightDoorIndicator.setBackground(Color.decode(indicatorOffColor));
        }
        if(currentTrain.getLightOn()) {
            ui.lightIndicator.setBackground(Color.decode(onIndicatorColor));
        } else {
            ui.lightIndicator.setBackground(Color.decode(indicatorOffColor));
        }
    }

}
