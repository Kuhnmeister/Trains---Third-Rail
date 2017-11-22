package Controller;

import java.util.ArrayList;

//Ethan Shuffelbottom
//This quickly test the AuthorityCalculator class
//This won't use the GUI at all and will only be used in testing

public class AuthTester {
	
	public static void main(String[] args) {
		//call a method to fill an ArrayList of BlockInfo
		ArrayList<BlockInfo> track = createTestTrack();
		
		AuthorityCalculator test = new AuthorityCalculator(track);
		
		//set a few blocks occupied
		track.get(4).changeOccupancy();
		track.get(9).changeOccupancy();
		
		for(int i = 0; i < 11; i++)
		{
			System.out.println("Block number before: " + track.get(i).nextBlock0().blockNumber() + " Block Number: " 
					+ track.get(i).blockNumber() + " Block Number After: " + track.get(i).nextBlock1().blockNumber());
			
			System.out.println("The Authority from block: " + track.get(i).blockNumber() + " is " + test.getAuth(i, true, track));
		}
		
		System.out.println("The crossing on 5 is: " + track.get(4).crossingOn());
	}
	
	public static ArrayList<BlockInfo> createTestTrack()
	{
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

}
