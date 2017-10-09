import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.scene.control.*;


public class MyGui extends Application {
    private TrackModel theModel;
    public static void main(String[] args) {

        launch(args);
    }
    public void SetTrackModel(TrackModel newModel){
        theModel=newModel;
        System.out.println("Um, ok this might work");
    }
    @Override
    public void start(Stage primaryStage) {
        theModel=new TrackModel();
        primaryStage.setTitle("Track Model");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        Text scenetitle = new Text("Load New Track");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 2, 0, 2, 1);
        Label newFileLabel = new Label("Track Filename:");
        grid.add(newFileLabel, 2, 2,1,1);
        TextField trackFileTextField = new TextField();
        grid.add(trackFileTextField, 3, 2,1,1);
        Button updateTrackButton = new Button("Update Track");
        updateTrackButton.setOnAction(new EventHandler<ActionEvent>() {


            @Override
            public void handle(ActionEvent e) {
                if(theModel != null) {
                    theModel.LoadNewTrack(trackFileTextField.getCharacters().toString());
                }
            }
        });
        grid.add(updateTrackButton, 2,3,1,1);

        Button showTrackButton = new Button("Show Track");
        showTrackButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                if(theModel != null) {
                    theModel.PrintTrack();
                }
            }
        });
        grid.add(showTrackButton, 3, 3,1,1);
        //Force Majeure
        Text forceMajeureTitle = new Text("Force Majeure Input");
        forceMajeureTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(forceMajeureTitle, 0, 0, 2, 1);
        Label blockAffectedLabel = new Label("Block Affected:");
        grid.add(blockAffectedLabel, 0, 2,1,1);
        TextField blockAffectedTextField = new TextField();
        grid.add(blockAffectedTextField, 1, 2,1,1);
        Button brokenRailButton = new Button("Broken Rail");
        grid.add(brokenRailButton,0,3,1,1);
        Button trackCircuitFailButton = new Button("Track Circuit Failure");
        grid.add(trackCircuitFailButton,1,3,1,1);
        Button powerFailButton = new Button("Power Failure");
        grid.add(powerFailButton,0,4,1,1);
        Button removeAllButton = new Button("Remove All");
        grid.add(removeAllButton,1,4,1,1);
        //Outputs
        //Line & Section Display Selection
        Text lineSectionSelectionTitle = new Text("Line & Section Display");
        lineSectionSelectionTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(lineSectionSelectionTitle, 0, 5, 2, 1);
        Label lineSelectionLabel = new Label("Line:");
        grid.add(lineSelectionLabel, 0, 6,1,1);
        TextField lineSelectionTextField = new TextField();
        grid.add(lineSelectionTextField, 1, 6,1,1);
        Label sectionSelectionLabel = new Label("Section:");
        grid.add(sectionSelectionLabel, 0, 7,1,1);
        TextField sectionTextField = new TextField();
        grid.add(sectionTextField, 1, 7,1,1);

        primaryStage.show();
    };
}
