import java.io.*;
import java.util.ArrayList;

public class TrackModel {
    private ArrayList<Block> track = new ArrayList<Block>();
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
                Block newBlock = new Block(blockString[0],blockString[1].charAt(0),Integer.parseInt(blockString[2]),Integer.parseInt(blockString[3]),Float.parseFloat(blockString[4]),Integer.parseInt(blockString[5]),blockString[6]);
                track.add(newBlock);
            }
        }catch(IOException e){
            System.err.println("Caught IOException: " + e.getMessage());
        }

    }
    public void PrintTrack(){
        System.out.println("Block Num: "+track.get(0).GetBlockNum());
    }
}
//String newLine,char newSection, int newBlockNum, int newLength, float newGrade, int newSpeedLimit,String newInfrastructure