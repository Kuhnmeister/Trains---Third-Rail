import java.lang.*;
import java.io.*;
public class Central{
	public static void main(String[] args){
		CTC.main(args);
		TrackModel.main(args);
		XXXX.main(args);
		XXXX.main(args);
	}
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
	
}