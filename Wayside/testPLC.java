package Controller;

import java.util.ArrayList;

//this is a test PLC to try loading the a new PLC

public class testPLC implements PLCinterface {
	

	//no arguments constructor
	public testPLC()
	{
		System.out.println(this.getClass().getCanonicalName());
	}
	
	@Override
	public int getAuth(int currentBlock, boolean direction, ArrayList<BlockInfo> track) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean decideCrossing(int currentBlock, ArrayList<BlockInfo> track) {
		// TODO Auto-generated method stub
		return false;
	}

}
