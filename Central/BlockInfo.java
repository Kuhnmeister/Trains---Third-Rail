
//Ethan Shuffelbottom
//This class will contain all the information need for wayside operation
//block occupancy, block Light, block crossing, if there is a switch, 
//and what state the switch is in. These will all be Boolean values
//This block will also point to the next blocks, both forward and back
//as well as hold a number for what block they are
//a string for section and track


public class BlockInfo {
	private String section;
	private Boolean occupancy, light, hasCrossing, crossingOn, hasSwitch, switchState;
	private int blockNumber = 0; //the block number
	private int blockNumber0 = 0; //the block being pointed towards on initial departure(0 means yard)
	private int blockNumber1 = 0; //block being pointed towards during return
	private int blockNumberSwitch = -1; //will be -1 for any block without a switch
	//this blocks are same as above
	//but this must be completed after the track is imported as BlockInfo
	private BlockInfo nextBlock0 = null;
	private BlockInfo nextBlock1 = null;
	private BlockInfo nextBlockSwitch = null; //this is the block that is pointed to when the switchState is high
	
	
	//the constructor with more specific fields
	public BlockInfo(Boolean crossing, Boolean switchHere, int createBlockNumber0, int blockNum, int createBlockNumber1, int createBlockNumberSwitch, String createSection)
	{
		//always start as false
		occupancy = false; 
		light = false;
		crossingOn = false;
		switchState = false;
		section = createSection;
		
		//this fields start out differently based on the blocks
		hasCrossing = crossing;
		hasSwitch = switchHere;
		blockNumber = blockNum;
		blockNumber0 = createBlockNumber0;
		blockNumber1 = createBlockNumber1;
		
		blockNumberSwitch = createBlockNumberSwitch;
	}
	
	public BlockInfo(Boolean crossing, Boolean switchHere, int createBlockNumber0, int blockNum, int createBlockNumber1, int createBlockNumberSwitch)
	{
		//always start as false
		occupancy = false; 
		light = false;
		crossingOn = false;
		switchState = false;
		section = "A";
		
		//this fields start out differently based on the blocks
		hasCrossing = crossing;
		hasSwitch = switchHere;
		blockNumber = blockNum;
		blockNumber0 = createBlockNumber0;
		blockNumber1 = createBlockNumber1;
		
		blockNumberSwitch = createBlockNumberSwitch;
	}
	
	//the switchless constructor
	public BlockInfo(Boolean crossing, int createBlockNumber0, int createBlockNumber, int createBlockNumber1)
	{
		//always start as false
		occupancy = false; 
		light = false;
		crossingOn = false;
		switchState = false;
		section = "A";
		
		//this fields start out differently based on the blocks
		hasCrossing = crossing;
		hasSwitch = false;
		
		blockNumber0 = createBlockNumber0;
		blockNumber = createBlockNumber;
		blockNumber1 = createBlockNumber1;
		
		blockNumberSwitch = -1;
	}
	
	public BlockInfo(int createBlockNumber0, int createBlockNumber, int createBlockNumber1)
	{
		//always start as false
		occupancy = false; 
		light = false;
		crossingOn = false;
		switchState = false;
		section = "A";
		
		//this fields start out differently based on the blocks
		hasCrossing = false;
		hasSwitch = false;
		
		blockNumber0 = createBlockNumber0;
		blockNumber = createBlockNumber;
		blockNumber1 = createBlockNumber1;
		
		blockNumberSwitch = -1;
	}
	
	//this function sets the next blocks; to be used after the track is made
	//it should be called will blockSwitch = null for no switch on block
	public void setNextBlocks(BlockInfo block0, BlockInfo block1, BlockInfo blockSwitch) {
		nextBlock0 = block0; //for the block before
		nextBlock1 = block1; //for block after
		
		nextBlockSwitch = blockSwitch;
	}
	
	public void setOccupancy(boolean newOcc)
	{
		occupancy = newOcc;
		if(occupancy) {
			light = true;
			//the crossing should be on before the train enters the block
			//this is just an extra precaution
			if(hasCrossing) {
				crossingOn = true;
			}
		}
	}
	
	//a method to mark/unmark a block as occupied
	public void changeOccupancy()
	{
		//set occupancy to not(occupancy)
		occupancy = !(occupancy);
		//the block is now occupied
		if(occupancy) {
			light = true;
			//the crossing should be on before the train enters the block
			//this is just an extra precaution
			if(hasCrossing) {
				crossingOn = true;
			}
		}
	}
	
	//method to move the switch
	public boolean moveSwitch()
	{
		boolean worked = false;
		//the only possible state that will allow a switch to move: free and has switch
		if(!occupancy && hasSwitch) {
			switchState = !(switchState);
			//check that the switch was moved
			worked = true;
		}
		
		return worked;
	}
	
	//method to change the lights based on occupancy
	public void setLight() 
	{
		light = occupancy;
	}
	
	public void setLight(Boolean lightState)
	{
		//lights can never be turned off from an occupied block
		if(!occupancy) {
			light = lightState;
		} else {
			light = occupancy;
		}
	}
	
	//method to turn on/off crossing
	public void setCrossing(Boolean crossState)
	{
		//enables or disable crossing; only able to turn the crossing off while not occupied
		if(!occupancy && hasCrossing) {
			crossingOn = crossState;
		}
	}
	
	public void setSection(String newSection) {
		section = newSection;
	}
	
	//the following are all accessor methods
	public Boolean occupancy()
	{
		return occupancy;
	}
	
	public Boolean hasSwitch()
	{
		return hasSwitch;
	}
	
	public Boolean light()
	{
		return light;
	}
	
	public Boolean switchState()
	{
		return switchState;
	}
	
	public Boolean hasCrossing() 
	{
		return hasCrossing;
	}
	
	public Boolean crossingOn()
	{
		return crossingOn;
	}
	
	public int blockNumber()
	{
		return blockNumber;
	}
	
	public int blockNumber0()
	{
		return blockNumber0;
	}
	
	public int blockNumber1()
	{
		return blockNumber1;
	}
	
	public int blockSwitch()
	{
		return blockNumberSwitch;
	}
	
	public BlockInfo nextBlock0()
	{
		return nextBlock0;
	}
	
	public BlockInfo nextBlock1()
	{
		return nextBlock1;
	}
	
	public BlockInfo nextBlockSwitch()
	{
		return nextBlockSwitch;
	}
	
	public String section()
	{
		return section;
	}
	
}
