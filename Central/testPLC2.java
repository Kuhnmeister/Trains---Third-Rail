import java.util.ArrayList;
//Ethan Shuffelbottom
//A 2nd test PLC that allows for minimal Authority
public class testPLC2 implements PLCinterface{
	//no arguments constructor
	public testPLC2()
	{
		System.out.println(this.getClass().getCanonicalName());
	}
	
	@Override
	public ArrayList<Integer> getAuth(int currentBlock, boolean direction, ArrayList<BlockInfo> track) {
		// TODO Auto-generated method stub
		ArrayList<Integer> testList = new ArrayList<Integer>();
		testList.add(currentBlock);
		if(!(track.get(currentBlock).occupancy()) && direction) {
			testList.add(track.get(currentBlock).blockNumber1());
		}else if(!(track.get(currentBlock).occupancy()) && !(direction)) {
			testList.add(track.get(currentBlock).blockNumber0());
		}
		return testList;
	}

	@Override
	public boolean decideCrossing(int currentBlock, ArrayList<BlockInfo> track) {
		// TODO Auto-generated method stub
		return true;
	}

}
