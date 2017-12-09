import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;

public class TrainControllerUI extends Application{

    private TrainControllerUISwing innerUi;

    TrainControllerUI()
    {
        innerUi = new TrainControllerUISwing();
    }

    @Override
    public void start(Stage stage) {
        final SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);

        Pane pane = new Pane();
        pane.getChildren().add(swingNode);

        stage.setScene(new Scene(pane, 900, 250));
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
        trainPool.controllerUI = this.innerUi;
        this.innerUi.trains = trainPool.trains;
        this.innerUi.update();
    }

    public static TrainControllerUI CreateTrainControllerUI(String[] args)
    {
        TrainControllerUI ui = new TrainControllerUI();
        ui.start(new Stage());
        return ui;
    }

    public static void main(String[] args) {
        //launch(args);
    }

}
