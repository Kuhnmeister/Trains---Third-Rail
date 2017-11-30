import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Timer;
import java.util.List;

public class Tracking{
	
	
	
	HashMap<String, HashMap<String, ArrayList<Block>>> trackData;
	HashMap<String, ArrayList<Block>> section;
	ArrayList<String[]> sectionList = new ArrayList<String[]>();
	HashMap<Block, Integer> blockInfrastructure = new HashMap<Block, Integer>();
	String[] lines;
	ArrayList<Block> blocks = new ArrayList<Block>();
	HashMap< String, ArrayList<Block>> lineBlocks = new HashMap<String, ArrayList<Block>>();
	ArrayList<Block> lineBlockList = new ArrayList<Block>();
	boolean trackTrue = false;
	public Tracking(){
		
	}
	public void receiveTrackData(HashMap<String, HashMap<String, ArrayList<Block>>> track){
		trackData = track;
		lines = Arrays.copyOf(track.keySet().toArray(), track.keySet().toArray().length, String[].class);
		for(int i = 0; i < lines.length; i++){
			section = trackData.get(lines[i]);
			lineBlockList.clear();
			sectionList.add(Arrays.copyOf(section.keySet().toArray(), section.keySet().toArray().length, String[].class));
			for(int j = 0; j < sectionList.get(i).length; j++){
				String[] sectionThru = sectionList.get(i);
				ArrayList<Block> medium = section.get(sectionThru[j]);				
				
				for(int k = 0; k < medium.size(); k++){
					blocks.add(medium.get(k));
					lineBlockList.add(medium.get(k));
				}
			}
			lineBlocks.put(lines[i], lineBlockList);	
		}
		
		for(int x = 0; x < blocks.size(); x++){
			int key = 0;
			if(blocks.get(x).GetHasSwitch()){
				key -= 5;
			}
			if(blocks.get(x).GetIsUnderground()){
				key += 2;
			}
			if(blocks.get(x).GetHasRailwayCrossing()){
				key += 1;
			}
			if(blocks.get(x).GetIsStation()){
				key += 20;
			}
			blockInfrastructure.put(blocks.get(x), key);
		}	
		for(int f = 0; f < blocks.size(); f++){
			System.out.println(blocks.get(f));
		}
	}
	public boolean TrackTrue(){
		return trackTrue;
	}
	public String[] getLines(){
		return lines;
	}
	public ArrayList<Integer> blocks(String choice){
		ArrayList<Integer> blockReturn = new ArrayList<Integer>();
		ArrayList<Block> get = new ArrayList<Block>();
		get = lineBlocks.get(choice);
		for(int v = 0; v < get.size(); v++){
			blockReturn.add(get.get(v).GetBlockNum());
		}
		return blockReturn;
	}

	public boolean hasStation(int choice, String lineChoice){
		int checker = blockInfrastructure.get(GetBlock(choice, lineChoice));
		System.out.println(checker);
		if(checker >= 15){
			return true;
		}
		return false;
	}
	public boolean hasCrossing(int choice, String lineChoice){

		int checker = blockInfrastructure.get(GetBlock(choice, lineChoice));
		System.out.println(checker);
		if(checker == 1){
			return true;
		}
		if(checker == 3){
			return true;
		}
		if(checker == -4){
			return true;
		}
		if(checker == -2){
			return true;
		}
		if(checker == 21){
			return true;
		}
		if(checker == 23){
			return true;
		}
		if(checker == 16){
			return true;
		}
		if(checker == 18){
			return true;
		}		
		return false;
	}
	public boolean isUnderground(int choice, String lineChoice){
		int checker = blockInfrastructure.get(GetBlock(choice, lineChoice));
		System.out.println(checker);
		if(checker == -3){
			return true;
		}
		if(checker == 3){
			return true;
		}
		if(checker == 2){
			return true;
		}
		if(checker == -2){
			return true;
		}
		if(checker == 22){
			return true;
		}
		if(checker == 23){
			return true;
		}
		if(checker == 17){
			return true;
		}
		if(checker == 18){
			return true;
		}
		return false;
	}
	public boolean hasSwitch(int choice, String lineChoice){
		int checker = blockInfrastructure.get(GetBlock(choice, lineChoice));
		System.out.println(checker);
		if(checker == -5){
			return true;
		}
		if(checker == -3){
			return true;
		}
		if(checker == -4){
			return true;
		}
		if(checker == -2){
			return true;
		}
		if(checker == 15){
			return true;
		}
		if(checker == 17){
			return true;
		}
		if(checker == 16){
			return true;
		}
		if(checker == 18){
			return true;
		}
		return false;
	}
	public Block GetBlock(int requestedBlockNum, String rLine){
        for (HashMap.Entry<String, HashMap<String, ArrayList<Block>>> line : trackData.entrySet()) {
            for (HashMap.Entry<String, ArrayList<Block>> section : line.getValue().entrySet()) {
                for (int i = 0; i < section.getValue().size(); i++) {
                    if (section.getValue().get(i).GetBlockNum() == requestedBlockNum) {
                        if(section.getValue().get(i).GetLine().equals(rLine)) {
                            return section.getValue().get(i);
                        }
                    }
                }
            }

        }
        return null;
    }
}
