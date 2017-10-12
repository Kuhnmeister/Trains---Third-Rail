package Controller;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WaysideController extends Application{

	//labels for the fields that will be displayed
	private Label controllerLabel, blockLabel, authorityLabel, switchStateLabel, lightLabel, stationLabel, sectionLabel,
	PLCProgramLabel, curBlock, occupyLabel;
	
	//keep track of the current block, for updating reasons
	private BlockInfo block;
	private String selectedBlock, PLC, selectedLine; 
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Wayside Controller GUI");
		//set up the labels
		controllerLabel = new Label("Wayside Controller: ");
		blockLabel = new Label("Block: ");
		authorityLabel = new Label("Authority: ");
		switchStateLabel = new Label("Switch State: Low");
		sectionLabel = new Label("Section: ");
		stationLabel = new Label("Station: ");
		PLCProgramLabel = new Label("PLC Program: ");
		curBlock = new Label("Please enter a block");
		lightLabel = new Label("Light State: ");
		occupyLabel = new Label("Occupancy: Empty");
		
		//call method to get a test track
		BlockInfo[] track = CreateFive();
		
		//create a choice box for selecting the controller
		ChoiceBox<String> cb = new ChoiceBox<>();
		cb.getItems().add("Green");
		cb.getItems().add("Red");
		//set a default value
		cb.setValue("Green");
		//nice little tooltip for guidance
		cb.setTooltip(new Tooltip("Select a Line"));

		//create a textfeild for the Block & PLC Program
		TextField blockInput = new TextField();
		blockInput.setPromptText("Enter a Block Number: ");
		TextField PLCInput = new TextField();
		PLCInput.setPromptText("Enter a PLC Program");
		
		//create a check box button for occupancy
		CheckBox occBox = new CheckBox("Occupied");
		occBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
	        public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
	              //when the occupy box is checked set block to occupied and inform that block's object
	        	if(new_val) {
	        		occupyLabel.setText("Occupancy: Occupied");
	        	}else {
	        		occupyLabel.setText("Occupancy: Empty");
	        	}
	        }
	    });
		
		//checkbox for switch state
		CheckBox swiBox = new CheckBox("High");
		swiBox.setTooltip(new Tooltip("Switch is in the high position when checked"));
		swiBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
	        public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
	              //when the switch box is checked set switch to high and inform that block's object
	        	if(new_val) {
	        		switchStateLabel.setText("Switch State: High");
	        	}else {
	        		switchStateLabel.setText("Switch State: Low");
	        	}
	        }
	    });
		
		//create a button to submit all entered fields
		Button btn = new Button("Submit Block");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_LEFT);
		btn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	//TODO make the button update everything when pressed
		    	//read in the block number from the textfield
		    	 if ((blockInput.getText() != null && !blockInput.getText().isEmpty())) {
		    		 //get information form the GUI 
		    		 selectedBlock = blockInput.getText();
		    		 selectedLine = cb.getValue();
		    		 curBlock.setText("Block " + selectedBlock + " on the " + selectedLine + " Line");
		    		 //TODO make the blocks update all fields when submitted
		    		 
		    	 }
		    	//first calculate occupancy, light, and authority
		    }
		});
		//button for PLC
		Button Pbtn = new Button("Submit PLC");
		HBox PLCBtn = new HBox(10);
		PLCBtn.setAlignment(Pos.BOTTOM_RIGHT);
		Pbtn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	//TODO make the button update everything when pressed
		    	//read in the block number from the textfield
		    	 if ((PLCInput.getText() != null && !PLCInput.getText().isEmpty())) {
		    		 //get the path to a java file to run for the PLC
		    		 PLC = PLCInput.getText();
		    		 selectedLine = cb.getValue();
		    	 }
		    	//first calculate occupancy, light, and authority
		    }
		});
		//adding buttons
		PLCBtn.getChildren().add(Pbtn);
		hbBtn.getChildren().add(btn);
		
		//set switch to none unless otherwise stated
		switchStateLabel.setText("No switch on this block");
		
		//create a gridpane to display information about the blocks under control
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(8);
		grid.setHgap(10);
		grid.add(hbBtn, 0, 3); //TODO move this button down to the bottom of the grid
		grid.add(PLCBtn, 1, 3);
		
		//use the labels from before to set up the grid info
		GridPane.setConstraints(controllerLabel, 0, 0);
		GridPane.setConstraints(cb, 1, 0);
		GridPane.setConstraints(blockLabel, 0, 1);
		GridPane.setConstraints(blockInput, 1, 1);
		GridPane.setConstraints(PLCProgramLabel, 0, 2);
		GridPane.setConstraints(PLCInput, 1, 2);
		GridPane.setConstraints(curBlock, 0, 4);
		GridPane.setConstraints(occupyLabel, 0, 5);
		GridPane.setConstraints(occBox, 3, 5);
		GridPane.setConstraints(authorityLabel, 0, 6);
		GridPane.setConstraints(switchStateLabel, 0, 7);
		GridPane.setConstraints(swiBox, 3, 7);
		GridPane.setConstraints(lightLabel, 0, 8);
		GridPane.setConstraints(stationLabel, 0, 9);
		
		
		
		//set choicebox for selecting the block in that section
		grid.getChildren().addAll(controllerLabel, cb, blockLabel, blockInput, authorityLabel, PLCProgramLabel, PLCInput, curBlock, switchStateLabel, 
		lightLabel, stationLabel, occupyLabel, occBox, swiBox);
		//prepare the scene
		Scene scene = new Scene(grid, 600, 800);
		primaryStage.setScene(scene);
		//display the window
		primaryStage.show();
	}
	
	//create a method to initialize the first few blocks to show functionality
	public BlockInfo[] CreateFive() {
		BlockInfo[] testTrack = new BlockInfo[4]; //create an array of the 5 peices of track for the test
		//load the track info for each track starting at green 1 through green 5
		testTrack[0] = new BlockInfo(false, false, null, "Green");
		testTrack[1] = new BlockInfo(false, false, null, "Green");
		testTrack[2] = new BlockInfo(false, false, null, "Green");
		testTrack[3] = new BlockInfo(false, false, null, "Green");
		
		return testTrack;
	}

}
