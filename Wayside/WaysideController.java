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
	private int currentBlock = 1;
	private int currentAuth = 0;
	private BlockInfo block = null;
	private String selectedBlock, PLC, selectedLine; 
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Wayside Controller GUI");
		//set up the labels
		controllerLabel = new Label("Wayside Controller: ");
		blockLabel = new Label("Block:	");
		authorityLabel = new Label("Authority: ");
		switchStateLabel = new Label("Switch State: Low");
		sectionLabel = new Label("Section: ");
		stationLabel = new Label("Station:	none");
		PLCProgramLabel = new Label("PLC Program: ");
		curBlock = new Label("Block 1 on the Green Line ");
		lightLabel = new Label("Light State:	Green");
		occupyLabel = new Label("Occupancy: Empty");
		
		//call method to get a test track
		BlockInfo[] track = CreateFive();
		block = track[0];
		
		//create a choice box for selecting the controller
		ChoiceBox<String> cb = new ChoiceBox<>();
		cb.getItems().add("Green");
		cb.getItems().add("Red");
		//set a default value
		cb.setValue("Green");
		//nice little tooltip for guidance
		cb.setTooltip(new Tooltip("Select a Line"));

		//create a textfeild for the Block & PLC Program
		TextField blockInput = new TextField("1");
		TextField PLCInput = new TextField();
		PLCInput.setPromptText("Enter a PLC Program");
		
		//create a check box button for occupancy
		CheckBox occBox = new CheckBox("Occupied");
		occBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
	        public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
	              //when the occupy box is checked set block to occupied and inform that block's object
	        	if(new_val) {
	        		occupyLabel.setText("Occupancy: Occupied");
	        		block.occupy = true;
	        		lightLabel.setText("Light State:	Red");
	        		block.light = true;
	        	}else {
	        		occupyLabel.setText("Occupancy: Empty");
	        		block.occupy = false;
	        		lightLabel.setText("Light State:	Green");
	        		block.light = false;
	        	}
	        }
	    });
		
		//checkbox for switch state
		CheckBox swiBox = new CheckBox("High");
		swiBox.setTooltip(new Tooltip("Switch is in the high position when checked"));
		if(block.switchState != null) {
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
		}
			
		
		
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
		    		 
		    		 //write the updates to the track object before clearing the block
		    		 track[currentBlock - 1] = block;
		    		 
		    		 //update text fields with existing data
		    		 currentBlock = Integer.parseInt(blockInput.getText());
		    		 if(currentBlock < 5 && currentBlock > 0) {
		    			 block = track[currentBlock - 1];
		    		 }
		    		 //now set the values for the block when block is submitted
		    		occBox.setSelected(block.occupy);
		    		if(block.switchState != null) {
		    			swiBox.setSelected(block.switchState);
		    		}
		    		
		    		//get and display Auth for the new block
		    		currentAuth = GetAuthority(track, currentBlock);
		    		authorityLabel.setText("Authority:    " + currentAuth + " Blocks ");
		    		 
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
	
	//create an Authority calculator
	public int GetAuthority(BlockInfo[] track, int blockNow) {
		int Auth = 0;
		
		//get number of free blocks ahead of the train
		for(int i = 0; i < track.length - blockNow; i++) {
			//if there is no train on the next block Authority is increased
			if(!(track[blockNow + i].occupy)) {
				Auth++;
			}else {
				break; //leave the for loop and return the calculated Authority
			}
		}
		
		return Auth;
	}
	
	
	//create a method to initialize the first few blocks to show functionality
	public BlockInfo[] CreateFive() {
		BlockInfo[] testTrack = new BlockInfo[5]; //create an array of the 5 peices of track for the test
		//load the track info for each track starting at green 1 through green 5
		testTrack[0] = new BlockInfo(false, false, null, "Green");
		testTrack[1] = new BlockInfo(false, false, false, "Green");
		testTrack[2] = new BlockInfo(false, false, null, "Green");
		testTrack[3] = new BlockInfo(false, false, null, "Green");
		testTrack[4] = new BlockInfo(false, false, false, "Green");
		return testTrack;
	}

}
