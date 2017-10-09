import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TrackModel {
    //private ArrayList<Block> track = new ArrayList<Block>();
    private HashMap<String,HashMap<String,ArrayList<Block>>> track = new HashMap<String,HashMap<String,ArrayList<Block>>>();
   // public static void main(String args[]){
       // TrackModel thisTrack = new TrackModel();
       // thisTrack.Initialize();
   // }
    //private void Initialize(){
    //    MyGui thisGui = new MyGui(this);
     //   thisGui.SetTrackModel(this);
     //   String[] guiArgs=null;
   //     thisGui.main(guiArgs);


    //    Block newBlock = track.get(2);
    //    System.out.println("Underground: "+newBlock.GetIsUnderground()+"Grade: "+newBlock.GetGrade() + "Station: "+newBlock.GetStationName());
   // }
    public void LoadNewTrack(String fileName){
        File f = new File(fileName);
        FileReader fRead;
        BufferedReader bufRead;
        try {
            fRead = new FileReader(f);
            bufRead = new BufferedReader(fRead);
            String newLine;
            while((newLine = bufRead.readLine())!= null){
                String[] blockString = newLine.split(",");
                //outside if checks if the line is currently in the hashmap, if so moves on the check section, if not it creates the line
                if(track.containsKey(blockString[0])){
                    //checks if the section is currently in the hashmap, if so it adds the block, if not it creates the section
                    if(track.get(blockString[0]).containsKey(blockString[1])){
                        Block newBlock = new Block(blockString[0],blockString[1],Integer.parseInt(blockString[2]),Integer.parseInt(blockString[3]),Float.parseFloat(blockString[4]),Integer.parseInt(blockString[5]),Boolean.parseBoolean(blockString[6]),blockString[7]);
                        track.get(blockString[0]).get(blockString[1]).add(newBlock);
                    }else{
                        track.get(blockString[0]).put(blockString[1],new ArrayList<Block>());
                        Block newBlock = new Block(blockString[0],blockString[1],Integer.parseInt(blockString[2]),Integer.parseInt(blockString[3]),Float.parseFloat(blockString[4]),Integer.parseInt(blockString[5]),Boolean.parseBoolean(blockString[6]),blockString[7]);
                        track.get(blockString[0]).get(blockString[1]).add(newBlock);
                    }
                }else{
                    track.put(blockString[0],new HashMap<String,ArrayList<Block>>());
                    track.get(blockString[0]).put(blockString[1],new ArrayList<Block>());
                    Block newBlock = new Block(blockString[0],blockString[1],Integer.parseInt(blockString[2]),Integer.parseInt(blockString[3]),Float.parseFloat(blockString[4]),Integer.parseInt(blockString[5]),Boolean.parseBoolean(blockString[6]),blockString[7]);
                    track.get(blockString[0]).get(blockString[1]).add(newBlock);
                }
                //Block newBlock = new Block(blockString[0],blockString[1].charAt(0),Integer.parseInt(blockString[2]),Integer.parseInt(blockString[3]),Float.parseFloat(blockString[4]),Integer.parseInt(blockString[5]),blockString[6]);
                //track.add(newBlock);
            }
        }catch(IOException e){
            System.err.println("Caught IOException: " + e.getMessage());
        }

    }
    //public void PrintTrack(){
        //System.out.println("Block Num: "+track.get(0).GetBlockNum());
    //}
    public ArrayList<String> DisplaySection(String Line, String Section){
        ArrayList<String> sectionData = new ArrayList<String>();
        int sectionSize=track.get(Line).get(Section).size();
        for(int i=0;i<sectionSize;i++){
            sectionData.add(track.get(Line).get(Section).get(i).PrintBlock());
        }

        return sectionData;
    }
    //Updating Data Sent to Wayside
    public void WaysideSendNewData(){

    }
}
//String newLine,char newSection, int newBlockNum, int newLength, float newGrade, int newSpeedLimit,String newInfrastructure