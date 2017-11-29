//Ethan shuffelbottom
//migrate(d) all no GUI related functionality from WaysideController
//to this Wayside. This was done for integration 
//this is the handle that he central will use to controll everything

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javafx.stage.Stage;

public class Wayside {

	public ArrayList<BlockInfo> track;
	public ArrayList<ArrayList<BlockInfo>> lines;
	public ArrayList<String> trackSections = new ArrayList<String>();
	public ArrayList<ArrayList<String>> blockSections = new ArrayList<ArrayList<String>>();
	public String[] lineNames;
	public int currentBlock = 0;
	public ArrayList<Integer> currentAuth;
	public ArrayList<Integer> occBlocks = new ArrayList<Integer>();
	public double suggSpeed = 55;
	public int suggBlock = 0;
	public BlockInfo block = null;
	public String selectedBlock, choosenPLC, selectedLine; 
	public AuthorityCalculator authCalc = new AuthorityCalculator();
	public Object PLC;
	private Central central;
	///if a PLC has been succesfully loaded, us it for auth and for crossing
	public boolean PLCloaded = false;
	
	private WaysideController theGui;
	
	//constructor for being called from cmd/jar
	public Wayside(String[] args){
		//create the track
		//getTrack();
		//getSections();
		track = getTrack();
		block = track.get(0);
       theGui = new WaysideController(args, this);
    }
	
	//constructor for being called from anything else
	public Wayside(String[] args, Central cen, HashMap<String,HashMap<String,ArrayList<Block>>> newTrack){
		central = cen;
		//getSections();
		track = GetTrack(newTrack);
		block = track.get(0);
		theGui = new WaysideController(args, this, true); //launchless GUI
		try {
			theGui.start(new Stage());
		} catch (Exception e) {
		}
	}
	
	public static void main(String[] args) {
		//TrackModel thisModel = new TrackModel(args);
		Wayside thisWayside = new Wayside(args);
	}
	
	//called from GUI in order to change the currentTrack
	public void changeTrack()
	{
		
	}
	
	//method for getting the Authority
	public ArrayList<Integer> getAuthority(int blockNow, boolean direction) {
		ArrayList<Integer> auth;
		System.out.println("calling the authCalc");
		try {
		//use PLC
		if(PLCloaded)
		{
			auth = ((PLCinterface)PLC).getAuth(blockNow, direction, track);
		}
		else {
			auth = authCalc.getAuth(blockNow, direction, track);
		}
		//TODO check Authority gotten from these methods
		} catch(Exception e) {
			//if there is any error in calculating authoirty, set authority to currentBlock
			auth = new ArrayList<Integer>();
			auth.add((Integer) blockNow);
		}
		return auth;
	}
	
	public ArrayList<BlockInfo> getTrack(){
		return track;
	}
	
