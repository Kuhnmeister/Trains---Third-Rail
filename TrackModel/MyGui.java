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


public class MyGui extends Application {
    private TrackModel theModel;
    private int trainNum=0;
    private ArrayList<Train> allActiveTrains= new ArrayList<Train>();
    private ArrayList<Integer> activeTrainNumbers = new ArrayList<Integer>();
    private ObservableList<Integer> activeTrainNumbersList = FXCollections.observableList(activeTrainNumbers);
    private ArrayList<Node> sectionDisplayLabels;
    private ArrayList<Block> outputToWayside;
    private String outputToWaysideDisplay="";
    private File selectedFile;
    private SimpleStringProperty observableOutputToWayside = new SimpleStringProperty();
    private String outputToTrainsDisplay="";
    private int displayingTrain;
    private SimpleStringProperty observableOutputToTrains = new SimpleStringProperty();
    public static void main(String[] args) {
        launch(args);
    }
    public void SetOutputToWayside(ArrayList<Block> newOutput){
        outputToWayside=newOutput;
        if(outputToWayside.size()==0){
            outputToWaysideDisplay="";
        }else{
            outputToWaysideDisplay=Integer.toString(outputToWayside.get(0).GetBlockNum());
        }
        for(int i=1;i<outputToWayside.size();i++){
           outputToWaysideDisplay=outputToWaysideDisplay+","+outputToWayside.get(i).GetBlockNum();
        }
        System.out.println("Wayside Output Updated: "+outputToWaysideDisplay);
        observableOutputToWayside.set(outputToWaysideDisplay);

    }
    public void SetOutputToTrains(){
        //removes Trains that are now done from the list
        for (Iterator<Train> iterator = allActiveTrains.iterator(); iterator.hasNext();) {
            Train train = iterator.next();
            if (!train.GetActive()) {
                activeTrainNumbersList.remove(train.GetTrainNum());
                iterator.remove();
            }
        }
        if(allActiveTrains.size()==0){
            outputToTrainsDisplay="";
        }else{
            outputToTrainsDisplay="";
            if(displayingTrain != -1) {
                for (int i = 0; i < allActiveTrains.size(); i++) {
                    if (allActiveTrains.get(i).GetTrainNum() == displayingTrain) {
                        outputToTrainsDisplay = outputToTrainsDisplay + "Train " + allActiveTrains.get(i).GetTrainNum() + "\nSpeed Limit: " + allActiveTrains.get(i).GetCurrentBlock().GetSpeedLimit() + "\nGrade: " + allActiveTrains.get(i).GetCurrentBlock().GetGrade();
                        outputToTrainsDisplay=outputToTrainsDisplay+"\nAuthority: "+allActiveTrains.get(i).GetAuthority()+"\nUnderground: " + allActiveTrains.get(i).GetCurrentBlock().GetIsUnderground();
                        outputToTrainsDisplay = outputToTrainsDisplay + "\nStation Next Block: " + allActiveTrains.get(i).GetCurrentBlock().GetNextBlock(+allActiveTrains.get(i).GetDirection()).GetIsStation()+ "\nHas Heater: " + allActiveTrains.get(i).GetCurrentBlock().GetHasHeater();
                        outputToTrainsDisplay = outputToTrainsDisplay + "\n";
                    }
                }
            }
        }
        observableOutputToTrains.set(outputToTrainsDisplay);
    }

    @Override
    public void start(Stage primaryStage) {



        observableOutputToWayside.setValue(outputToWaysideDisplay);
        theModel=new TrackModel(this);
        primaryStage.setTitle("Track Model");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        Text scenetitle = new Text("Load New Track");
        //handles updating our reference to occupied blocks
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    SetOutputToWayside(theModel.getNewWaysideOutput());
                    SetOutputToTrains();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        //Some Titles
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 2, 0, 2, 1);

