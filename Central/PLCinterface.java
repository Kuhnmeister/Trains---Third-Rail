import java.util.ArrayList;

public interface PLCinterface {
	//this method should return the block number, as an integer, of the last free block to move to
	ArrayList<Integer> getAuth(int currentBlock, boolean direction, ArrayList<BlockInfo> track);
	
	//this should return a boolean to indicate what state a crossing should be in
	boolean decideCrossing(int currentBlock, ArrayList<BlockInfo> track);
	
}
