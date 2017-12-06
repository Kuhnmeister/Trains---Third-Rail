//Zachery Blouse COE 1186 Fall 2017 - The Third Rail


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;
import javafx.stage.Stage;
import java.util.BitSet;

public class TrackModel {
    private HashMap<String,HashMap<String,ArrayList<Block>>> track = new HashMap<String,HashMap<String,ArrayList<Block>>>();
    private HashMap<String,Block> startingBlocks;
    private TrackGui theGui;
    private int multiplier=1;
    private ArrayList<Block> occupiedBlocks;
    private Central theCentral;
    public ArrayList<String> lineNames = new ArrayList<String>();
    private ArrayList<Block> stations = new ArrayList<Block>();
    private ArrayList<String> stationNames = new ArrayList<String>();
    private HashMap<Integer,Train> allTrains = new HashMap<Integer,Train>();
    private double minimumBlockLength=-1;
    private boolean demoMode=false;
    public TrackModel(String[] args, Central newCentral){
        System.out.println("Inside track Model contstructor");
        theCentral=newCentral;
        theGui = new TrackGui(args,this,true);
        theGui.start(new Stage());

    }
    public TrackModel(String[] args){
        demoMode=true;
        theGui = new TrackGui(args,this);

    }
    public static void main(String[] args){
        TrackModel thisModel = new TrackModel(args);


    }
    public void ReportBeaconData(BitSet beaconData, int trainNum){
        if(!demoMode){
            theCentral.ReportBeaconData(beaconData, trainNum);
        }else{
            System.out.println("Track Transmitted: "+beaconData+" to train: "+trainNum);
        }
    }
    public int GetMultiplier(){
        return multiplier;
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
                        Block newBlock = new Block(blockString[0],blockString[1],Integer.parseInt(blockString[2]),Double.parseDouble(blockString[3]),Float.parseFloat(blockString[4]),Integer.parseInt(blockString[5]),Boolean.parseBoolean(blockString[6]),nextBlock0Num,nextBlock1Num,nextSwitchBlockNum,blockString[10]);
                        track.get(blockString[0]).get(blockString[1]).add(newBlock);
                        if(newBlock.GetLength()<minimumBlockLength|| minimumBlockLength==-1){
                            minimumBlockLength=newBlock.GetLength();
                            System.out.println("New minimum block length: "+minimumBlockLength);
                        }
                    }else{
                        track.get(blockString[0]).put(blockString[1],new ArrayList<Block>());
                        Block newBlock = new Block(blockString[0],blockString[1],Integer.parseInt(blockString[2]),Double.parseDouble(blockString[3]),Float.parseFloat(blockString[4]),Integer.parseInt(blockString[5]),Boolean.parseBoolean(blockString[6]),nextBlock0Num,nextBlock1Num,nextSwitchBlockNum,blockString[10]);
                        track.get(blockString[0]).get(blockString[1]).add(newBlock);
                        if(newBlock.GetLength()<minimumBlockLength || minimumBlockLength==-1){
                            minimumBlockLength=newBlock.GetLength();
                            System.out.println("New minimum block length: "+minimumBlockLength);
                        }
                    }
                }else{
                    track.put(blockString[0],new HashMap<String,ArrayList<Block>>());
                    track.get(blockString[0]).put(blockString[1],new ArrayList<Block>());
                    Block newBlock = new Block(blockString[0],blockString[1],Integer.parseInt(blockString[2]),Double.parseDouble(blockString[3]),Float.parseFloat(blockString[4]),Integer.parseInt(blockString[5]),Boolean.parseBoolean(blockString[6]),nextBlock0Num,nextBlock1Num,nextSwitchBlockNum,blockString[10]);
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
                        String blockLine= section.getValue().get(i).GetLine();
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
                                        if (nextBlock0 == innerSection.getValue().get(j).GetBlockNum() && blockLine.equals(innerSection.getValue().get(j).GetLine()) ) {
                                            section.getValue().get(i).SetDirection0Block(innerSection.getValue().get(j));
                                            if(innerSection.getValue().get(j).GetIsStation()){
                                                section.getValue().get(i).SetHasHeater(true);
                                            }
                                            found0=true;
                                        }
                                    }
                                    if(!found1) {
                                        if (nextBlock1 == innerSection.getValue().get(j).GetBlockNum() && blockLine.equals(innerSection.getValue().get(j).GetLine())) {
                                            section.getValue().get(i).SetDirection1Block(innerSection.getValue().get(j));
                                            if(innerSection.getValue().get(j).GetIsStation()){
                                                section.getValue().get(i).SetHasHeater(true);
                                            }
                                            found1=true;
                                        }
                                    }
                                    if(!foundSwitch) {
                                        if (nextBlockSwitch == innerSection.getValue().get(j).GetBlockNum() && blockLine.equals(innerSection.getValue().get(j).GetLine())) {
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
                        if(!found0||!found1||!foundSwitch){
                            System.out.println("Can't find next block on line:"+blockLine);
                        }
                    }
                }
            }
            if(!demoMode) {
                theCentral.UpdateTrack(track);
            }
        }catch(IOException e){
            System.err.println("Caught IOException: " + e.getMessage());
        }

    }
    public void AddOccupied(Block newBlock){
        if(!demoMode){
            IntegratedAddOccupancy(newBlock.GetBlockNum(), newBlock.GetLine());
        }
        occupiedBlocks.add(newBlock);
        System.out.println("Added Block: " + newBlock.GetBlockNum());
    }
    public void RemoveOccupied(Block newBlock){
        if(!demoMode){
            IntegratedRemoveOccupancy(newBlock.GetBlockNum(), newBlock.GetLine());
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
    public Block GetBlock(int requestedBlockNum, String rLine){

        for (HashMap.Entry<String, HashMap<String, ArrayList<Block>>> line : track.entrySet()) {

            for (HashMap.Entry<String, ArrayList<Block>> section : line.getValue().entrySet()) {
                for (int i = 0; i < section.getValue().size(); i++) {
                    if (section.getValue().get(i).GetBlockNum() == requestedBlockNum) {
                        if(section.getValue().get(i).GetLine().equals(rLine)) {
                            return section.getValue().get(i);
                        }else{
                            System.out.println("Block "+section.getValue().get(i).GetBlockNum()+" is on line: "+section.getValue().get(i).GetLine()+ " looking for "+rLine);
                        }
                    }
                }
            }

        }
        return null;

    }
    public void WaysideInput(int updateBlock,String line, String newLightColor,boolean flipSwitch){
        Block editingBlock = GetBlock(updateBlock, line);
        if(editingBlock==null){
            System.out.println("the block is null");
        }else if(newLightColor == null){
            System.out.println("the light color is null");
        }else {
            editingBlock.SetLightColor(newLightColor);
            editingBlock.FlipSwitch(flipSwitch);
        }

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
    public void SetBeaconDataString(int blockNum, String line, String messageString){
        GetBlock(blockNum,line).SetBeaconMessageString(messageString);
    }
    public void SetBrokenRail(int blockNum, String line){
        if(GetBlock(blockNum,line)==null){
            System.out.println("Null Block blockNum: "+blockNum+" line: "+line);
        }else {
            GetBlock(blockNum, line).SetBrokenRail();
            AddOccupied(GetBlock(blockNum, line));
        }

    }
    public void SetTrackCircuitFail(int blockNum, String line){
        GetBlock(blockNum,line).SetTrackCircuitFail();
        AddOccupied(GetBlock(blockNum, line));
    }
    public void SetPowerFail(int blockNum, String line){
        GetBlock(blockNum,line).SetPowerFail();
        RemoveOccupied(GetBlock(blockNum, line));
    }
    public void RemoveForceMajeure(int blockNum, String line){
        int whichForce =0;
        Block affectedBlock = GetBlock(blockNum, line);
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
        if(GetBlock(blockNum,line).GetIsOccupied()){
            AddOccupied(affectedBlock);
        }
    }
    public void SetTicketCount(String stationName,int ticketCount){
        Block stationBlock = GetStationBlock(stationName);
        stationBlock.GetStation().AddTickets(ticketCount);
    }
    public void GenerateTickets(int blockNum, String line){
        int newTickets=0;
        Block stationBlock = GetBlock(blockNum,line);
        if(stationBlock==null){
            System.out.println("Station block is null: "+blockNum);
        }else{
            newTickets= stationBlock.GenerateTickets();
            if(!demoMode) {
                theCentral.TrackGenerateTickets(newTickets, blockNum, line);
            }else{
                System.out.println("Track reported: "+newTickets+" at Block: "+blockNum+" on "+line+" line");
            }
        }

    }
    public void RemoveTrain(Train removingTrain){
        if(!demoMode) {
            theCentral.TrainToYard(removingTrain.GetTrainNum());
        }
        allTrains.remove(removingTrain.GetTrainNum());
    }

    //***********************************************************Integrated Methods*******************************************//
    //Called when a train is dispatched from the station
    public void NewTrain(int trainNum, int length, int direction,int startBlock,String line){

        Train newTrain =new Train(trainNum, length,direction, GetBlock(startBlock, line),this,line,true);
        theGui.AddTrain(newTrain);
        allTrains.put(trainNum,newTrain);
    }
    public void TrainModelUpdatePosition(int trainNum, double movedDistance){
        allTrains.get(trainNum).UpdatePositionIntegrated(movedDistance);
    }
    public void NewTrain(int trainNum, int length, int direction, int startBlock,String line,boolean noTrainModel){

        Train newTrain =new Train(trainNum, length,direction, GetBlock(startBlock,line),this,line);
        theGui.AddTrain(newTrain,true);
        allTrains.put(trainNum,newTrain);
        System.out.println("Train created: "+ newTrain.GetTrainNum());
    }
    public void WaysideCommandedSpeed(int trainNum, double speed){
        theCentral.TrainModelCommandedSpeed(trainNum,speed);
    }
    public void WaysideCommandedSpeed(int trainNum, double speed,boolean noTrainModel){
        allTrains.get(trainNum).SetVelocity(speed);
        System.out.println("Update Speed Train: "+trainNum+" to "+speed);
    }
    public void IntegratedAddOccupancy(int blockNum,String line){
        theCentral.WaysideAddOccupied(blockNum,line);
    }
    public void IntegratedRemoveOccupancy(int blockNum,String line){
        theCentral.WaysideRemoveOccupied(blockNum,line);
    }


    //Get authority from actual wayside. Wayside will return arraylist<int> which will represent the block nums of all the blocks of authority starting with block the train is on
    //blockNum is the number of the block the train we are seeking authority is on
    //this method returns the distance the train can travel
    public void CommandedAuthority(ArrayList<Integer> authorityBlocks,ArrayList<Integer> authorityBlocks1, int trainNum){
        Train theTrain = allTrains.get(trainNum);
        double calcAuthority=0;
        if(theTrain.GetDirection()==0){
            if(theTrain==null) {
                System.out.println("no reference to train: "+trainNum);
            }else{
                if(authorityBlocks == null) {
                    System.out.println("Authority blocks is null");
                }else {
                    if(authorityBlocks.size()>1) {
                        for (Integer i : authorityBlocks) {
                            calcAuthority += GetBlock(i, theTrain.GetLine()).GetLength();
                        }
                    }
                    System.out.println("New authority sent to train: "+calcAuthority);
                    theTrain.SetAuthority(calcAuthority);
                    double authorityMiles = calcAuthority/1609.74;
                    theCentral.TrainSendAuthority(trainNum,authorityMiles);
                }
            }
        }else{
            if(theTrain==null) {
                System.out.println("no reference to train: "+trainNum);
            }else{
                if(authorityBlocks1 == null) {
                    System.out.println("Authority blocks is null");
                }else {
                    if(authorityBlocks.size()>1) {
                        for (Integer i : authorityBlocks1) {
                            calcAuthority += GetBlock(i, theTrain.GetLine()).GetLength();
                        }
                    }
                    System.out.println("New authority sent to train: "+calcAuthority);
                    theTrain.SetAuthority(calcAuthority);
                }
            }
        }
    }
    public void CommandedAuthorityBlock(ArrayList<Integer> authorityBlocks,ArrayList<Integer> authorityBlocks1, int blockNum,String line){
        Train theTrain= new Train();
        for (Train value : allTrains.values()) {
            if(value.GetCurrentBlock().GetBlockNum()==blockNum && line.equals(value.GetLine())){
                theTrain=value;
            }
        }
        double calcAuthority=0;
        if(!theTrain.GetIsReal()){
            System.out.println("Can't find train on block: "+blockNum);
        }else {
            if (theTrain.GetDirection() == 0) {
                if (theTrain == null) {

                } else {
                    if (authorityBlocks == null) {
                        System.out.println("Authority blocks is null");
                    } else {
                        if (authorityBlocks.size() > 1) {
                            for (Integer i : authorityBlocks) {
                                calcAuthority += GetBlock(i, theTrain.GetLine()).GetLength();
                            }
                        }
                        System.out.println("New authority sent to train: " + calcAuthority);
                        theTrain.SetAuthority(calcAuthority);
                        double authorityMiles = calcAuthority / 1609.74;
                        theCentral.TrainSendAuthority(theTrain.GetTrainNum(), authorityMiles);
                    }
                }
            } else {
                if (theTrain == null) {

                } else {
                    if (authorityBlocks1 == null) {
                        System.out.println("Authority blocks is null");
                    } else {
                        if (authorityBlocks.size() > 1) {
                            for (Integer i : authorityBlocks1) {
                                calcAuthority += GetBlock(i, theTrain.GetLine()).GetLength();
                            }
                        }
                        System.out.println("New authority sent to train: " + calcAuthority);
                        theTrain.SetAuthority(calcAuthority);
                    }
                }
            }
        }
    }
    public void CommandedAuthority(ArrayList<Integer> authorityBlocks, int trainNum,boolean noTrainModel){
        if(allTrains.get(trainNum)==null) {
            System.out.println("no reference to train: "+trainNum);
        }else{
            if(authorityBlocks == null) {
                System.out.println("Authority blocks is null");
            }else {

                allTrains.get(trainNum).SetAuthority(authorityBlocks.size() - 1);
                System.out.println("New Authority on train: "+allTrains.get(trainNum).GetTrainNum()+" Authority is set to: "+(authorityBlocks.size() - 1));
            }
        }
    }
    public void CommandedAuthority(ArrayList<Integer> authorityBlocks,ArrayList<Integer> authorityBlocks1, int trainNum,boolean noTrainModel){
        Train theTrain = allTrains.get(trainNum);
        double calcAuthority=0;
        if(theTrain == null){
            System.out.println("Can't find train: "+trainNum);
        }else {
            if (theTrain.GetDirection() == 0) {

                if (authorityBlocks == null) {
                    System.out.println("Authority blocks is null");
                } else {
                    if (authorityBlocks.size() > 1) {
                        for (Integer i : authorityBlocks) {
                            calcAuthority += GetBlock(i, theTrain.GetLine()).GetLength();
                        }
                    }
                    System.out.println("New authority sent to train: " + calcAuthority);
                    theTrain.SetAuthority(calcAuthority);
                }

            } else {

                if (authorityBlocks1 == null) {
                    System.out.println("Authority blocks is null");
                } else {
                    if (authorityBlocks.size() > 1) {
                        for (Integer i : authorityBlocks1) {
                            calcAuthority += GetBlock(i, theTrain.GetLine()).GetLength();
                        }
                    }
                    System.out.println("New authority sent to train: " + calcAuthority);
                    theTrain.SetAuthority(calcAuthority);
                }

            }
        }
    }
    public void CommandedAuthorityBlock(ArrayList<Integer> authorityBlocks,ArrayList<Integer> authorityBlocks1, int blockNum, String line, boolean noTrainModel){
        Train theTrain= new Train();
        for (Train value : allTrains.values()) {
            if(value.GetCurrentBlock().GetBlockNum()==blockNum && line.equals(value.GetLine())){
                theTrain=value;
            }
        }
        double calcAuthority=0;
        if(!theTrain.GetIsReal()){
            System.out.println("Can't find train on block: "+blockNum);
        }else {
            if (theTrain.GetDirection() == 0) {

                if (authorityBlocks == null) {
                    System.out.println("Authority blocks is null");
                } else {
                    if (authorityBlocks.size() > 1) {
                        for (Integer i : authorityBlocks) {
                            calcAuthority += GetBlock(i, theTrain.GetLine()).GetLength();
                        }
                    }
                    System.out.println("New authority sent to train: " + calcAuthority);
                    theTrain.SetAuthority(calcAuthority);
                }

            } else {

                if (authorityBlocks1 == null) {
                    System.out.println("Authority blocks is null");
                } else {
                    if (authorityBlocks.size() > 1) {
                        for (Integer i : authorityBlocks1) {
                            calcAuthority += GetBlock(i, theTrain.GetLine()).GetLength();
                        }
                    }
                    System.out.println("New authority sent to train: " + calcAuthority);
                    theTrain.SetAuthority(calcAuthority);
                }

            }
        }
    }
    public void FlipSwitch(int blockNum, String line){
        GetBlock(blockNum,line).FlipSwitch(true);
    }
    public void FlipCrossing(int blockNum, String line){
        GetBlock(blockNum,line).SwitchCrossing();
    }
    public void SetLight(int blockNum, String line, String lightColor){
        GetBlock(blockNum,line).SetLightColor(lightColor);
    }
    public void SetExecutionMultiplier(int newMultiplier){
        multiplier=newMultiplier;
    }







}
//String newLine,char newSection, int newBlockNum, int newLength, float newGrade, int newSpeedLimit,String newInfrastructure