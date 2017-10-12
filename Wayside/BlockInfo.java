package Controller;

//this object will house data for blocks like occupency, Light State, and Switch state
//this will be stored as bool values

public class BlockInfo {
	
	public Boolean occupy, light, switchState; 
	public BlockInfo(Boolean occ, Boolean lgt, Boolean swi) {
		//occ is occupied when true and empty when false
		//lgt is green when false, and red when true
		//swi is up when true, swi is down when false and NULL when it doesn't exist
		occupy = occ;
		light = lgt;
		switchState = swi;
		
	}
	
	public BlockInfo() {
		occupy = false;
		light = false;
		switchState = null; 
	}

}
