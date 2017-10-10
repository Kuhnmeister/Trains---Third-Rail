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
import java.util.ArrayList;
import javafx.scene.Node;


public class MyGui extends Application {
    private TrackModel theModel;
    private ArrayList<Train> allActiveTrains= new ArrayList<Train>();
    private ArrayList<Node> sectionDisplayLabels;
    public static void main(String[] args) {

        launch(args);
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

        Scene scene = new Scene(grid);
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
        Button showTrackButton = new Button("Show Track");
        showTrackButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                if(theModel != null) {
                    if(sectionDisplayLabels != null){

                        grid.getChildren().removeAll(sectionDisplayLabels);
                    }else{
                        System.out.println("This was null, this should only occur on the first section display");
                    }
                    sectionDisplayLabels = new ArrayList<Node>();
                    ArrayList<String> sectionString = theModel.DisplaySection(lineSelectionTextField.getCharacters().toString(),sectionTextField.getCharacters().toString());
                    for(int i=0;i<sectionString.size();i++) {

                        String[] blockData = sectionString.get(i).split(",");
                        for(int j=0;j<blockData.length;j++) {
                            Label lineSelectionLabel = new Label(blockData[j]);
                            sectionDisplayLabels.add(lineSelectionLabel);
                            grid.add(lineSelectionLabel, j, 10+i, 1, 1);
                        }
                    }

                }
            }
        });
        grid.add(showTrackButton, 0, 8,1,1);
        //Section display Labels
        Label lineDisplayLabel = new Label("Line");
        grid.add(lineDisplayLabel, 0, 9,1,1);
        Label sectionDisplayLabel = new Label("Section");
        grid.add(sectionDisplayLabel, 1, 9,1,1);
        Label blockDisplayLabel = new Label("Block");
        grid.add(blockDisplayLabel, 2, 9,1,1);
        Label occupiedDisplayLabel = new Label("Occupied?");
        grid.add(occupiedDisplayLabel, 3, 9,1,1);
        Label lightColorDisplayLabel = new Label("Light Color");
        grid.add(lightColorDisplayLabel, 4, 9,1,1);
        Label gradeDisplayLabel = new Label("Grade");
        grid.add(gradeDisplayLabel, 5, 9,1,1);
        Label speedLimitDisplayLabel = new Label("Speed Limit");
        grid.add(speedLimitDisplayLabel, 6, 9,1,1);
        Label undergroundDisplayLabel = new Label("Underground?");
        grid.add(undergroundDisplayLabel, 7, 9,1,1);
        Label stationDisplayLabel = new Label("Station?");
        grid.add(stationDisplayLabel, 8, 9,1,1);
        Label stationNameDisplayLabel = new Label("Station Name");
        grid.add(stationNameDisplayLabel, 9, 9,1,1);
        Label switchDisplayLabel = new Label("Switch?");
        grid.add(switchDisplayLabel, 10, 9,1,1);
        Label railwayCrossingDisplayLabel = new Label("Railway Crossing?");
        grid.add(railwayCrossingDisplayLabel, 11, 9,1,1);
        //Demo Mode Create Train
        Button makeTrainButton = new Button("Demo Train");
        makeTrainButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                if(theModel != null) {
                    //constructor = int newTrainNum,int newDirection,Block newCurrentBlock,TrackModel newModel
                    Train newTrain = new Train(allActiveTrains.size(),0,theModel.GetStartingBlock(),theModel);
                    allActiveTrains.add(newTrain);
                }
            }
        });
        grid.add(makeTrainButton, 10, 0,1,1);
        primaryStage.show();
    };
}
