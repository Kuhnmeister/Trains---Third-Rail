import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TrackModel {
    HashMap<String,HashMap<String,ArrayList<Block>>> track = new HashMap<String,HashMap<String,ArrayList<Block>>>();
    private HashMap<String,Block> startingBlocks;
    private MyGui theGui;
    private ArrayList<Block> occupiedBlocks;
    public TrackModel(MyGui newGui){
        theGui=newGui;
    }
    public void LoadNewTrack(String fileName){
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
                    }else{
                        track.get(blockString[0]).put(blockString[1],new ArrayList<Block>());
                        Block newBlock = new Block(blockString[0],blockString[1],Integer.parseInt(blockString[2]),Integer.parseInt(blockString[3]),Float.parseFloat(blockString[4]),Integer.parseInt(blockString[5]),Boolean.parseBoolean(blockString[6]),nextBlock0Num,nextBlock1Num,nextSwitchBlockNum,blockString[10]);
                        track.get(blockString[0]).get(blockString[1]).add(newBlock);
                    }
                }else{
                    track.put(blockString[0],new HashMap<String,ArrayList<Block>>());
                    track.get(blockString[0]).put(blockString[1],new ArrayList<Block>());
                    Block newBlock = new Block(blockString[0],blockString[1],Integer.parseInt(blockString[2]),Integer.parseInt(blockString[3]),Float.parseFloat(blockString[4]),Integer.parseInt(blockString[5]),Boolean.parseBoolean(blockString[6]),nextBlock0Num,nextBlock1Num,nextSwitchBlockNum,blockString[10]);
                    track.get(blockString[0]).get(blockString[1]).add(newBlock);
                    if(startingBlocks.get(blockString[0]) == null){
                        startingBlocks.put(blockString[0],newBlock);
                    }
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
            occupiedBlocks.add(newBlock);
            System.out.println("Added Block: " + newBlock.GetBlockNum());
    }
    public void RemoveOccupied(Block newBlock){
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
    public ArrayList<Block> getNewWaysideOutput(){
        return occupiedBlocks;
    }
    public Block GetStartingBlock(String trainLine){
        return startingBlocks.get(trainLine);
    }
    public Block GetBlock(int requestedBlockNum){
        for(HashMap.Entry<String,HashMap<String,ArrayList<Block>>> line:track.entrySet()) {
            for (HashMap.Entry<String, ArrayList<Block>> section : line.getValue().entrySet()) {
                for (int i = 0; i < section.getValue().size(); i++) {
                    if(section.getValue().get(i).GetBlockNum()==requestedBlockNum){
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
}
//String newLine,char newSection, int newBlockNum, int newLength, float newGrade, int newSpeedLimit,String newInfrastructure