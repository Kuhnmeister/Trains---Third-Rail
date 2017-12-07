import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;


//TODO things that will make this a lot more user friendly, but aren't requirements:
//show all occupied blocks in a section
//make entering a PLC easier(unsure of how just yet)

public class WaysideController extends Application{

	//labels for the fields that will be displayed
	private Label controllerLabel, blockLabel, authorityLabel, switchStateLabel, lightLabel, stationLabel, sectionLabel,
	PLCProgramLabel, curBlock, occupyLabel, CTCLabel, inCTCLabel, mphLabel, CTCOutLabel, crossingLabel, inCTCAuth, trackOut,
	trackOutSpeed;
	
	//these are used during the updating process
	//this is event driven based on input from the Track model

	private static Wayside way;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	//create a constructor
	public WaysideController(String[] args, Wayside wayside)
	{
		way = wayside;
		launch(args);
	}
	
	//praise java method overriding
	public WaysideController(String[] args, Wayside wayside, boolean someBool) {
		way = wayside;
	}
	
	public WaysideController() {}
	
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Wayside Controller GUI");
		//set up the labels
		controllerLabel = new Label("Wayside Controller: ");
		blockLabel = new Label("Block:	");
		authorityLabel = new Label("Authority to: ");
		switchStateLabel = new Label("Switch State: Low");
		sectionLabel = new Label("Section: ");
		stationLabel = new Label("Station:	none");
		PLCProgramLabel = new Label("PLC Program: ");
		curBlock = new Label("Block 1 on the Green Line ");
		lightLabel = new Label("Light State:	Green");
		occupyLabel = new Label("Occupancy: Empty");
		CTCLabel = new Label("output to the CTC: ");
		inCTCLabel = new Label("Suggested Speed: ");
		TextField CTCin = new TextField();
		CTCin.setPromptText("Enter input speed");
		inCTCAuth = new Label("Suggested Authority: ");
		TextField CTCinAuth = new TextField();
		CTCinAuth.setPromptText("Enter input Authority");
		mphLabel= new Label("mph");
		CTCOutLabel = new Label();
		crossingLabel = new Label("There is no crossing on this block");
		trackOut = new Label("Output to the track: ");
		trackOutSpeed = new Label("Suggested Speed is: " + way.suggSpeed);
		
		//TODO make this change which wayside object is being used, look up later
		//create a choice box for selecting the controller
		ChoiceBox<String> cb = new ChoiceBox<>();
		cb.getItems().add("Green");
		cb.getItems().add("Red");
		//set a default value
		cb.setValue("Red");
		cb.setTooltip(new Tooltip("Select a Line"));

		//TODO make blockInput choice box that is filled automatically
		ChoiceBox<String> sectionCB = new ChoiceBox<String>();
		ChoiceBox<String> blockInput = new ChoiceBox<String>();
		
		way.track = way.getTrack();
		way.block = way.track.get(0);
		way.getSections(); 
		
		//for each section of track
		//TODO make a choice box for all sections, instead of showing all blocks
		for(int i = 0; i < way.trackSections.size(); i++) {
			sectionCB.getItems().add(way.trackSections.get(i));
			for(int j = 0; j < way.blockSections.get(i).size(); j++)
			{
				blockInput.getItems().add(way.blockSections.get(i).get(j));
			}
		}
		
		
		//set default values
		sectionCB.setValue(way.trackSections.get(0));
		blockInput.setValue(way.blockSections.get(0).get(0));
		
		TextField PLCInput = new TextField();
		PLCInput.setPromptText("Enter a PLC Program");
		//FileChooser fileChooser = new FileChooser();
		//FileNameExtensionFilter PLCFile = new FileNameExtensionFilter("JAVA", "java");
		
