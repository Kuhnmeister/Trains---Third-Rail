import java.lang.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Central{
	private TrackModel trackModel;
	private Wayside wayside;
	private CentralGui centralGui;
	public static void main(String[] args){
		Central thisCentral = new Central(args);
	}
	public Central(String[] args){
		centralGui = new CentralGui(args,this);

	}
	public void TestMethod(String testString){
		System.out.println(testString);
	}

	public void CreateTrackModel(String[] emptyArgs){
		System.out.println("Create Track Model");
		trackModel=new TrackModel(emptyArgs, this);
	}
	public void CreateWayside(String[] emptyArgs){
		System.out.println("Create Wayside");
		wayside=new Wayside(emptyArgs, this);
	}

	public void CreateTrain(int trainNum, int trainLength){

	}
	public void UpdateTrack(HashMap<String,HashMap<String,ArrayList<Block>>> track){
		System.out.println("Updating the track");
	}
	public void UpdateTrainDistance(int trainId, float  movedDistance){

	}
	public void Update(){

	}
	/*
	public void suggestedSpeed(int blockForTrain, double speedForTrain){
		XXXX.receiveSpeed(blockForTrain, speedForTrain);
	}
	public void suggestedAuthority(int blockForTrain, int blockForAuthority){
		XXXX.receiveAuthority(blockForTrain, blockForAuthority);
	}
	public void switchSignalWayside(int blockWithSwitch){
		XXXX.signalSwitchChange(blockWithSwitch);
	}
	public void updateSales(int numOfTickets){
		CTC.receiveTickets(numOfTickets);
	}
	public void updateBlocks(ArrayList<int> updatedBlocks){
		Tracking.update(updatedBlocks);
	}
	public void sendTrack(HashMap<String,HashMap<String,ArrayList<Block>>> track){
		Tracking.receiveTrackData(track);
		XXXX.receiveTrackData(track);
	}
	*/
	
}