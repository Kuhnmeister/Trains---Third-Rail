import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;

public class TrainModelUI extends Application{

    private TrainModelUISwing innerUi;

    TrainModelUI()
    {
        innerUi = new TrainModelUISwing();

    }

    @Override
    public void start(Stage stage) {
        final SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);

        Pane pane = new Pane();
        pane.getChildren().add(swingNode);

        stage.setScene(new Scene(pane, 570, 200));
        stage.show();
        
    }

    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //JFrame frame = new JFrame("Train Model");
                swingNode.setContent(innerUi.getPanelMain());
                //frame.setContentPane(innerUi.getPanelMain());
                //frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                //frame.pack();
                //frame.setVisible(true);
            }
        });
    }

    void linkToTrainPool(TrainPool trainPool)
    {
        trainPool.modelUI = this.innerUi;
        this.innerUi.trains = trainPool.trains;
        this.innerUi.update();
    }

    public static TrainModelUI CreateTrainModelUI(String[] args)
    {
        System.out.println("Creating Train Model UI");
        TrainModelUI ui = new TrainModelUI();
        ui.start(new Stage());
        return ui;
    }

    public static void main(String[] args) {
        //launch(args);
    }

}