		//create a check box button for occupancy
		CheckBox occBox = new CheckBox("Occupied");
		occBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
	        public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
	            //when the occupy box is checked set block to occupied and inform that block's object
	        	if(new_val) {
	        		occupyLabel.setText("Occupancy: Occupied");
	        		way.block.setOccupancy(true);
	        		lightLabel.setText("Light State:	Red");
	        		way.block.setLight(true);
	        		CTCOutLabel.setText(way.currentBlock +" Occupied");
	        		way.currentAuth = way.getAuthority(way.currentBlock, true);
	        		way.block.setCrossing(way.getCrossing(way.currentBlock));
	        		way.AddOccupied(way.currentBlock);
	        	}else {
	        		occupyLabel.setText("Occupancy: Empty");
	        		way.block.setOccupancy(false);
	        		lightLabel.setText("Light State:	Green");
	        		way.block.setLight(false);
	        		CTCOutLabel.setText(way.currentBlock +" Empty");
	        		way.RemoveOccupied(way.currentBlock);
	        	}
	        }

	    });
		
		//checkbox for switch state
		CheckBox swiBox = new CheckBox("High");
		swiBox.setTooltip(new Tooltip("Switch is in the high position when checked"));
		//TODO learn how to disable this part when block doesn't have switch
		if(way.block.hasSwitch()) {
			swiBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
				public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
					//when the switch box is checked set switch to high and inform that block's object
					if(new_val) {
						switchStateLabel.setText("Switch State: High");
						//call a method to move the switch
					}else {
						switchStateLabel.setText("Switch State: Low");
						//call a method to move the switch, return true for success
					}
				}
			});
		}
			
		
		
		//button updates which block is being edited/viewed 
		Button btn = new Button("Get Block");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_LEFT);
		btn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	//get current block from dropdown menu
		    	way.currentBlock = Integer.parseInt(blockInput.getValue());
		    	//update all the current information displayed to match the block
		    	way.block = way.track.get(way.currentBlock);
		    	//check occupancy
		    	if(way.block.occupancy()) {
		    		occupyLabel.setText("Occupancy: Occupied");
		    		lightLabel.setText("Light State:	Red");
		    		CTCOutLabel.setText(way.currentBlock +" Occupied");
		    		occBox.setSelected(true);
		    	}else {
		    		occupyLabel.setText("Occupancy: Empty");
		    		CTCOutLabel.setText(way.currentBlock +" Empty");
		    		occBox.setSelected(false);
		    	}
		    	//check switch
		    	if(way.block.hasSwitch()) {
		    		swiBox.setVisible(true);
		    		if(way.block.switchState()) {
		    			switchStateLabel.setText("Switch State: High");
					}else {
						switchStateLabel.setText("Switch State: Low");
					}
		    	}else {
		    		switchStateLabel.setText("Switch State: none");
		    		//hide the check box for blocks without switches
		    		swiBox.setVisible(false);
		    	}
		    	//update crossing label
		    	if(way.block.hasCrossing())
		    	{
		    		if(way.block.crossingOn()) 
		    		{
		    			crossingLabel.setText("The crossing on this block is: ON");
		    		} else {
		    			crossingLabel.setText("The crossing on this block is: OFF");
		    		}
		    	}
		    	//check crossing
		    	//check authority
		    	way.currentAuth = way.getAuthority(way.currentBlock, true);
		    	authorityLabel.setText("Authority to Block: " + way.currentAuth.toString());
		    	trackOut.setText("Output to the track: Authority to " + way.currentAuth.toString());
		    }
		});
		
		hbBtn.getChildren().add(btn);
		
		//TODO make this a file selector
		Button Pbtn = new Button("Submit PLC");
		HBox PLCBtn = new HBox(10);
		PLCBtn.setAlignment(Pos.BOTTOM_RIGHT);
		Pbtn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	 if ((PLCInput.getText() != null && !PLCInput.getText().isEmpty())) {
		    		 //get the path to a java file to run for the PLC
		    		 way.choosenPLC = PLCInput.getText();
		    		 way.selectedLine = cb.getValue();
		    		 way.PLC = way.getPLC();
		    	 }
		    }
		});
		//adding buttons
		PLCBtn.getChildren().add(Pbtn);
		
		//create the input button for CTC
		Button CTCSbtn = new Button("Submit Speed");
		Button CTCAbtn = new Button("Submit Authority");
		HBox Sbtn = new HBox(10);
		HBox Abtn = new HBox(10);
		Sbtn.setAlignment(Pos.BOTTOM_LEFT);
		Abtn.setAlignment(Pos.BOTTOM_RIGHT);
		//create action listener for buttons
		CTCSbtn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	//TODO make the button update everything when pressed
		    	//read in the block number from the text field
		    	 if ((CTCin.getText() != null && !CTCin.getText().isEmpty())) {
		    		 way.suggSpeed = Double.parseDouble(CTCin.getText());
		    		 trackOutSpeed.setText("Suggested Speed is: " + way.suggSpeed);
		    		 //output to track
		    		 way.SuggestSpeed(way.currentBlock, 1, way.suggSpeed);
		    	 }
		    	//first calculate occupancy, light, and authority
		    }
		});
		CTCAbtn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	//TODO make the button update everything when pressed
		    	//read in the block number from the text field
		    	 if ((CTCinAuth.getText() != null && !CTCinAuth.getText().isEmpty())) {
		    		 way.suggBlock = Integer.parseInt(CTCinAuth.getText());
		    		 //calculate auth from current block, check it has the suggBlock
		    		 way.currentAuth = way.getAuthority(way.currentBlock, true);
		    		 if(way.currentAuth.contains(way.suggBlock)) {
		    			//safe authority
		    			int index = way.currentAuth.indexOf(way.suggBlock);
		    			way.currentAuth = new ArrayList<Integer> (way.currentAuth.subList(0, index));
		    			authorityLabel.setText("Authority to Block: " + way.currentAuth.toString());
		 		    	trackOut.setText("Output to the track: Authority to " + way.currentAuth.toString());
		    		 }else {
		    			 //unsafe, just use the calculated Authority
		    			authorityLabel.setText("Authority to Block: " + way.currentAuth.toString());
			 		    trackOut.setText("Output to the track: Authority to " + way.currentAuth.toString());
		    		 }
		    	 }
		    	//first calculate occupancy, light, and authority
		    }
		});
		
		Sbtn.getChildren().add(CTCSbtn);
		Abtn.getChildren().add(CTCAbtn);
		
		//set switch to none unless otherwise stated
		switchStateLabel.setText("No switch on this block");
		
		//create a gridpane to display information about the blocks under control
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(8);
		grid.setHgap(10);
		grid.add(hbBtn, 0, 3);
		grid.add(PLCBtn, 1, 3);
		
		//use the labels from before to set up the grid info
		GridPane.setConstraints(controllerLabel, 0, 0);
		GridPane.setConstraints(cb, 1, 0);
		GridPane.setConstraints(blockLabel, 0, 1);
		GridPane.setConstraints(sectionCB, 1, 1);
		GridPane.setConstraints(blockInput, 2, 1);
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
		GridPane.setConstraints(crossingLabel, 1, 8);
		GridPane.setConstraints(inCTCLabel, 0, 10);
		GridPane.setConstraints(CTCin, 1, 10);
		GridPane.setConstraints(mphLabel, 2, 10);
		GridPane.setConstraints(inCTCAuth, 0, 11);
		GridPane.setConstraints(CTCinAuth, 1, 11);
		GridPane.setConstraints(Sbtn, 0, 12);
		GridPane.setConstraints(Abtn, 1, 12);
		GridPane.setConstraints(CTCLabel, 0, 13);
		GridPane.setConstraints(CTCOutLabel, 1, 13);
		GridPane.setConstraints(trackOut, 0, 14);
		GridPane.setConstraints(trackOutSpeed, 1, 14);
		
		
		
		//set choicebox for selecting the block in that section
		grid.getChildren().addAll(controllerLabel, cb, blockLabel, blockInput, sectionCB, authorityLabel, PLCProgramLabel, PLCInput, curBlock, switchStateLabel, 
		lightLabel, stationLabel, occupyLabel, occBox, swiBox, inCTCLabel, CTCin, mphLabel, CTCLabel, CTCOutLabel,crossingLabel, CTCinAuth, inCTCAuth, Sbtn,
		Abtn, trackOut, trackOutSpeed);
		//prepare the scene
		Scene scene = new Scene(grid, 800, 600);
		primaryStage.setScene(scene);
		//display the window
		primaryStage.show();
	}
}
