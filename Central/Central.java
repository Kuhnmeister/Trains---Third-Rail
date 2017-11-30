import com.sun.org.apache.xpath.internal.operations.Bool;

import java.lang.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.BitSet;

public class Central{
	private TrackModel trackModel;
	private Wayside wayside;
	private CTCcontroller ctc;
	private CentralGui centralGui;
	private TrainWithController trainWithControl;
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

	// TrainModel and Controller are now one module.
	public void CreateTrainWithController(String[] emptyArgs){
		System.out.println("Create Train Model and Train Controller");
		trainWithControl = new TrainWithController(emptyArgs, this);
		hasTrainModel = true;
	}

	public void CreateTrackModel(String[] emptyArgs){
		System.out.println("Create Track Model");
		trackModel=new TrackModel(emptyArgs, this);
	}
	public void CreateCTC(String[] emptyArgs){
		System.out.println("Create CTC");
		ctc=new CTCcontroller(this);
	}

	// Change this name to sth. more aproperaite
	public void TrainModelNewTrain(int trainId, String name, int carNumber, ArrayList<String> stopNames)
    {
        trainWithControl.newTrain(trainId, name, carNumber, stopNames);
    }
    public void TrainModelDeleteTrain(int trainId)
    {
        trainWithControl.deleteTrain(trainId);
    }

	public void CreateTrain(int trainNum, int length, int direction,int startBlock, String line){
		if(hasTrainModel) {
			trackModel.NewTrain(trainNum, length*2, direction, startBlock, line);
		}else{
			trackModel.NewTrain(trainNum, length*2, direction, startBlock,line, true);
			System.out.println("Creating train: "+trainNum);
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
	public void TrackModelCommandedSpeed(int trainId, double speed){

	}

	public void TrainModelCommandedSpeed(int trainId, double speed)
    {
        trainWithControl.getCommandSpeed(trainId, speed);
    }

    // TrainModel will call this
	public void UpdateTrainDistance(int trainId, float movedDistance){
		//Whoever needs this information, please fill in your parts
	}

	// TrainModel will call this
	public void ServiceBrakeFromTrain(int trainId, Boolean activate)
	{
		//Whoever needs this information, please fill in your parts
    }

    // TrainModel will call this
    public void EmergencyStopFromTrain(int trainId, Boolean activate)
    {
		//Whoever needs this information, please fill in your parts
    }

	public void TrackStateUpdate(int occBlock){

	}
	public void WaysideAddOccupied(int blockNum, String line){
		wayside.AddOccupied(blockNum);
	}
	public void WaysideRemoveOccupied(int blockNum, String line){
		wayside.RemoveOccupied(blockNum);
	}
	public void CTCAuthority(int location, int authority, int trainNum){
		wayside.SuggestAuthority(location, authority, trainNum);
	}
	public void WaysideSendAuthority(ArrayList<Integer> authorityBlocks, ArrayList<Integer> authorityBlocks1, int blockNum, boolean filler){
		if(!hasTrainModel) {
			trackModel.CommandedAuthorityBlock(authorityBlocks, authorityBlocks1, blockNum,true);
		}else{
			trackModel.CommandedAuthorityBlock(authorityBlocks,authorityBlocks1,blockNum);
		}
	}
	public void WaysideSendAuthority(ArrayList<Integer> authorityBlocks, ArrayList<Integer> authorityBlocks1, int trainNum){
		System.out.println("Wayside calling update authority on: "+trainNum);
		if(!hasTrainModel) {
			trackModel.CommandedAuthority(authorityBlocks, authorityBlocks1, trainNum,true);
		}else{
			trackModel.CommandedAuthority(authorityBlocks, authorityBlocks1, trainNum);
		}
	}
	public void SendMultiplier(int multiplier){
		
	}
	public void TrainToYard(int trainNum){
		ctc.killTrain(trainNum);
	}
	public boolean CTCMoveSwitch(int blockNum, boolean state)
	{
		return wayside.SwitchSwitch(blockNum, state);
	}
	public void TrackMoveSwitch(int blockNum, String line)
	{
		trackModel.FlipSwitch(blockNum, line);
	}
	public void TrackSetLight(int blockNum, String line, String color){
		trackModel.SetLight(blockNum, line,color);
	}
	public void CTCAddOccupancy(int blockNum, String line)
	{
		
	}
	public void TrackSetCrossing(int blockNum, String line)
	{
		trackModel.FlipCrossing(blockNum,line);
	}
	public void ReportBeaconData(BitSet data, int trainNum){
		System.out.println("Track Model transmitted: "+data+" to train: "+trainNum);
	}
	public void TrackGenerateTickets(int newTickets,int blockNum, String line){
		System.out.println("Track reported: "+newTickets+" at Block: "+blockNum+" on "+line+" line");
		ctc.ReceiveTickets(newTickets, blockNum, line);
	}
	// Send speed limit To TrainModel
    public void TrainSendSpeedLimit(int trainNum, double speedLimit)
    {
        trainWithControl.getSpeedLimit(trainNum, speedLimit);
    }

    public void TrainSendInYard(int trainNum, Boolean inYard)
	{
		trainWithControl.getInYard(trainNum, inYard);
	}

	// Send authority TO TrainModel
	public void TrainSendAuthority(int trainNum, Double authority) {
	    trainWithControl.getAuthority(trainNum, authority);
    }

    // Send emergency stop info To TrainModel
    public void TrainSendEmergencyStop(int trainId, Boolean activate)
    {
        trainWithControl.emergencyStop(trainId, activate);
    }
    // Send stop info TO train
    // stopAtStation = true =>  arrives at a station after moving distance of authority
    public void TrainStopAtStation(int trainId, Boolean stopAtStation)
    {
        trainWithControl.atStation(trainId, stopAtStation);
    }

	public void Update(int mulitplyer){
		trainWithControl.step();
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