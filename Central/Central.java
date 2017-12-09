import java.lang.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.BitSet;

public class Central{
	private TrackModel trackModel;
	private Wayside wayside;
	private CTCcontroller ctc;
	private CentralGui centralGui;
	private TrainPool trainPool;
	private TrainControllerUI trainControllerUi;
	private TrainModelUI trainModelGui;
	private boolean hasTrainModel=true;
	private String[] args;
	public static void main(String[] args){
		Central thisCentral = new Central(args);
	}
	public Central(String[] Args){
		trainPool = new TrainPool(this);
		centralGui = new CentralGui(Args,this);
		args=Args;
	}
	public void TestMethod(String testString){
		System.out.println(testString);
	}


	public void CreateTrackModel(String[] emptyArgs){
		System.out.println("Create Track Model");
		trackModel=new TrackModel(emptyArgs, this);
	}
	public void CreateCTC(String[] emptyArgs){
		System.out.println("Create CTC");
		ctc=new CTCcontroller(this);
	}

	public void CreateTrainModel(String[] emptyArgs)
    {
    	hasTrainModel = true;
		trainModelGui = TrainModelUI.CreateTrainModelUI(emptyArgs);
		trainModelGui.linkToTrainPool(trainPool);
    }

	public void CreateTrainController(String[] emptyArgs)
	{
		trainControllerUi = TrainControllerUI.CreateTrainControllerUI(emptyArgs);
		trainControllerUi.linkToTrainPool(trainPool);
	}

	public Boolean TrainModelNewTrain(int trainNum)
	{
		return trainPool.createNewTrain(trainNum);
	}

	public void CreateTrain(int trainNum, int length, int direction,int startBlock, String line){
		System.out.println("Central trying to create train: "+trainNum);
		if(hasTrainModel) {
			Boolean ret = TrainModelNewTrain(trainNum);
			System.out.println("Train Pool created train: "+ret);
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

	public void TrainModelCommandedSpeed(int trainId, double speed) {
		trainPool.setCommandSpeed(trainId, speed);
	}

	//in miles
	public void UpdateTrainAuthority(int trainId, double authority)
	{
		trainPool.setAuthority(trainId, authority);
	}

    // TrainModel will call this
	public void UpdateTrainDistance(int trainId, double movedDistance){
		trackModel.TrainModelUpdatePosition(trainId,movedDistance);
	}

	

	public void TrackStateUpdate(int occBlock){

	}

	//TrainModel will call this
	public void UpdatePassegnerFromTrain(int trainId, int getOffNum, int numRemain)
	{

	}

	public void SendInTunnelToTrain(int trainId, Boolean inTunnel)
	{
		trainPool.inTunnel(trainId, inTunnel);
	}

	//call your train method to receive the grade
	public void TrackGrade(int trainNum, double newGrade){
		trainPool.setGrade(trainNum, newGrade);
	}
	public void WaysideAddOccupied(int blockNum, String line){
		System.out.println("Block num: "+blockNum);
		if(wayside==null) {
			System.out.println("wayside is null");
		}else{
			wayside.AddOccupied(blockNum);
		}
	}
	public void WaysideRemoveOccupied(int blockNum, String line){
		wayside.RemoveOccupied(blockNum);
	}
	public void CTCAuthority(int location, int authority, int trainNum){
		System.out.println(authority);
		wayside.SuggestAuthority(location, authority, trainNum);
	}
	public void WaysideSendAuthority(ArrayList<Integer> authorityBlocks, ArrayList<Integer> authorityBlocks1, int blockNum, String line, boolean filler){
		if(!hasTrainModel) {
			trackModel.CommandedAuthorityBlock(authorityBlocks, authorityBlocks1, blockNum,line,true);
		}else{
			trackModel.CommandedAuthorityBlock(authorityBlocks,authorityBlocks1,blockNum,line);
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
		trackModel.SetExecutionMultiplier(multiplier);
	}
	public void TrainToYard(int trainNum){
		ctc.killTrain(trainNum);
		trainPool.removeTrain(trainNum);
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
		ctc.ReceiveOccupancy(blockNum, line);
	}

	public void TrackSetCrossing(int blockNum, String line)
	{
		trackModel.FlipCrossing(blockNum,line);
	}
	public void ReportBeaconData(BitSet data, int trainNum){
		System.out.println("Track Model transmitted: "+data+" to train: "+trainNum);
		trainPool.setBeaconData(trainNum, data);
	}
	public void TrackGenerateTickets(int newTickets,int blockNum, String line){
		System.out.println("Track reported: "+newTickets+" at Block: "+blockNum+" on "+line+" line");
		ctc.ReceiveTickets(newTickets, blockNum, line);
	}



	public void Update(int mulitplyer){
		trainPool.Step();
	}



	public void SetMaintainanceToTrackModel(int blockNum, String line){
		trackModel.SetMaintainance(blockNum,line);
	}
	public void SetMaintenanceToWayside(int blockNum, String line){
		wayside.SetMaintainance(blockNum, line);
	}

	
}