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
	HashMap<String, Block> firstBlocks = new HashMap<String, Block>(); 
	HashMap<String, Block> lastBlocks = new HashMap<String, Block>();
	boolean trackTrue = false;
	public Tracking(){
		
	}
	public void receiveTrackData(HashMap<String, HashMap<String, ArrayList<Block>>> track){
		trackData = track;
		lines = Arrays.copyOf(track.keySet().toArray(), track.keySet().toArray().length, String[].class);
		for(int i = 0; i < lines.length; i++){
			section = trackData.get(lines[i]);
			ArrayList<Block> blockList = new ArrayList<Block>();
			
			sectionList.add(Arrays.copyOf(section.keySet().toArray(), section.keySet().toArray().length, String[].class));
			for(int j = 0; j < sectionList.get(i).length; j++){
				String[] sectionThru = sectionList.get(i);
				ArrayList<Block> medium = section.get(sectionThru[j]);				
				
				for(int k = 0; k < medium.size(); k++){
					blocks.add(medium.get(k));
					blockList.add(medium.get(k));
					if(medium.get(k).GetFromYard()){
						firstBlocks.put(lines[i], medium.get(k));
					}
					if(medium.get(k).GetToYard()){
						lastBlocks.put(lines[i], medium.get(k));
					}
				}
			}
			System.out.println(lines[i]);
			lineBlocks.put(lines[i], blockList);	
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
	}
	public int GetFirstBlock(String line){
		return firstBlocks.get(line).GetBlockNum();
	}
	public boolean TrackTrue(){
		return trackTrue;
	}
	public String[] getLines(){
		return lines;
	}
	public void updateRoute(ArrayList<Trains> trainList, BBC bbc){
		for(int i = 0; i < trainList.size(); i++){
			if(Double.parseDouble(trainList.get(i).getSpeed()) != 0.0){
				String[] stops = trainList.get(i).getSchedule();
				int location = trainList.get(i).getLocation();
				int difference = 0;
				int nextStop = 0 ;
				for(int j = 0; j < stops.length; j++){
					System.out.println(stops[j] + "POOSY");
					if(location == Integer.parseInt(stops[j])){
						System.out.println("ITS ME");
						trainList.get(i).deleteStop(stops[j]);
						bbc.SetSpeed(i, 0);
						bbc.SetAuthority(i, 0);
					}
					while(difference <= 0){
						difference = Integer.parseInt(stops[j]) - location;
						nextStop = Integer.parseInt(stops[j]);
					}
					if(Integer.parseInt(stops[j]) - location < difference){
						difference = Integer.parseInt(stops[j]) - location;
						nextStop = Integer.parseInt(stops[j]);
					}
					if(location == lastBlocks.get(trainList.get(i).getLine()).GetBlockNum()){
						if(lastBlocks.get(trainList.get(i).getLine()).GetBlockNum() == Integer.parseInt(stops[j])){
							bbc.TrainInYard(i);
						}
					}
				}
				if(location != 0){
					if(hasSwitch(location+1, trainList.get(i).getLine())){
						int direction = nextStop - location;
					}
				}
			}
		}
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
		if(checker >= 15){
			return true;
		}
		return false;
	}
	public boolean hasCrossing(int choice, String lineChoice){

		int checker = blockInfrastructure.get(GetBlock(choice, lineChoice));
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