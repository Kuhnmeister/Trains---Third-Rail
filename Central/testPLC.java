import java.util.ArrayList;
//Ethan Shuffelbottom
//this is a test PLC to try loading the a new PLC
//view this as an emergency PLC, stops trains from moving and turns on all crossings
public class testPLC implements PLCinterface {
	

	//no arguments constructor
	public testPLC()
	{
		System.out.println(this.getClass().getCanonicalName());
	}
	
	@Override
	public ArrayList<Integer> getAuth(int currentBlock, boolean direction, ArrayList<BlockInfo> track) {
		// TODO Auto-generated method stub
		ArrayList<Integer> testList = new ArrayList<Integer>();
		testList.add(currentBlock);
		return testList;
	}

	@Override
	public boolean decideCrossing(int currentBlock, ArrayList<BlockInfo> track) {
		// TODO Auto-generated method stub
		return true;
	}

}
