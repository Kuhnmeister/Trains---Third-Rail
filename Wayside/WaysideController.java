package Controller;

import java.util.ArrayList;

import javax.swing.filechooser.FileNameExtensionFilter;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class WaysideController extends Application{

	//labels for the fields that will be displayed
	private Label controllerLabel, blockLabel, authorityLabel, switchStateLabel, lightLabel, stationLabel, sectionLabel,
	PLCProgramLabel, curBlock, occupyLabel, CTCLabel, inCTCLabel, mphLabel, CTCOutLabel;
	
	//these are used during the updating process
	//this is event driven based on input from the Track model
	private ArrayList<BlockInfo> track;
	private ArrayList<String> trackSections = new ArrayList<String>();
	private ArrayList<ArrayList<String>> blockSections = new ArrayList<ArrayList<String>>();
	private int currentBlock = 0;
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
		CTCLabel = new Label("output to the CTC: ");
		inCTCLabel = new Label("Suggested Speed: ");
		TextField CTCin = new TextField();
		CTCin.setPromptText("Enter input from the CTC");
		mphLabel= new Label("mph");
		CTCOutLabel = new Label();
		
		//call method to get track
		track = getTrack();
		block = track.get(0);
		
		//TODO make this change which wayside object is being used, look up later
		//create a choice box for selecting the controller
		ChoiceBox<String> cb = new ChoiceBox<>();
		cb.getItems().add("Green");
		cb.getItems().add("Red");
		//set a default value
		cb.setValue("Green");
		cb.setTooltip(new Tooltip("Select a Line"));

		//TODO make blockInput choice box that is filled automatically
		ChoiceBox<String> sectionCB = new ChoiceBox<String>();
		ChoiceBox<String> blockInput = new ChoiceBox<String>();
		
		getSections(); 
		
		//for each section of track
		for(int i = 0; i < trackSections.size(); i++) {
			sectionCB.getItems().add(trackSections.get(i));
			//for each block in that section
			for(int j = 0; j < blockSections.get(i).size(); j++)
			{
				blockInput.getItems().add(blockSections.get(i).get(j));
			}
		}
		
		//set default values
		sectionCB.setValue(trackSections.get(0));
		blockInput.setValue(blockSections.get(0).get(0));
		
		//TODO make this a file selector
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
	        		block.setOccupancy(true);
	        		lightLabel.setText("Light State:	Red");
	        		block.setLight(true);
	        		CTCOutLabel.setText(currentBlock +" Occupied");
	        	}else {
	        		occupyLabel.setText("Occupancy: Empty");
	        		block.setOccupancy(false);
	        		lightLabel.setText("Light State:	Green");
	        		block.setLight(false);
	        		CTCOutLabel.setText(currentBlock +" Empty");
	        	}
	        }
	    });
		
		//checkbox for switch state
		CheckBox swiBox = new CheckBox("High");
		swiBox.setTooltip(new Tooltip("Switch is in the high position when checked"));
		//TODO learn how to disable this part when block doesn't have switch
		if(block.hasSwitch()) {
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
		    	blockInput.getValue();
		    }
		});
		
		hbBtn.getChildren().add(btn);
		
		//TODO make this a file selector
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
		GridPane.setConstraints(inCTCLabel, 0, 10);
		GridPane.setConstraints(CTCin, 1, 10);
		GridPane.setConstraints(mphLabel, 2, 10);
		GridPane.setConstraints(CTCLabel, 0, 11);
		GridPane.setConstraints(CTCOutLabel, 1, 11);
		
		
		//set choicebox for selecting the block in that section
		grid.getChildren().addAll(controllerLabel, cb, blockLabel, blockInput, sectionCB, authorityLabel, PLCProgramLabel, PLCInput, curBlock, switchStateLabel, 
		lightLabel, stationLabel, occupyLabel, occBox, swiBox, inCTCLabel, CTCin, mphLabel, CTCLabel, CTCOutLabel);
		//prepare the scene
		Scene scene = new Scene(grid, 800, 600);
		primaryStage.setScene(scene);
		//display the window
		primaryStage.show();
	}
	
	//create an Authority calculator
	public int GetAuthority(ArrayList<BlockInfo> track, int blockNow) {
		int Auth = 0;
		
		//Auth here is the interger for the next not free block
		return Auth;
	}
	
	
	//this method will get the track that this wayside will have control over
	//import from track
	public ArrayList<BlockInfo> getTrack() {
		ArrayList<BlockInfo> testTrack = new ArrayList<BlockInfo>();
		BlockInfo currBlock, prevBlock, nextBlock, switchBlock;
		//the first block will be the yard
		testTrack.add(new BlockInfo(false, 0 , 0 , 1));
		
		for(int i = 1; i < 11; i++)
		{
			if(i < 10) {
				if(i != 5) {
					testTrack.add(new BlockInfo(i - 1, i, i + 1));
				}else {
					testTrack.add(new BlockInfo(true, i - 1, i, i + 1)); //create a block with a crossing @5
				}
			}else {
				testTrack.add(new BlockInfo(false, i - 1, i, 0)); //loop it back to the yard
			}
		}
		
		currBlock = testTrack.get(0);
		//now link the track
		while(testTrack.get(currBlock.blockNumber()).nextBlock1() == null) {
			prevBlock = testTrack.get(currBlock.blockNumber0());
			nextBlock = testTrack.get(currBlock.blockNumber1());
			if(currBlock.blockSwitch() == -1) {
				switchBlock = null;
			}else {
				switchBlock = testTrack.get(currBlock.blockSwitch()).nextBlockSwitch();
			}
			//System.out.println("linking blocks");	
			testTrack.get(currBlock.blockNumber()).setNextBlocks(prevBlock, nextBlock, switchBlock);
			currBlock = currBlock.nextBlock1();
		}
		
		return testTrack;
	}
	
	public void getSections()
	{
		String currentSection = track.get(0).section();
		trackSections.add(currentSection);
		int sectionNumber = 0;
		blockSections.add(new ArrayList<String>());
		
		for(int i = 0; i < track.size(); i++)
		{
			if(track.get(i).section().equals(currentSection)) {
				//add the block number to the section of track
				blockSections.get(sectionNumber).add(Integer.toString(i));
			}else {
				//create a new section
				currentSection = track.get(0).section();
				trackSections.add(currentSection);
				blockSections.add(new ArrayList<String>());
				sectionNumber++;
				blockSections.get(sectionNumber).add(Integer.toString(i));
			}
		}
		//trackSection now holds a string for all sections of the track
		//blockSection now holds a string for every block of the track
	}

}
