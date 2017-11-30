import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.stage.FileChooser.*;
import javafx.stage.FileChooser;
import java.io.File;

public class CentralGui extends Application{
    private static Central theCentral;
    private static String[] theArgs;
    public CentralGui(String[] args,Central newCentral){
        theCentral=newCentral;
        theArgs=args;
        launch(args);
    }
    public CentralGui(){

    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Central");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);

        Button createTrackModelButton = new Button("Track Model");
        createTrackModelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                theCentral.CreateTrackModel(theArgs);
            }
        });
        Button createCTCButton = new Button("CTC");
        createCTCButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                theCentral.CreateCTC(theArgs);
            }
        });
        Button createTrainModelButton = new Button("Train Model");
        createTrainModelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                theCentral.CreateTrainWithController(theArgs);
            }
        });

        grid.add(createCTCButton,0,0,2,1);
        grid.add(createTrackModelButton,0,1,2,1);
        grid.add(createTrainModelButton,0,2,2,1);

        primaryStage.show();
    }
}