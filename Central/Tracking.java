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
	ArrayList<String[]> sectionList;
	HashMap<Block, Integer> blockInfrastructure;
	String[] lines;
	ArrayList<Block> blocks;
	HashMap< String, ArrayList<Block>> lineBlocks;
	ArrayList<Block> lineBlockList = new ArrayList<Block>();
	boolean trackTrue = false;
	public Tracking(){
		
	}
	public void receiveTrackData(HashMap<String, HashMap<String, ArrayList<Block>>> track){
		trackData = track;
		lines = Arrays.copyOf(track.keySet().toArray(), track.keySet().toArray().length, String[].class);
		for(int i = 0; i < lines.length; i++){
			section = trackData.get(lines[i]);
			System.out.println(section.keySet.toArray().length);
			System.out.println(section.keySEt().toArray();
			sectionList.add(Arrays.copyOf(section.keySet().toArray(), section.keySet().toArray().length, String[].class));
			for(int j = 0; j < sectionList.get(i).length; j++){
				String[] sectionThru = sectionList.get(i);
				ArrayList<Block> medium = section.get(sectionThru[j]);
				for(int k = 0; k < medium.size(); k++){
					blocks.add(medium.get(k));
				}
			}
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
		for(int l = 0; l < lines.length; l++){
			String choice = lines[l];
			ArrayList<Integer> blocksReturned;
			for(int m = 0; m < trackData.get(choice).keySet().toArray().length; m++){
				Arrays.copyOf(trackData.get(choice).keySet().toArray(), trackData.get(choice).keySet().toArray().length, String[].class);
				String[] checkLines = Arrays.copyOf(trackData.get(choice).keySet().toArray(), trackData.get(choice).keySet().toArray().length, String[].class);
				ArrayList<Block> temp = trackData.get(choice).get(checkLines[m]);
				for(int n = 0; n < temp.size(); n++){
					lineBlockList.add(temp.get(n));
				}
			}
			lineBlocks.put(choice, lineBlockList);
		}
		trackTrue = true;
	}
	public boolean TrackTrue(){
		return trackTrue;
	}
	public String[] getLines(){
		return lines;
	}
	public ArrayList<Integer> blocks(String choice){
		ArrayList<Integer> blockReturn = new ArrayList<Integer>();
		for(int v = 0; v < lineBlocks.get(choice).size(); v++){
			blockReturn.add(lineBlocks.get(choice).get(v).GetBlockNum());
		}
		return blockReturn;
	}

	public boolean hasStation(int choice){
		int checker = blockInfrastructure.get(GetBlock(choice));
		if(checker >= 15){
			return true;
		}
		return false;
	}
	public boolean hasCrossing(int choice){
		int checker = blockInfrastructure.get(GetBlock(choice));
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
	public boolean isUnderground(int choice){
		int checker = blockInfrastructure.get(GetBlock(choice));
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
	public boolean hasSwitch(int choice){
		int checker = blockInfrastructure.get(GetBlock(choice));
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
	public Block GetBlock(int requestedBlockNum){
        for (HashMap.Entry<String, HashMap<String, ArrayList<Block>>> line : trackData.entrySet()) {
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
}
