//Ethan shuffelbottom
//migrate(d) all no GUI related functionality from WaysideController
//to this Wayside. This was done for integration 
//this is the handle that he central will use to controll everything

import java.util.ArrayList;

import javafx.stage.Stage;

public class Wayside {

	public ArrayList<BlockInfo> track;
	public ArrayList<String> trackSections = new ArrayList<String>();
	public ArrayList<ArrayList<String>> blockSections = new ArrayList<ArrayList<String>>();
	public int currentBlock = 0;
	public ArrayList<Integer> currentAuth, occBlocks;
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
	public Wayside(String[] args, Central cen){
		central = cen;
		//getTrack();
		//getSections();
		track = getTrack();
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
	
	
	//method for getting the Authority
	public ArrayList<Integer> getAuthority(int blockNow, boolean direction) {
		ArrayList<Integer> auth;
		System.out.println("calling the authCalc");
		//use PLC
		if(PLCloaded)
		{
			auth = ((PLCinterface)PLC).getAuth(blockNow, direction, track);
		}
		else {
			auth = authCalc.getAuth(blockNow, direction, track);
		}
		//TODO check Authority gotten from these methods
		return auth;
	}
	
	//replace this with getTrack from trackModel
	public ArrayList<BlockInfo> getTrack() {
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
	
	//called from controller method to update track state
	public void AddOccupied(int occBlock)
	{
		
	}
	
	//get newly freed block from track
	public void removeOccupied(int freedBlock)
	{
		
	}
	
	//this method creates a track from the track models object
	public void createTrack()
	{
		
	}
	
	//get speed from central 
	public void suggestSpeed(int blockNum, double speed)
	{
		
	}
	
	//get authority from central
	public void suggestAuthority(int blockNum, int suggestedAuth)
	{
		
	}
	
	public void switchSwitch(int blockNum) 
	{
		
	}
}
