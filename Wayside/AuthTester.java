
import java.util.ArrayList;

//Ethan Shuffelbottom
//This quickly test the AuthorityCalculator class
//This won't use the GUI at all and will only be used in testing

public class AuthTester {
	
	public static void main(String[] args) {
		//call a method to fill an ArrayList of BlockInfo
		
		//testPLC trail = new testPLC();
		ArrayList<BlockInfo> track = createTestTrack();
		
		AuthorityCalculator test = new AuthorityCalculator();
		
		//set a few blocks occupied
		track.get(4).changeOccupancy();
		
		for(int i = 0; i < 11; i++)
		{
			System.out.println("Block number before: " + track.get(i).nextBlock0().blockNumber() + " Block Number: " 
					+ track.get(i).blockNumber() + " Block Number After: " + track.get(i).nextBlock1().blockNumber());
			
			//System.out.println("The Authority from block: " + track.get(i).blockNumber() + " is " + test.getAuth(i, true, track));
		}
		
		System.out.println("The crossing on 5 is: " + track.get(4).crossingOn());
	}
	
	public static ArrayList<BlockInfo> createTestTrack()
	{
		ArrayList<BlockInfo> testTrack = new ArrayList<BlockInfo>();
		BlockInfo currBlock, prevBlock, nextBlock, switchBlock;
		
		/*  this code is for a looped track
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
		*/
		
		//This code is for a straight line, with a switch
		testTrack = new ArrayList<BlockInfo>();
		testTrack.add(new BlockInfo(false, 0, 0 , 1));
		testTrack.add(new BlockInfo(false, 0, 1 , 2));
		testTrack.add(new BlockInfo(false, 1, 2 , 3));
		//now create a loop
		testTrack.add(new BlockInfo(true, true, 2, 3 , 4, 8));
		testTrack.add(new BlockInfo(false, 3, 4 , 5));
		testTrack.add(new BlockInfo(false, 4, 5 , 6));
		testTrack.add(new BlockInfo(false, 5, 6, 7));
		testTrack.add(new BlockInfo(false, 6, 7, 8));
		//8 should only point to block when the switch is high
		testTrack.add(new BlockInfo(false, false, 7, 8, -1, 3));
		
		currBlock = testTrack.get(0);
		//now link the track
		while(currBlock.nextBlock1() == null) {
			prevBlock = testTrack.get(currBlock.blockNumber0());
			nextBlock = testTrack.get(currBlock.blockNumber1());
			System.out.println("linking block: " + currBlock.blockNumber());
			if(currBlock.blockSwitch() == -1) {
				switchBlock = null;
			}else {
				switchBlock = testTrack.get(currBlock.blockSwitch()).nextBlockSwitch();
			}
			
			testTrack.get(currBlock.blockNumber()).setNextBlocks(prevBlock, nextBlock, switchBlock);
			//ensure that there is a next block; -1 means no block there
			if(currBlock.blockNumber1() != -1) 
			{
				currBlock = currBlock.nextBlock1();
			}else if(currBlock.blockSwitch() != -1) {
				System.out.println("Block: " + currBlock.blockNumber() + " has been linked via switch to: " + currBlock.blockSwitch());
				currBlock = currBlock.nextBlockSwitch();
			}
		}
		
		
		return testTrack;
	}

}
