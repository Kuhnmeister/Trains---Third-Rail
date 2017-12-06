import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.*;
import java.util.ArrayList;

public class TrainWithController {

    private Central central;
    private TrainModel trainModel;
    private TrainController trainControl;
    private ArrayList<TrainStatus> Trains;
    public TrainWithController(String[] args, Central theCentral) {

        Trains =  new ArrayList<TrainStatus>();
        central = theCentral;

        /*Trains.add(new TrainStatus("Train 1"));
        Trains.add(new TrainStatus("Train 2"));
        Trains.add(new TrainStatus("Train 3"));*/

        TrainController.Trains = Trains;

        trainControl = new TrainController(this);
        JFrame frame = new JFrame("Train Controller");
        frame.setContentPane(trainControl.getPanelMain());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        /*TrainControllerTestbench testbench = new TrainControllerTestbench(trainControl);
        trainControl.addOutputReceiver(testbench);
        JFrame frame2 = new JFrame("Testbench");
        frame2.setContentPane(testbench.PanelMain);
        frame2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame2.pack();
        frame2.setVisible(true);*/

        trainModel = new TrainModel(Trains);
        trainControl.addOutputReceiver(trainModel);
    }

    private Integer findTrainIndexById(Integer id)
    {
        for(int i=0; i<Trains.size(); i++)
        {
            if(Trains.get(i).id.equals(id))
            {
                return i;
            }
        }
        return -1;
    }

    Boolean newTrain(Integer id, String name, Integer carNumber, ArrayList<String> stopNames)
    {
        if(findTrainIndexById(id)!=-1)
        {
            return false;
        }
        TrainStatus aTrain = new TrainStatus(name, carNumber, this, stopNames);
        Trains.add(aTrain);
        trainControl.refresh();
        return true;
    }

    Boolean deleteTrain(Integer id)
    {
        Integer index = findTrainIndexById(id);
        if(index == -1)
        {
            return false;
        }
        Trains.remove(index);
        trainControl.refresh();
        return true;
    }

    void getInYard(Integer trainId, Boolean InYard)
    {
        Integer index = findTrainIndexById(trainId);
        Trains.get(index).setInYard(InYard);
        trainControl.refresh();
    }

    void getCommandSpeed(Integer trainId, Double commandSpeed)
    {
        Integer index = findTrainIndexById(trainId);
        Trains.get(index).commandSpeed = commandSpeed;
        trainControl.refresh();
    }

    void getSpeedLimit(Integer trainId, Double speedLimit)
    {
        Integer index = findTrainIndexById(trainId);
        Trains.get(index).speedLimit = speedLimit;
        trainControl.refresh();
    }

    void getAuthority(Integer trainId, Double authority)
    {
        Integer index = findTrainIndexById(trainId);
        Trains.get(index).authority = authority;
        trainControl.refresh();
    }

    void serviceBrake(Integer trainId, Boolean activate)
    {
        Integer index = findTrainIndexById(trainId);
        trainControl.refresh();
    }

    void emergencyStop(Integer trainId, Boolean activate)
    {
        Integer index = findTrainIndexById(trainId);
        Trains.get(index).authorityEmergencyStop = activate;
        trainControl.refresh();
    }

    void atStation(Integer trainId, Boolean stopAtStation)
    {
        Integer index = findTrainIndexById(trainId);
        Trains.get(index).stopAtStation = stopAtStation;
        trainControl.refresh();
    }

    void step()
    {
        trainControl.Calc();
    }

    void triggerServiceBrake(Integer trainId, Boolean activate)
    {

        central.ServiceBrakeFromTrain(trainId, activate);
    }

    void triggerEmergencyStop(Integer trainId, Boolean activate)
    {
        central.EmergencyStopFromTrain(trainId, activate);
    }

    void updateTrainDistance(Integer trainId, Double movedDistance)
    {
        central.UpdateTrainDistance(trainId, movedDistance.floatValue());
    }


}
