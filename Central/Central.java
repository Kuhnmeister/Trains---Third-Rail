import java.lang.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Central{
	private TrackModel trackModel;
	private Wayside wayside;
	private CTCcontroller ctc;
	private CentralGui centralGui;
	private boolean hasTrainModel=false;
	private String[] args;
	public static void main(String[] args){
		Central thisCentral = new Central(args);
	}
	public Central(String[] Args){
		centralGui = new CentralGui(Args,this);
		args=Args;
	}
	public void TestMethod(String testString){
		System.out.println(testString);
	}
	//*********************He, put the code to instantiate your stuff here****************************
	public void CreateTrainModel(String[] theArgs){

	}
	public void CreateTrainController(String[] theArgs){

	}
	public void CreateTrackModel(String[] emptyArgs){
		System.out.println("Create Track Model");
		trackModel=new TrackModel(emptyArgs, this);
	}
	public void CreateCTC(String[] emptyArgs){
		System.out.println("Create CTC");
		ctc=new CTCcontroller(this);
	}

	public void CreateTrain(int trainNum, int length, int direction,int startBlock, String line){
		if(hasTrainModel) {
			trackModel.NewTrain(trainNum, length*2, direction, startBlock, line);
		}else{
			trackModel.NewTrain(trainNum, length*2, direction, startBlock,line, true);
		}
	}
	public void UpdateTrack(HashMap<String,HashMap<String,ArrayList<Block>>> track){
		System.out.println("Updating the track");
		ctc.receiveTrackData(track);
		wayside=new Wayside(args, this, track);
	}
	public void SuggestSpeed(int blockNum, int trainNum, double speed){
		wayside.SuggestSpeed(blockNum,trainNum,speed);
	}
	public void WaysideCommandedSpeed(int trainNum, double speed){
		if(!hasTrainModel) {
			trackModel.WaysideCommandedSpeed(trainNum,speed,true);
		}else{
			trackModel.WaysideCommandedSpeed(trainNum, speed);
		}
	}
	public void TrackModelCommandedSpeed(int trainNum, double speed){

	}
	public void UpdateTrainDistance(int trainId, float  movedDistance){

	}
	public void TrackStateUpdate(int occBlock){

	}
	public void WaysideAddOccupied(int blockNum){
		wayside.AddOccupied(blockNum);
	}
	public void WaysideRemoveOccupied(int blockNum){
		wayside.RemoveOccupied(blockNum);
	}
	public void CTCAuthority(int location, int authority, int trainNum){
		wayside.SuggestAuthority(location, authority, trainNum);
	}
	public void WaysideSendAuthority(ArrayList<Integer> authorityBlocks, ArrayList<Integer> authorityBlocks1, int trainNum){
		if(!hasTrainModel) {
			trackModel.CommandedAuthority(authorityBlocks,trainNum,true);
		}else{
			trackModel.CommandedAuthority(authorityBlocks,trainNum);
		}
	}
	public boolean CTCMoveSwitch(int blockNum, boolean state)
	{
		return wayside.SwitchSwitch(blockNum, state);
	}
	public void TrackMoveSwitch(int blockNum, String Line)
	{
		//trackModel.flipSwitch();
	}
	public void Update(int mulitplyer){

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