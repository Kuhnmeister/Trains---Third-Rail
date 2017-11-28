//Zachery Blouse COE 1186 Fall 2017 - The Third Rail

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;
import javafx.stage.Stage;

public class TrackModel {
    private HashMap<String,HashMap<String,ArrayList<Block>>> track = new HashMap<String,HashMap<String,ArrayList<Block>>>();
    private HashMap<String,Block> startingBlocks;
    private TrackGui theGui;
    private ArrayList<Block> occupiedBlocks;
    private Central theCentral;
    public ArrayList<String> lineNames = new ArrayList<String>();
    private ArrayList<Block> stations = new ArrayList<Block>();
    private ArrayList<String> stationNames = new ArrayList<String>();
    private int minimumBlockLength=-1;
    private boolean demoMode=false;
    public TrackModel(String[] args, Central newCentral){
        System.out.println("Inside track Model contstructor");
        theCentral=newCentral;
        theGui = new TrackGui(args,this,true);
        theGui.start(new Stage());

    }
    public TrackModel(String[] args){
        theGui = new TrackGui(args,this);
    }
    public static void main(String[] args){
        TrackModel thisModel = new TrackModel(args);

    }
    public void LoadNewTrack(String fileName){
        if(theCentral != null) {
            theCentral.TestMethod("IT Works!!!!!");
        }
        occupiedBlocks=new ArrayList<Block>();
        track = new HashMap<String,HashMap<String,ArrayList<Block>>>();
        startingBlocks = new HashMap<String,Block>();
        File f = new File(fileName);
        FileReader fRead;
        BufferedReader bufRead;
        try {
            fRead = new FileReader(f);
            bufRead = new BufferedReader(fRead);
            String newLine;
            while((newLine = bufRead.readLine())!= null){
                String[] blockString = newLine.split(",");
                int nextBlock0Num;
                int nextBlock1Num;
                int nextSwitchBlockNum;
                if(blockString[7].equals("None")){
                    nextBlock0Num=-1;
                }else{
                    nextBlock0Num = Integer.parseInt(blockString[7]);
                }
                if(blockString[8].equals("None")){
                    nextBlock1Num=-1;
                }else{
                    nextBlock1Num = Integer.parseInt(blockString[8]);
                }
                if(blockString[9].equals("None")){
                    nextSwitchBlockNum=-1;
                }else{
                    nextSwitchBlockNum = Integer.parseInt(blockString[9]);
                }
                //outside if checks if the line is currently in the hashmap, if so moves on the check section, if not it creates the line
                if(track.containsKey(blockString[0])){
                    //checks if the section is currently in the hashmap, if so it adds the block, if not it creates the section

                    if(track.get(blockString[0]).containsKey(blockString[1])){
                        Block newBlock = new Block(blockString[0],blockString[1],Integer.parseInt(blockString[2]),Integer.parseInt(blockString[3]),Float.parseFloat(blockString[4]),Integer.parseInt(blockString[5]),Boolean.parseBoolean(blockString[6]),nextBlock0Num,nextBlock1Num,nextSwitchBlockNum,blockString[10]);
                        track.get(blockString[0]).get(blockString[1]).add(newBlock);
                        if(newBlock.GetLength()<minimumBlockLength|| minimumBlockLength==-1){
                            minimumBlockLength=newBlock.GetLength();
                            System.out.println("New minimum block length: "+minimumBlockLength);
                        }
                    }else{
                        track.get(blockString[0]).put(blockString[1],new ArrayList<Block>());
                        Block newBlock = new Block(blockString[0],blockString[1],Integer.parseInt(blockString[2]),Integer.parseInt(blockString[3]),Float.parseFloat(blockString[4]),Integer.parseInt(blockString[5]),Boolean.parseBoolean(blockString[6]),nextBlock0Num,nextBlock1Num,nextSwitchBlockNum,blockString[10]);
                        track.get(blockString[0]).get(blockString[1]).add(newBlock);
                        if(newBlock.GetLength()<minimumBlockLength || minimumBlockLength==-1){
                            minimumBlockLength=newBlock.GetLength();
                            System.out.println("New minimum block length: "+minimumBlockLength);
                        }
                    }
                }else{
                    track.put(blockString[0],new HashMap<String,ArrayList<Block>>());
                    track.get(blockString[0]).put(blockString[1],new ArrayList<Block>());
                    Block newBlock = new Block(blockString[0],blockString[1],Integer.parseInt(blockString[2]),Integer.parseInt(blockString[3]),Float.parseFloat(blockString[4]),Integer.parseInt(blockString[5]),Boolean.parseBoolean(blockString[6]),nextBlock0Num,nextBlock1Num,nextSwitchBlockNum,blockString[10]);
                    track.get(blockString[0]).get(blockString[1]).add(newBlock);
                    if(startingBlocks.get(blockString[0]) == null){
                        startingBlocks.put(blockString[0],newBlock);
                    }
                    if(newBlock.GetLength()<minimumBlockLength || minimumBlockLength==-1){
                        minimumBlockLength=newBlock.GetLength();
                        System.out.println("New minimum block length: "+minimumBlockLength);

                    }
                    lineNames.add(blockString[0]);
                }

            }
            //Now that the whole track is loaded, we want to give each block a reference to the blocks around them

            //Map.Entry<String, Integer> entry : items.entrySet;
            for(HashMap.Entry<String,HashMap<String,ArrayList<Block>>> line:track.entrySet()){
                for(HashMap.Entry<String,ArrayList<Block>> section: line.getValue().entrySet()){
                    for(int i=0;i<section.getValue().size();i++){
                        boolean found0 = false;
                        boolean found1 = false;
                        boolean foundSwitch=false;
                        if(section.getValue().get(i).GetIsStation()){
                            stations.add(section.getValue().get(i));
                            stationNames.add(section.getValue().get(i).GetStationName());
                        }
                        int nextBlock0 =   section.getValue().get(i).GetDirection0Num();
                        if(nextBlock0==-1){
                            found0=true;
                        }
                        int nextBlock1 = section.getValue().get(i).GetDirection1Num();
                        if(nextBlock1==-1){
                            found1=true;
                        }
                        int nextBlockSwitch = section.getValue().get(i).GetSwitchNum();
                        if(nextBlockSwitch==-1){
                            foundSwitch=true;
                        }
                        for(HashMap.Entry<String,HashMap<String,ArrayList<Block>>> innerLine:track.entrySet()){
                            for(HashMap.Entry<String,ArrayList<Block>> innerSection: innerLine.getValue().entrySet()){
                                for(int j=0;j<innerSection.getValue().size();j++){
                                    if(!found0) {
                                        if (nextBlock0 == innerSection.getValue().get(j).GetBlockNum()) {
                                            section.getValue().get(i).SetDirection0Block(innerSection.getValue().get(j));
                                            if(innerSection.getValue().get(j).GetIsStation()){
                                                section.getValue().get(i).SetHasHeater(true);
                                            }
                                            found0=true;
                                        }
                                    }
                                    if(!found1) {
                                        if (nextBlock1 == innerSection.getValue().get(j).GetBlockNum()) {
                                            section.getValue().get(i).SetDirection1Block(innerSection.getValue().get(j));
                                            if(innerSection.getValue().get(j).GetIsStation()){
                                                section.getValue().get(i).SetHasHeater(true);
                                            }
                                            found1=true;
                                        }
                                    }
                                    if(!foundSwitch) {
                                        if (nextBlockSwitch == innerSection.getValue().get(j).GetBlockNum()) {
                                            section.getValue().get(i).SetSwitchBlock(innerSection.getValue().get(j));
                                            if(innerSection.getValue().get(j).GetIsStation()){
                                                section.getValue().get(i).SetHasHeater(true);
                                            }
                                            foundSwitch=true;
                                        }
                                    }
                                    if(found0 && found1 && foundSwitch){
                                        break;
                                    }
                                }
                                if(found0 && found1 && foundSwitch){
                                    break;
                                }
                            }
                            if(found0 && found1 && foundSwitch){
                                break;
                            }
                        }
                    }
                }
            }

        }catch(IOException e){
            System.err.println("Caught IOException: " + e.getMessage());
        }

    }
    public void AddOccupied(Block newBlock){
            if(!demoMode){
                //call central class to report new occupied to Wayside
            }
            occupiedBlocks.add(newBlock);
            System.out.println("Added Block: " + newBlock.GetBlockNum());
    }
    public void RemoveOccupied(Block newBlock){
        if(!demoMode){
            //call central class to report new unoccupied to Wayside
        }
        occupiedBlocks.remove(newBlock);
        System.out.println("Removed Block: "+newBlock.GetBlockNum());
    }
    public ArrayList<String> DisplaySection(String Line, String Section){
        ArrayList<String> sectionData = new ArrayList<String>();
        int sectionSize=track.get(Line).get(Section).size();
        for(int i=0;i<sectionSize;i++){
            sectionData.add(track.get(Line).get(Section).get(i).PrintBlock());
        }

        return sectionData;
    }
    //Updating Data Sent to Wayside
    public ArrayList<Block> GetNewWaysideOutput(){
        return occupiedBlocks;
    }
    public Block GetStartingBlock(String trainLine){
        return startingBlocks.get(trainLine);
    }
    public Block GetBlock(int requestedBlockNum){

        for (HashMap.Entry<String, HashMap<String, ArrayList<Block>>> line : track.entrySet()) {
            for (HashMap.Entry<String, ArrayList<Block>> section : line.getValue().entrySet()) {
                for (int i = 0; i < section.getValue().size(); i++) {
                    if (section.getValue().get(i).GetBlockNum() == requestedBlockNum) {
                        return section.getValue().get(i);
                    }
                }

            }
        }
        return null;

    }
    public void WaysideInput(int updateBlock,String newLightColor,boolean flipSwitch){
            Block editingBlock = GetBlock(updateBlock);
            editingBlock.SetLightColor(newLightColor);
            editingBlock.FlipSwitch(flipSwitch);

    }

