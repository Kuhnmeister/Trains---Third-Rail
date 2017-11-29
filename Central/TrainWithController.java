import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.*;
import java.util.ArrayList;

public class TrainWithController {

    private Central central;
    private TrainModel trainModel;
    private TrainController trainControl;
    public TrainWithController(String[] args, Central theCentral) {
        ArrayList<TrainStatus> Trains = new ArrayList<TrainStatus>();
        central = theCentral;

        /*Trains.add(new TrainStatus("Train 1"));
        Trains.add(new TrainStatus("Train 2"));
        Trains.add(new TrainStatus("Train 3"));*/

        TrainController.Trains = Trains;

        trainControl = new TrainController();
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

    void newTrain(Integer id, String name, Integer carNumber, ArrayList<String> stopNames)
    {

    }

    void deleteTrain(Integer id)
    {

    }

    void getCommandSpeed(Integer trainId, Double commandSpeed)
    {

    }

    void getAuthority(Integer trainId, Double Authority)
    {

    }

    void serviceBrake(Integer trainId, Boolean activate)
    {

    }

    void emergencyStop(Integer trainId, Boolean activate)
    {

    }

    void atStation(Integer trainId, Boolean stopAtStation)
    {
         
    }

    void step()
    {

    }

}
