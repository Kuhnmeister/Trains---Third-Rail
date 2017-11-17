package Controller;

import java.util.*;

//Ethan Shuffelbottom
//Third Rail Inc.
//This class exisits to calculate the authoirty given an ArrayList<BlockInfo> and a current block number

public class AuthorityCalculator {
	
	//this function will import the track that the wayside has control over
	//it will not do any calculation at this part
	private ArrayList<BlockInfo> track = null;
	
	public AuthorityCalculator(ArrayList<BlockInfo> importTrack) {
		track = importTrack; //saves on sending in a new track every time
	}
	
	//this method will need to be called everytime a swtich is moved on the track
	public void moveSwitch(int currentBlock) {
		track.get(currentBlock).switchNextBlock();
		
	}
	
	//now to calculate Authority for the current track state
	//this will change each and every time a switch is moved
	//this function returns an integer for the block that the current one has auth to
	public int authToBlock(int currentBlock)
	{
		//need a way to keep track of blocks that have been visited to prevent infinite loop
		//each block that gets visited will have its blockNum marked as true
		//a check will occur to ensure that no looping has happened
		ArrayList<Boolean> visitedBlocks = new ArrayList<Boolean>();
		//mark the first block as true since it will always be visited
		visitedBlocks.add(currentBlock, true);
		
		//the integer is only here as a block number,
		//all of this will be boolean
		int authBlock = currentBlock;
		BlockInfo block = track.get(currentBlock);
		//first check if the next block is occupied
		//if yes, then break and return the authorizedblock as the maximum authority
		//if no, update authBlock to the number of the next block then try again
		
		while(!block.blockAfter.occupy && visitedBlocks.get(authBlock))
		{
			block = block.blockAfter;
			authBlock = block.blockNum;
			visitedBlocks.add(authBlock, true);
		}
		
		//if its free until it loops around
		
		return authBlock;
	}
	
}