    public ArrayList<String> GetStationNameList(){
        return stationNames;
    }
    public Block GetStationBlock(String statName){
        for(Block stationBlock:stations){
            if(stationBlock.GetStationName()==statName){
                return stationBlock;
            }
        }
        return null;
    }
    public ArrayList<String> GetLineNamesList(){
        return lineNames;
    }
    public ArrayList<String> GetSectionNamesList(String selectedLineName){
        Collection<String> sectionNames=track.get(selectedLineName).keySet();
        ArrayList<String> listOfSections = new ArrayList<String>(sectionNames);
        System.out.println(listOfSections);
        return listOfSections;
    }
    public void SetBeaconDataString(int blockNum, String messageString){
        GetBlock(blockNum).SetBeaconMessageString(messageString);
    }
    public void SetBrokenRail(int blockNum){
        GetBlock(blockNum).SetBrokenRail();
        AddOccupied(GetBlock(blockNum));
    }
    public void SetTrackCircuitFail(int blockNum){
        GetBlock(blockNum).SetTrackCircuitFail();
        AddOccupied(GetBlock(blockNum));
    }
    public void SetPowerFail(int blockNum){
        GetBlock(blockNum).SetPowerFail();
        RemoveOccupied(GetBlock(blockNum));
    }
    public void RemoveForceMajeure(int blockNum){
        int whichForce =0;
        Block affectedBlock = GetBlock(blockNum);
        if( affectedBlock.GetTrackCircuitFail()||affectedBlock.GetBrokenRail()){
            whichForce=1;
        }
        if(affectedBlock.GetPowerFail()){
            whichForce=-1;
        }
        affectedBlock.RemoveAllForceMajeure();
        if(whichForce==1){
            RemoveOccupied(affectedBlock);

        }else if(whichForce==-1){

        }else{
            return;
        }
        if(GetBlock(blockNum).GetIsOccupied()){
            AddOccupied(affectedBlock);
        }
    }
    public void SetTicketCount(String stationName,int ticketCount){
        Block stationBlock = GetStationBlock(stationName);
        stationBlock.GetStation().AddTickets(ticketCount);
    }

    //***********************************************************Integrated Methods*******************************************//
    //Called when a train is dispatched from the station
    public void NewTrain(int trainNum){

    }
    //Get authority from actual wayside. Wayside will return arraylist<int> which will represent the block nums of all the blocks of authority starting with block the train is on
    //blockNum is the number of the block the train we are seeking authority is on
    //this method returns the distance the train can travel
    private int GetAuthorityFromWayside(int blockNum){
        return 0;
    }
    

    public HashMap<String,HashMap<String,ArrayList<Block>>> GetTrack(){
        return track;
    }



}
//String newLine,char newSection, int newBlockNum, int newLength, float newGrade, int newSpeedLimit,String newInfrastructure