	//A hard coded track used for testing purposes only
	//replace this with getTrack from trackModel
	public ArrayList<BlockInfo> getTrack(boolean t) {
		ArrayList<BlockInfo> testTrack = new ArrayList<BlockInfo>();
		BlockInfo currBlock, prevBlock, nextBlock, switchBlock;
		//the first block will be the yard
		testTrack.add(new BlockInfo(false, 0 , 0 , 1));
		
		for(int i = 1; i < 11; i++)
		{
			if(i < 10) {
				if(i != 5 && i != 1) {
					testTrack.add(new BlockInfo(i - 1, i, i + 1));
				}else if(i == 5){
					testTrack.add(new BlockInfo(true, i - 1, i, i + 1)); //create a block with a crossing & switch @5
				}else {
					testTrack.add(new BlockInfo(true, i - 1, i, i + 1)); 
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
				switchBlock = testTrack.get(currBlock.blockSwitch());
			}
			//System.out.println("linking blocks");	
			testTrack.get(currBlock.blockNumber()).setNextBlocks(prevBlock, nextBlock, switchBlock);
			currBlock = currBlock.nextBlock1();

		}
		
		//looped track; add in all the fun bits here
		testTrack.get(8).setSection("B");
		testTrack.get(9).setSection("C");
		testTrack.get(10).setSection("D");
			
		return testTrack;
	}
	
	public Boolean getCrossing(int currentBlock) {
		boolean crossing = true;
		if(PLCloaded)
		{
			crossing = ((PLCinterface)PLC).decideCrossing(currentBlock, track);
		}else {
			crossing = authCalc.decideCrossing(currentBlock, track);
		}
		return crossing;
	}
	
	public Object getPLC() {
		Object PLCobject = new AuthorityCalculator();
		try {
			PLCobject = Class.forName(choosenPLC).newInstance();
			//now test that the PLC works(testPLC in package is Controller.testPLC)
			System.out.println(((PLCinterface)PLCobject).getAuth(currentBlock, true, track));
			System.out.println(((PLCinterface)PLCobject).decideCrossing(currentBlock, track));
			PLCloaded = true;
		}catch(Exception e) {
			System.out.println("The PLC has failed to load");
			System.out.println(e);
			PLCobject = new AuthorityCalculator();
			PLCloaded = false;
		}

		return PLCobject;
	}
	
	//leave this GUI side for use of propagation
		public void getSections()
		{
			String currentSection = track.get(0).section();
			trackSections.add(currentSection);
			int sectionNumber = 0;
			blockSections.add(new ArrayList<String>());
			
			for(int i = 0; i < track.size(); i++)
			{
				if(track.get(i).section().equals(currentSection)) {
					//add the block number to the section of track
					blockSections.get(sectionNumber).add(Integer.toString(i));
				}else {
					//create a new section
					sectionNumber++;
					currentSection = track.get(i).section();
					trackSections.add(currentSection);
					blockSections.add(new ArrayList<String>());
					blockSections.get(sectionNumber).add(Integer.toString(i));
				}
			}
		}
	
	public ArrayList<BlockInfo> GetTrack( HashMap<String,HashMap<String,ArrayList<Block>>> newTrack)
	{
		String[] keyArray = newTrack.keySet().toArray(new String[newTrack.keySet().size()]);
		lineNames = keyArray;
		try {
		Block currentBlock;
		BlockInfo newBlockInfo, prevBlock, nextBlock, currBlock, switchBlock;
		int numberOfBlocks = 1;
		int numberOfLines = 0;
		//create the lines object, to hold each line
		lines = new ArrayList<ArrayList<BlockInfo>>();
		//create one track object large enough to hold every block on the track
		for (HashMap.Entry<String, HashMap<String, ArrayList<Block>>> line : newTrack.entrySet()) {
			track = new ArrayList<BlockInfo>();
			track.add(0, new BlockInfo(0, 0, 0));
			track.get(0).setNextBlocks(track.get(0), track.get(0), track.get(0));
			for (HashMap.Entry<String, ArrayList<Block>> sectionCount : line.getValue().entrySet()) {
                for (int i = 0; i < sectionCount.getValue().size(); i++) {
                	numberOfBlocks++;
                	track.add(new BlockInfo(0, 0, 0));
                }
			}
			lines.add(track);
		}
		
		//start on the first line
		track = lines.get(numberOfLines);
		for (HashMap.Entry<String, HashMap<String, ArrayList<Block>>> line : newTrack.entrySet()) {
			//get the total number of blocks in this line
			System.out.println(numberOfBlocks);
			
            for (HashMap.Entry<String, ArrayList<Block>> section : line.getValue().entrySet()) {
                for (int i = 0; i < section.getValue().size(); i++) {
                	//this is a block in the form of Block
                	currentBlock = section.getValue().get(i); 
                	//public BlockInfo(Boolean crossing, Boolean switchHere, int createBlockNumber0, int blockNumber, int createBlockNumber1, int createBlockNumberSwitch)
                	System.out.println(currentBlock.GetDirection1Num() + " " + currentBlock.GetBlockNum() + " " + currentBlock.GetDirection0Num());
                	
                	newBlockInfo = new BlockInfo(currentBlock.GetHasRailwayCrossing(), currentBlock.GetHasSwitch(), currentBlock.GetDirection1Num(), currentBlock.GetBlockNum(),
                			currentBlock.GetDirection0Num(), currentBlock.GetSwitchNum(), currentBlock.GetSection());
                	track.set(currentBlock.GetBlockNum(), newBlockInfo);
                }
                for(BlockInfo cBlock : track)
        		{
        			System.out.println(cBlock.blockNumber0() + " " + cBlock.blockNumber() + " " + cBlock.blockNumber1());
        		}
        		
        		 for(BlockInfo cBlock : track)
                 {
                 	//System.out.println(cBlock.blockNumber());
                 	if(cBlock.blockNumber0() != -1) {
                 		//System.out.println(cBlock.blockNumber0());
                 		prevBlock = track.get(cBlock.blockNumber0());
                 	}else {
                 		prevBlock = track.get(0);
                 	}
                 	if(cBlock.blockNumber1() != -1) {
                 		//System.out.println(cBlock.blockNumber1());
                 		nextBlock = track.get(cBlock.blockNumber1());
                 	} else {
                 		nextBlock = track.get(0);
                 	}
         			if(cBlock.blockSwitch() == -1) {
         				//System.out.println(cBlock.blockSwitch());
         				switchBlock = track.get(0);
         			}else {
         				switchBlock = track.get(cBlock.blockSwitch());
         			}
         			//System.out.println("linking blocks");	
         			System.out.println("Prev Block: " + prevBlock.blockNumber() + " Next Block: " + nextBlock.blockNumber() + " Switch Block: " + switchBlock.blockNumber());
         			track.get(cBlock.blockNumber()).setNextBlocks(prevBlock, nextBlock, switchBlock);
            }
            } 
            //change the track to the next line
            numberOfLines++;
            track = lines.get(numberOfLines);
         }
		
		System.out.println("It worked!");
		//set initial track to green line
		
		}catch(Exception e) {
			//something wasn't loaded/read correctly
			System.out.println(e);
		}
		
		return track;
        }
		
	//called from controller method to update track state
	public void AddOccupied(int occBlock)
	{
		if(occBlocks.contains((Integer) occBlock))
		{
			//do not add, already in list
		}else {
			//add to occupied list
			occBlocks.add((Integer) occBlock);
			//set the block in the trck object as occupied
			track.get(occBlock).setOccupancy(true);
			//call Central to inform CTC
			central.TrackStateUpdate(occBlock);
		}
		System.out.println(occBlocks.toString());
	}
	
	//get newly freed block from track
	public void RemoveOccupied(int freedBlock)
	{
		if(occBlocks.contains((Integer) freedBlock))
		{
			//add to occupied list
			occBlocks.remove((Integer) freedBlock);
			//set the block in the trck object as occupied
			track.get(freedBlock).setOccupancy(false);
			//call Central to inform CTC
		}else {
			
		}
		System.out.println(occBlocks.toString());
	}
	
	//this method creates a track from the track models object
	public void createTrack()
	{
		
	}
	
	//get speed from central 
	public void SuggestSpeed(int blockNum,int trainNum, double speed)
	{
		//pass this like a hot potato
		//central.suggestSpeed(blockNum, speed);
		System.out.println("Received Suggested Speed for train: "+trainNum+": BlockNum: "+blockNum+" speed: "+speed);
		central.WaysideCommandedSpeed(trainNum,speed);
	}
	
	//get authority from central
	public void SuggestAuthority(int blockNum, int suggestedAuth, int trainNum)
	{
		currentBlock = blockNum;
		currentAuth = getAuthority(currentBlock, true);
		 if(currentAuth.contains(suggestedAuth)) {
			//safe authority
			int index = currentAuth.indexOf(suggestedAuth);
			currentAuth = new ArrayList<Integer> (currentAuth.subList(0, index));
		 }
		 //call central to pass to track
		 
		 central.WaysideSendAuthority(currentAuth, trainNum);
	}
	
	public boolean SwitchSwitch(int blockNum, boolean state) 
	{
		//move switch in BlockInfo with this method
		if(track.get(blockNum).setSwitch(state))
		{
			central.TrackMoveSwitch(blockNum, lineNames[0]);
		}
		return track.get(blockNum).switchState();
	}
}
