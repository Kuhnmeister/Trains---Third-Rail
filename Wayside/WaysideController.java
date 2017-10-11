package Controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WaysideController extends Application{

	//labels for the fields that will be displayed
	private Label controller, block, authority, switchState, station, PLCProgram;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		//set up the labels
		controller = new Label("Wayside Controller: ");
		block = new Label("Block: ");
		authority = new Label("Authority: ");
		switchState = new Label("SwitchState: ");
		station = new Label("Station: ");
		PLCProgram = new Label("PLC Program: ");
		
		//display the window
		primaryStage.show();
	}

}
