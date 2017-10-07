import java.io.*;
import java.util.ArrayList;
public class TrackModel {
    private ArrayList track = new ArrayList<Block>();
    public static void main(String args[]){
        Block newBlock = new Block(5);
        System.out.println("Block Num: "+newBlock.GetBlockNum());
        LoadNewTrack();
    }
    private static void LoadNewTrack(){
        File f = new File("testTrack.txt");
        FileReader fRead;
        BufferedReader bufRead;
        try {
            fRead = new FileReader(f);
            bufRead = new BufferedReader(fRead);
            String newLine;
            while((newLine = bufRead.readLine())!= null){

            }
        }catch(IOException e){
            System.err.println("Caught IOException: " + e.getMessage());
        }

    }
}