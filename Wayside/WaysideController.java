package Controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WaysideController extends Application{

	//labels for the fields that will be displayed
	private Label controllerLabel, blockLabel, authorityLabel, switchStateLabel, stationLabel, sectionLabel, PLCProgramLabel;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Wayside Controller GUI");
		//set up the labels
		controllerLabel = new Label("Wayside Controller: ");
		blockLabel = new Label("Block: ");
		authorityLabel = new Label("Authority: ");
		switchStateLabel = new Label("SwitchState: ");
		sectionLabel = new Label("Section: ");
		stationLabel = new Label("Station: ");
		PLCProgramLabel = new Label("PLC Program: ");
		
		//create a choice box for selecting the controller
		ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList("Green Line(151 Blocks)", "Red Line"));
		cb.setTooltip(new Tooltip("Select a Line"));

		//create a textfeild for the Block
		TextField blockInput = new TextField();
		blockInput.setPromptText("Enter a Block Number");
		
		//create a feild to display Authority under the submit button
		Label dispAuth = new Label("No Block selected");
		//create a button to submit all entered fields
		Button btn = new Button("Submit");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_CENTER);
		btn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	//TODO make the button update everything when pressed
		    	//first calculate occupancy, light, and authority
		    	dispAuth.setText("Some Auth Value");
		    }
		});
		hbBtn.getChildren().add(btn);
		
		
		//create a gridpane to display information about the blocks under control
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(8);
		grid.setHgap(10);
		grid.add(hbBtn, 0, 10); //TODO move this button down to the bottom of the grid
		
		//use the labels from before to set up the grid info
		GridPane.setConstraints(controllerLabel, 0, 0);
		GridPane.setConstraints(cb, 1, 0);
		GridPane.setConstraints(blockLabel, 0, 1);
		GridPane.setConstraints(blockInput, 1, 1);
		GridPane.setConstraints(authorityLabel, 0, 11);
		GridPane.setConstraints(dispAuth, 1, 11);
		
		
		
		//set choicebox for selecting the block in that section
		grid.getChildren().addAll(controllerLabel, cb, blockLabel, blockInput, dispAuth, authorityLabel);
		//prepare the scene
		Scene scene = new Scene(grid, 400, 600);
		primaryStage.setScene(scene);
		//display the window
		primaryStage.show();
	}

}
