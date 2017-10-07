import java.io.*;
import java.util.ArrayList;
public class TrackModel {
    private ArrayList<Block> track = new ArrayList<Block>();
    public static void main(String args[]){
        TrackModel thisTrack = new TrackModel();
        thisTrack.Initialize();

    }
    private void Initialize(){
        LoadNewTrack();
        Block newBlock = track.get(1);
        System.out.println("Block Num: "+newBlock.GetBlockNum());
    }
    private void LoadNewTrack(){
        File f = new File("testTrack.txt");
        FileReader fRead;
        BufferedReader bufRead;
        try {
            fRead = new FileReader(f);
            bufRead = new BufferedReader(fRead);
            String newLine;
            while((newLine = bufRead.readLine())!= null){
                String[] blockString = newLine.split(",");
                Block newBlock = new Block(Integer.parseInt(blockString[0]),Integer.parseInt(blockString[1]),Integer.parseInt(blockString[2]));
                track.add(newBlock);
            }
        }catch(IOException e){
            System.err.println("Caught IOException: " + e.getMessage());
        }

    }
}