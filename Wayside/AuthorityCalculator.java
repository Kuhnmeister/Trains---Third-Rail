package Controller;

import java.util.*;

//Ethan Shuffelbottom
//Third Rail Inc.
//This class exists to calculate the authority given an ArrayList<BlockInfo> and a current block number

public class AuthorityCalculator {
	
	private ArrayList<BlockInfo> baseTrack = null;
	
	//only one can exist per track
	//track means a segment of blocks
	public AuthorityCalculator(ArrayList<BlockInfo> importTrack) {
		baseTrack = importTrack; //saves on sending in a new track every time
	}
		
	//this method will return the maximum safe Authority for a train(aka doesn't let it run into anything)
	//used when no PLC is uploaded
	public int getAuth(int currentBlock, boolean direction, ArrayList<BlockInfo> track)
	{
		//the current block will always be the minimum authority; i.e. can't move forward or backwards
		//this will also be safe
		int authority = currentBlock; 
		//direction determines advancing/returning
		if(direction) {
			//compare the blocks to make sure they aren't the same(if they're the same the maximum has been reached)
			while(canAdvance(track.get(authority)) != authority) {
				authority = canAdvance(track.get(authority));
			}
		}else {
			//compare the blocks to make sure they aren't the same(if they're the same the maximum has been reached)
			while(canReturn(track.get(authority)) != authority) {
				authority = canReturn(track.get(authority));
			}
		}
		return authority;
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
			if(!(safeAdvance.nextBlock1().occupancy()))
			{
				safeAdvance = safeAdvance.nextBlock1();
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
			if(!safeReturn.nextBlock0().occupancy())
			{
				safeReturn = safeReturn.nextBlock0();
			}
		}
		
		return safeReturn.blockNumber();
	}
	
	//this method will look for any crossing in the next block and turn it on.
	public boolean decideCrossing(int currentBlock, ArrayList<BlockInfo> track)
	{
		int nextBlock = currentBlock;
		boolean turnOnCrossing = true; //Defaulted to true incase something goes wrong with calculation
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
			}else {
				nextBlock = track.get(nextBlock).blockNumber1();
			}
			turnOnCrossing = false; //if no crossing is found, no need to turn it on
		}
		
		//returns true if crossing should be on or false if it doesn't need to be
		return turnOnCrossing;
	}
	
}
