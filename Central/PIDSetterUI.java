import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;

public class PIDSetterUI extends Application {

    private PIDSetterSwing innerUi;

    PIDSetterUI(TrainControllerUISwing ctl)
    {
        innerUi = new PIDSetterSwing(ctl);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);

        Pane pane = new Pane();
        pane.getChildren().add(swingNode);

        stage.setScene(new Scene(pane, 200, 200));
        stage.show();
    }

    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingNode.setContent(innerUi.getPanelMain());
                //frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                //frame.pack();
                //frame.setVisible(true);
            }
        });
    }

    public static PIDSetterUI CreatePidSetterUI(TrainControllerUISwing ctl) throws Exception {
        PIDSetterUI ui = new PIDSetterUI(ctl);
        ui.start(new Stage());
        return ui;
    }
}
