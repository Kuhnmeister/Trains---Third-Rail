
import java.util.*;

//Ethan Shuffelbottom
//Third Rail Inc.
//This class exists to calculate the authority given an ArrayList<BlockInfo> and a current block number

public class AuthorityCalculator implements PLCinterface{
	
	//only one can exist per track
	//track meaning any number of blocks
	
	//an arraylist of all the blocks that are safe to move on
	private ArrayList<Integer>  freeBlocks;
	
	public AuthorityCalculator() {
		//null constructor
	}
		
	//this method will return the maximum safe Authority for a train(aka doesn't let it run into anything)
	//used when no PLC is uploaded
	public ArrayList<Integer> getAuth(int currentBlock, boolean direction, ArrayList<BlockInfo> track)
	{
		//the current block will always be the minimum authority; i.e. can't move forward or backwards
		//this will also be safe
		int authority = currentBlock; 
		//array list of free blocks
		freeBlocks = new ArrayList<Integer>();
		freeBlocks.add(authority);
		//right now just check the next 10 blocks ahead and behind
		int i = 10;
		System.out.println("get Auth has been called");
		//direction determines advancing/returning
		if(direction) {
			//compare the blocks to make sure they aren't the same(if they're the same the maximum has been reached)
			while(canAdvance(track.get(authority)) != authority && i >= 0) {
				//System.out.println("Advancing to: " + i);
				authority = canAdvance(track.get(authority));
				freeBlocks.add(authority);
				i--;
			}
		}else {
			//compare the blocks to make sure they aren't the same(if they're the same the maximum has been reached)
			while(canReturn(track.get(authority)) != authority && i >= 0) {
				//System.out.println("Advancing to: " + i);
				authority = canReturn(track.get(authority));
				freeBlocks.add(authority);
				i--;
			}
		}
		System.out.println(freeBlocks.toString());
		return freeBlocks;
	}
	
	//canAdvance will return that is is safe to move forward from current block
	private int canAdvance(BlockInfo currentBlock) 
	{
		BlockInfo safeAdvance = currentBlock;
		//check for switch
		if(safeAdvance.switchState()) {
			if(safeAdvance.nextBlockSwitch().occupancy())
			{
				//do nothing the auth is just the number of the currentBlock
			}else {
				safeAdvance = safeAdvance.nextBlockSwitch();
			}
		}else {
			//System.out.println("testing auth on no switch");
			if(safeAdvance.nextBlock1() != null) 
			{
				if(!(safeAdvance.nextBlock1().occupancy()))
				{
					safeAdvance = safeAdvance.nextBlock1();
				}
			}
		}
		
		return safeAdvance.blockNumber();
	}
	
	//canReturn will return that it is safe to move backwards from current block
	private int canReturn(BlockInfo currentBlock)
	{
		BlockInfo safeReturn = currentBlock;
		//check for switch
		if(safeReturn.switchState()) {
			if(safeReturn.nextBlockSwitch().occupancy())
			{
				//do nothing the auth is just the number of the currentBlock
			}else {
				safeReturn = safeReturn.nextBlockSwitch();
			}
		}else {
			if(safeReturn.nextBlock0() != null)
			{
				if(!safeReturn.nextBlock0().occupancy())
				{
					safeReturn = safeReturn.nextBlock0();
				}
			}
		}
		
		return safeReturn.blockNumber();
	}
	
	//this method will look for any crossing in the next block and turn it on.
	public boolean decideCrossing(int currentBlock, ArrayList<BlockInfo> track)
	{
		int nextBlock = currentBlock;
		boolean turnOnCrossing = true; //Defaulted to true in case something goes wrong with calculation
		for(int i = 0; i < 2; i++)
		{
			//the if statement is a check that the train isn't branching off to any path
			if(track.get(nextBlock).hasCrossing() && (!(track.get(nextBlock).hasSwitch()) || track.get(nextBlock).switchState() == false))
			{
				turnOnCrossing = true;
				track.get(nextBlock).setCrossing(true);
				break;
			}
			if(track.get(nextBlock).hasSwitch() && (track.get(nextBlock).switchState() == true))
			{
				nextBlock = track.get(nextBlock).blockSwitch();
			}else if(track.get(nextBlock) != null){
				nextBlock = track.get(nextBlock).blockNumber1();
			}
			turnOnCrossing = false; //if no crossing is found, no need to turn it on
		}
		
		//returns true if crossing should be on or false if it doesn't need to be
		return turnOnCrossing;
	}
	
}
