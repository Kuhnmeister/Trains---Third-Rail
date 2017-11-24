package Controller;
//this is a framework for the engineer to write a PLC for the program
//It provides a base example

public class framePLC {
	private int Auth = 0;
	//each BlockInfo object has 4 fields occupied, light, switch state and line
	//since there will be only one wayside for track, the track field won't need to be used
	
	//BlockInfo.occupy will show if the block is occupied or not
	
	//light will show what state the light is; green is false and red is trues
	
	//switchState will show if there is a switch, it'll be null if there isn't a switch, and what position it's in(false is low, true is high)
	
	/*	
	 * Here is an example of a possible PLC;
	 * In this example the engineer wants the Authority to be max - 1
	 *  
	 public void GetAuthority(BlockInfo[] track, int currBlock) {
		for(int i = 0; i < track.length; i++) {
					for(int i = 0; i < track.length - blockNow; i++) {
			//if there is no train on the next block Authority is increased
			if(!(track[blockNow + i].occupy)) {
				Auth++;
			}else {
				break; //leave the for loop and return the calculated Authority
			}
		}
		}
		if() {
			return Auth - 1;
		}else{
			return Auth;	
		}
	 }
	 *
	 */
	
	//this method will were the engineer enters the PLC for Authority
	public int GetAuthority(BlockInfo[] track, int curBlock) {
		
		
		return Auth;
	}
	
}