        Button updateTrackButton = new Button("Update Track");
        updateTrackButton.setOnAction(new EventHandler<ActionEvent>() {


            @Override
            public void handle(ActionEvent e) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Track File");
                fileChooser.getExtensionFilters().addAll(
                        new ExtensionFilter("All Files", "*.*"));
                selectedFile = fileChooser.showOpenDialog(new Stage());
                if(theModel != null && selectedFile !=null) {
                    theModel.LoadNewTrack(selectedFile.getAbsolutePath());
                }
            }
        });
        grid.add(updateTrackButton, 2,1,2,1);


        //Force Majeure
        Text forceMajeureTitle = new Text("Force Majeure Input");
        forceMajeureTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(forceMajeureTitle, 0, 0, 2, 1);
        Label blockAffectedLabel = new Label("Block Affected:");
        grid.add(blockAffectedLabel, 0, 1,1,1);
        TextField blockAffectedTextField = new TextField();
        grid.add(blockAffectedTextField, 1, 1,1,1);
        Button brokenRailButton = new Button("Broken Rail");
        grid.add(brokenRailButton,0,2,1,1);
        Button trackCircuitFailButton = new Button("Track Circuit Failure");
        grid.add(trackCircuitFailButton,1,2,1,1);
        Button powerFailButton = new Button("Power Failure");
        grid.add(powerFailButton,0,3,1,1);
        Button removeAllButton = new Button("Remove All");
        grid.add(removeAllButton,1,3,1,1);
        //Wayside Outputs
        Text outputToWaysideTitle = new Text("Output to Wayside");
        outputToWaysideTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(outputToWaysideTitle, 4, 0, 2, 1);
        Label occupiedBlocksSectionLabel = new Label("OccupiedBlocks:");
        grid.add(occupiedBlocksSectionLabel, 4, 1,2,1);
        Label occupiedBlocksLabel = new Label("");
        occupiedBlocksLabel.textProperty().bind(observableOutputToWayside);
        grid.add(occupiedBlocksLabel,4,2,2,1);
        //Train Outputs
        Text outputToTrainsTitle = new Text("Output to Trains");
        outputToTrainsTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(outputToTrainsTitle, 4, 3, 2, 1);
        Label trainToDisplayLabel = new Label("Train: ");
        grid.add(trainToDisplayLabel,4,4,1,1);
        Label trainOutputsLabel = new Label("");
        trainOutputsLabel.textProperty().bind(observableOutputToTrains);
        grid.add(trainOutputsLabel,4,5,2,4);
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
        Label blockDisplayLabel = new Label("Block");
        grid.add(blockDisplayLabel, 0, 9,1,1);
        Label occupiedDisplayLabel = new Label("Occupied?");
        grid.add(occupiedDisplayLabel, 1, 9,1,1);
        Label lightColorDisplayLabel = new Label("Light Color");
        grid.add(lightColorDisplayLabel, 2, 9,1,1);
        Label gradeDisplayLabel = new Label("Grade");
        grid.add(gradeDisplayLabel, 3, 9,1,1);
        Label speedLimitDisplayLabel = new Label("Speed Limit");
        grid.add(speedLimitDisplayLabel, 4, 9,1,1);
        Label undergroundDisplayLabel = new Label("Underground?");
        grid.add(undergroundDisplayLabel, 5, 9,1,1);
        Label stationDisplayLabel = new Label("Station?");
        grid.add(stationDisplayLabel, 6, 9,1,1);
        Label stationNameDisplayLabel = new Label("Station Name");
        grid.add(stationNameDisplayLabel, 7, 9,1,1);
        Label switchDisplayLabel = new Label("Switch");
        grid.add(switchDisplayLabel, 8, 9,1,1);
        Label switchActiveDisplayLabel = new Label("Switched?");
        grid.add(switchActiveDisplayLabel, 9, 9,1,1);
        Label railwayCrossingDisplayLabel = new Label("Railway Crossing?");
        grid.add(railwayCrossingDisplayLabel, 10, 9,1,1);

        //Demo Mode Inputs
        Text inputTitle = new Text("Wayside Inputs");
        inputTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(inputTitle, 7, 0, 2, 1);
        Label blockChangingLabel = new Label("Block to Edit: ");
        grid.add(blockChangingLabel,7,1,1,1);
        TextField blockChangingTextField = new TextField();
        grid.add(blockChangingTextField, 8,1,1,1);
        Label lightColorInputLabel = new Label("Light Color:");
        grid.add(lightColorInputLabel,7,2,1,1);
        TextField lightColorInputTextField = new TextField();
        grid.add(lightColorInputTextField, 8,2,1,1);
        Label flipSwitchLabel = new Label("Flip Switch: ");
        grid.add(flipSwitchLabel,7,3,1,1);
        CheckBox flipSwitchCheckBox = new CheckBox();
        flipSwitchCheckBox.setIndeterminate(false);
        grid.add(flipSwitchCheckBox,8,3,1,1);
        Button waysideInputButton = new Button("Confirm Input");
        waysideInputButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                theModel.WaysideInput(Integer.parseInt(blockChangingTextField.getCharacters().toString()),lightColorInputTextField.getCharacters().toString(),flipSwitchCheckBox.isSelected());
            }
        });
        grid.add(waysideInputButton, 7, 4,2,1);
        //Wayside Input to pass to trains
        Label waysideTrainInputLabel = new Label("Train to Edit:");
        grid.add(waysideTrainInputLabel,7,5,1,1);
        ComboBox<Integer> trainEditComboBox = new ComboBox<Integer>();
        trainEditComboBox.setItems(activeTrainNumbersList);
        grid.add(trainEditComboBox,8,5,1,1);
        activeTrainNumbersList.addListener(new ListChangeListener<Integer>() {

            @Override
            public void onChanged(ListChangeListener.Change change) {
                trainEditComboBox.setItems(activeTrainNumbersList);
            }
        });

        Label authorityInputLabel = new Label("Authority: ");
        grid.add(authorityInputLabel,7,6,1,1);
        TextField authorityInputTextField = new TextField();
        grid.add(authorityInputTextField, 8,6,1,1);
        Label speedLimitInputLabel = new Label("Train Speed: ");
        grid.add(speedLimitInputLabel,7,7,1,1);
        TextField speedLimitInputTextField = new TextField();
        grid.add(speedLimitInputTextField, 8,7,1,1);


        Button waysideTrainInputButton = new Button("Confirm Input");
        waysideTrainInputButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                Train editingTrain= GetTrain(trainEditComboBox.getSelectionModel().getSelectedItem());
                editingTrain.WaysideInput(Integer.parseInt(authorityInputTextField.getCharacters().toString()),(0!=Integer.parseInt(speedLimitInputTextField.getCharacters().toString())));
            }
        });
        grid.add(waysideTrainInputButton, 7, 8,2,1);
        //Demo Mode Create Train
        Text demoModeTitle = new Text("Demo Mode: Create Train");
        demoModeTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(demoModeTitle, 9, 0, 2, 1);
        //Demo Mode select train line
        Label trainLineLabel = new Label("Train Line: ");
        grid.add(trainLineLabel, 9, 1,1,1);
        TextField trainLineTextField = new TextField();
        grid.add(trainLineTextField, 10,1,1,1);
        //Demo Mode select Starting Block
        Label trainStartLabel = new Label("Starting Block Num: ");
        grid.add(trainStartLabel, 9, 2,1,1);
        TextField trainStartTextField = new TextField();
        grid.add(trainStartTextField, 10,2,1,1);
        //Demo Mode select Starting Block
        Label trainEndLabel = new Label("Ending Block Num: ");
        grid.add(trainEndLabel, 9, 3,1,1);
        TextField trainEndTextField = new TextField();
        grid.add(trainEndTextField, 10,3,1,1);
        //Demo Mode Make Train Button
        Button makeTrainButton = new Button("Make Train");

        makeTrainButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                timeline.stop();
                timeline.play();
                if(theModel != null) {
                    if(theModel.GetStartingBlock(trainLineTextField.getCharacters().toString())!= null) {
                        Train newTrain = new Train(trainNum, 0, theModel.GetBlock(Integer.parseInt(trainStartTextField.getCharacters().toString())),theModel.GetBlock(Integer.parseInt(trainEndTextField.getCharacters().toString())), theModel);
                        allActiveTrains.add(newTrain);
                        activeTrainNumbersList.add(trainNum);
                        System.out.println(trainNum);
                        trainNum++;
                    }
                }
            }
        });
        grid.add(makeTrainButton, 10, 4,1,1);
        ComboBox<Integer> trainSelectComboBox = new ComboBox<Integer>();
        trainSelectComboBox.setItems(activeTrainNumbersList);
        grid.add(trainSelectComboBox,5,4,1,1);
        activeTrainNumbersList.addListener(new ListChangeListener<Integer>() {

            @Override
            public void onChanged(ListChangeListener.Change change) {
                trainSelectComboBox.setItems(activeTrainNumbersList);
            }
        });
        trainSelectComboBox.setOnAction((event) -> {
                    displayingTrain = trainSelectComboBox.getSelectionModel().getSelectedItem();
                });
        primaryStage.show();
    };
    private Train GetTrain(int lookingNum){
        for (Iterator<Train> iterator = allActiveTrains.iterator(); iterator.hasNext();) {
            Train train = iterator.next();
            if (train.GetTrainNum()==lookingNum) {
                return train;
            }
        }
        return null;
    }
}
