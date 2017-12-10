//Ethan shuffelbottom
//migrate(d) all no GUI related functionality from WaysideController
//to this Wayside. This was done for integration 

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javafx.stage.Stage;

public class Wayside {
	//these used to all be set to package, but it wouldn't work
	//with the other modules, so they are set to public
	//depending on time contraints they may all get getters, and setters.
	public boolean integated = false;
	public ArrayList<BlockInfo> track;
	public ArrayList<BlockInfo> track2;
	//track that is currently being altered by the GUI
	public ArrayList<BlockInfo> currentTrack;
	public ArrayList<ArrayList<BlockInfo>> lines;
	public ArrayList<String> trackSections = new ArrayList<String>();
	public ArrayList<ArrayList<String>> blockSections = new ArrayList<ArrayList<String>>();
	public String[] lineNames;
	public int currentBlock = 0;
	public ArrayList<Integer> currentAuth;
	public ArrayList<Integer> currentAuth1;
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
		track = getTrack(true);
		block = track.get(0);
       theGui = new WaysideController(args, this);
    }
	
	//constructor for being called from anything else
	public Wayside(String[] args, Central cen, HashMap<String,HashMap<String,ArrayList<Block>>> newTrack){
		central = cen;
		integated = true;
		//find all the keys for the track
		String[] keyArray = newTrack.keySet().toArray(new String[newTrack.keySet().size()]);
		lineNames = keyArray;
		track = GetTrack(newTrack, lineNames[0]);
		System.out.println(lineNames[0]);
		//track2 = GetTrack(newTrack, lineNames[1]);
		block = track.get(9);
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
		System.out.println("Given Authority is: " + auth.toString());
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
		String[] lNames = {"Green", "Red"};
		lineNames = lNames;
		for(int i = 1; i < 20; i++)
		{
			if(i < 19) {
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
	
	public ArrayList<BlockInfo> GetTrack( HashMap<String,HashMap<String,ArrayList<Block>>> newTrack, String keyForLine)
	{
		try {
		Block currentBlock;
		BlockInfo newBlockInfo, prevBlock, nextBlock, currBlock, switchBlock;
		int numberOfBlocks = 1;
		int numberOfLines = 0;
		//create the lines object, to hold each line
		lines = new ArrayList<ArrayList<BlockInfo>>();
		//create one track object large enough to hold every block on the track
		track = new ArrayList<BlockInfo>();
		track.add(0, new BlockInfo(0, 0, 0));
		track.get(0).setNextBlocks(track.get(0), track.get(0), track.get(0));
		
		HashMap<String,ArrayList<Block>> line = newTrack.get(keyForLine);
		
		for (HashMap.Entry<String, ArrayList<Block>> sectionCount : line.entrySet()) {
			for (int i = 0; i < sectionCount.getValue().size(); i++) {
				numberOfBlocks++;
				track.add(new BlockInfo(0, 0, 0));
			}
		}
			//get the total number of blocks in this line
			System.out.println(numberOfBlocks);	
            for (HashMap.Entry<String, ArrayList<Block>> section : line.entrySet()) {
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
        			//System.out.println(cBlock.blockNumber0() + " " + cBlock.blockNumber() + " " + cBlock.blockNumber1());
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
		
		System.out.println("It worked!");
		//set initial track to green line
		
		}catch(Exception e) {
			//something wasn't loaded/read correctly
			System.out.println("There was an error with loading the track, please try a different one");
			System.out.println(e);
		}
		
		return track;
        }
		
	//called from controller method to update track state
	//TODO handle lines
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
			//iff called from central
			if(integated) {
				central.TrackStateUpdate(occBlock);
				central.CTCAddOccupancy(occBlock, lineNames[0]);
			}
			this.SetLights(occBlock, lineNames[0]);
			this.SetCrossing(occBlock, lineNames[0]);
			//now call for an authority and send to to the track
			currentAuth = getAuthority(occBlock, true);
			ArrayList<Integer> currentAuthTest = getAuthority(occBlock, true);
			currentAuth1 = getAuthority(occBlock, false);
			ArrayList<Integer> currentAuth1Test = getAuthority(occBlock, false);
			
			//test to ensure they are equal
			if(!(VoterAuthority(currentAuth, currentAuthTest) && VoterAuthority(currentAuth1, currentAuth1Test)))
			{
				//only entered if the two arrays do not match
				//makes a new arrayList of only the current Block
				currentAuth = new ArrayList<Integer> ();
				currentAuth.add((Integer) occBlock);
				currentAuth1 = new ArrayList<Integer> ();
				currentAuth1.add((Integer) occBlock);
			}
		
			
			//pass these to the central to be sent to the train
			if(integated) {
				central.WaysideSendAuthority(currentAuth, currentAuth1, occBlock, lineNames[0], true);
				central.CTCAddOccupancy(occBlock, lineNames[0]);
			}
		}
		//print to show update
		System.out.println(occBlocks.toString());
		
	}
	
	//get newly freed block from track
	public void RemoveOccupied(int freedBlock)
	{
		if(occBlocks.contains((Integer) freedBlock))
		{
			//call Central to inform CTC of occupancy
			//call method to set lights
			this.SetLights(freedBlock, lineNames[0]);
			this.SetCrossing(freedBlock, lineNames[0]);
			//handle automatic switching
			if(track.get(freedBlock).hasSwitch()){
				///unlock to enable moving the switch
				track.get(freedBlock).setLock(false);
				//move and lock a switch in the wayside and send moved switch to TrackModel
				//hard coded for red and green track
				if(lineNames[0].equals("Red") && freedBlock != 9)
				{
					track.get(freedBlock).moveSwitch();
					//locks when the switch is first gone over and unlocked after train returns over it
					track.get(freedBlock).changeLock();
					System.out.println("switch on: " + freedBlock + " has been set to: " + track.get(freedBlock).switchState());
				}else if(lineNames[0].equals("Green") && freedBlock != 58 && freedBlock != 62)
				{
					track.get(freedBlock).moveSwitch();
					//locks when the switch is first gone over and unlocked after train returns over it
					track.get(freedBlock).changeLock();
					System.out.println("switch on: " + freedBlock + " has been set to: " + track.get(freedBlock).switchState());
				}
			}
			//remove from occupied list
			occBlocks.remove((Integer) freedBlock);
			//set the block in the trck object as occupied
			track.get(freedBlock).setOccupancy(false);
		
		}else {
			
		}
		System.out.println(occBlocks.toString());
	}
	
	//a method to pass the light state to the track
	private void SetLights(int block, String Line)
	{
		//true == Red, false == Green
		//public void TrackSetLight(int blockNum, String line, String color)
		if(track.get(block).light()) {
			if(integated) {
				central.TrackSetLight(block, Line, "Red");
			}
		}else {
			if(integated) {
				central.TrackSetLight(block, Line, "Green");
			}
		}
	}
	
	//calls central to move a crossing
	public void SetCrossing(int blockNum, String Line)
	{
		if(integated) {
			central.TrackSetCrossing(blockNum, Line);
		}
	}
	
	//get speed from central 
	public void SuggestSpeed(int blockNum,int trainNum, double speed)
	{
		//pass this like a hot potato
		System.out.println("Received Suggested Speed for train: "+trainNum+": BlockNum: "+blockNum+" speed: "+speed);
		if(integated) {
			central.WaysideCommandedSpeed(trainNum,speed);
		}
	}
	
	//get authority from central
	public void SuggestAuthority(int blockNum, int suggestedAuth, int trainNum)
	{
		currentBlock = blockNum;
		System.out.println("The block is: " + blockNum + " For train: " + trainNum + " on  Block: " + blockNum);
		currentAuth = getAuthority(blockNum, true);
		ArrayList<Integer> currentAuthTest = getAuthority(blockNum, true);
		currentAuth1 = getAuthority(blockNum, false);
		ArrayList<Integer> currentAuth1Test = getAuthority(blockNum, false);
		//test to ensure they are equal
		if(!(VoterAuthority(currentAuth, currentAuthTest) && VoterAuthority(currentAuth1, currentAuth1Test)))
		{
			  //only entered if the two arrays do not match
			currentAuth = new ArrayList<Integer> ();
			currentAuth.add((Integer) blockNum);
			currentAuth1 = new ArrayList<Integer> ();
			currentAuth1.add((Integer) blockNum);
		}
		
		if(currentAuth.contains(suggestedAuth)) {
			//safe authority
			int index = currentAuth.indexOf(suggestedAuth);
			//System.out.println("Authority is within safe limits: direction 0");
			currentAuth = new ArrayList<Integer> (currentAuth.subList(0, index));
			
		}else if(currentAuth1.contains(suggestedAuth)) {
			//System.out.println("Authority is within safe limits: direction 1");
			int index = currentAuth1.indexOf(suggestedAuth);
			currentAuth1 = new ArrayList<Integer> (currentAuth1.subList(0, index));
		}
		 //call central to pass to track
		 System.out.println(currentAuth.toString() + "  |  " +currentAuth1.toString());
		 System.out.println("To train: " + trainNum);
		 if(integated) {
			 central.WaysideSendAuthority(currentAuth, currentAuth1, trainNum);
		 }
	}
	
	//called everytime a block with a switch is no longer occupied move and lock the switch
	//reduces throughput, but is safe
	private boolean LockSwitch(int blockNum)
	{
		//unlock the switch to enable it to move
		track.get(blockNum).setLock(false);
		//check that the block has a switch on it
		if(track.get(blockNum).hasSwitch())
		{
			if(lineNames[0].equals("Red")){
				//hardcoded check to ensure not shutting off the line
				if(blockNum != 9){
					track.get(blockNum).moveSwitch();
					track.get(blockNum).setLock(true);
					System.out.println("Switch has been moved and locked");
				}
			}else if(lineNames[0].equals("Green")){
				//hardcoded check to ensure not shutting off the line
				if(blockNum != 58 && blockNum != 62){
					track.get(blockNum).moveSwitch();
					track.get(blockNum).setLock(true);
					System.out.println("Switch has been moved and locked");
				}
			}
		}
		return false;
	}
	
	//this method will be used for safety vital code
	//compares 2 values too ensure the match
	private boolean VoterAuthority(ArrayList<Integer> auth1, ArrayList<Integer> auth2)
	{
		//if both arrays contain all the same points, they are equal and the results are valid
		System.out.println(auth1.toString() + "      " + auth2.toString());
		Object[] testArray1 = auth1.toArray();
		Object[] testArray2 = auth2.toArray();
		System.out.println("The authorities do match: " + Arrays.equals(testArray1, testArray2));
		return Arrays.equals(testArray1, testArray2);
	}
	
	//this method will not lock the switch, since it is called externally
	//locking shall only occur from internal wayside methods
	public boolean SwitchSwitch(int blockNum, boolean state) 
	{
		//move switch in BlockInfo with this method
		if(track.get(blockNum).setSwitch(state))
		{
			central.TrackMoveSwitch(blockNum, lineNames[0]);
		}
		return track.get(blockNum).switchState();
	}
	
	//passes mataince request from central to TrackModel
	public void SetMaintainance(int blockNum, String line)
	{
		central.SetMaintainanceToTrackModel(blockNum, line);
	}
}
