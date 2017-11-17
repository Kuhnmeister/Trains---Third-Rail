package Controller;

//this object will house data for blocks like occupency, Light State, and Switch state
//this will be stored as bool values
//false is Green line, true is red line

public class BlockInfo {
	
	public Boolean occupy, light, switchState, hasSwitch;
	public int blockNum, nextBlock;
	//2 seperate states dependig on where the switch is pointing
	public BlockInfo blockAfterLow = null, blockAfterHigh = null;
	//the current next block
	public BlockInfo blockAfter = null;
	
	public BlockInfo(Boolean occ, Boolean lgt, Boolean swi, Boolean state) {
		//occ is occupied when true and empty when false
		//lgt is green when false, and red when true
		//swi is up when true, swi is down when false and NULL when it doesn't exist
		//has switch is whether or not the block has a switch
		//li is which line the block is on
		occupy = occ;
		light = lgt;
		hasSwitch = swi;
		switchState = state;
	}
	
	public BlockInfo() {
		occupy = false;
		light = false;
		hasSwitch = false;
		switchState = false;
		blockNum = 0;
		nextBlock = 0;
	}
	
	//call this when a switch is switched
	public Boolean switchNextBlock() {
		Boolean worked = false;
		
		BlockInfo block = null;
		//if the switch is currently high when this is called, move it to the low and flipped
		if(switchState == false) {
			block = blockAfterHigh;
		}else { //if the switch is low
			block = blockAfterLow;
		}
		//only allow for switch to move while train is on currblock
		if(!occupy && hasSwitch) {
			nextBlock = block.blockNum;
			blockAfter = block;
			switchState = !switchState;
			worked = true; 
		}
		//else don't move, if both are occupied
		return worked;
	}
}
