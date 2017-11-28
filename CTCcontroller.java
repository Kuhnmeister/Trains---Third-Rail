public class CTCcontroller{
	public static void main(String[] args){
		CTCcontroller thisController = new CTCcontroller();
	}
	public CTCcontroller(){
		BBC thisBBC = new BBC();
	}
	public void receiveTrackData(HashMap<String, HashMap<String, ArrayList<Block>>> track){
		Tracking.receiveTrackData(track);
	}
}
