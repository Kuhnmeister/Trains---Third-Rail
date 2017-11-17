package Controller;

import java.util.ArrayList;

//Ethan Shuffelbottom
//This quickly test the AuthorityCalculator class
//This won't use the GUI at all and will only be used in testing

public class AuthTester {
	
	public static void main(String[] args) {
		
	} 
	
	public ArrayList<BlockInfo> TrackCreater(){
		//this object will hold the created track
		ArrayList<BlockInfo> testTrack = new ArrayList<BlockInfo>();
		for(int i = 0; i < 10; i++) {
			//create 10 unlinked BlockInfos
			testTrack.add(new BlockInfo(false, false, false, false));
		}
		for(int i = 0; i < 10; i++) {
			//Link the BlockInfos present in the track
			testTrack.get(i).blockNum = i;
		}
		
		return testTrack;
	}

}
