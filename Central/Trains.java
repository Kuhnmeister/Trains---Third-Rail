import java.util.*;

public class Trains{
	protected static int numOfTrains = 0;
	protected static String[] greenStations = new String[18];
	protected static String[] redStations = new String[8];
	String trainName;
	int id;
	double trainSpeed;
	int trainAuthority;
	ArrayList<String> trainStops = new ArrayList<String>();
	int stopCount= 0;
	String departTime;
	String amPm;
	int location;
	String line;
	int trainLength;
	int numOfTickets;
	
	public Trains(){
		numOfTrains++; 
		trainName = "Train" + Integer.toString(numOfTrains);
		id = numOfTrains;
		trainSpeed = 0;
		trainAuthority = 3;
		trainLength = 1;
	}
	public Trains(double Speed){
		numOfTrains++;
		trainName = "Train" + Integer.toString(numOfTrains);
		trainSpeed = Speed;
		trainAuthority = 3;
	}
	public Trains(int Authority){
		numOfTrains++;
		trainName = "Train" + Integer.toString(numOfTrains);
		trainSpeed = 0;
		trainAuthority = Authority;
	}
	public Trains(double Speed, int Authority){
		numOfTrains++;
		trainName = "Train" + Integer.toString(numOfTrains);
		trainSpeed = Speed;
		trainAuthority = Authority;
	}
	public int getId(){
		return id;
	}
	public void setLength(int length){
		trainLength = length;
	}
	public int getLength(){
		return trainLength;
	}
	public void setSpeed(double speed){
		trainSpeed = speed;

	}
	public String getSpeed(){
		String speed = Double.toString(trainSpeed);
		return speed;
	}
	public Double getSpeedDouble(){
		return trainSpeed;
	}
	public void setDeparture(String depart){
		departTime = depart.substring(0,2);
		amPm = depart.substring(2,4);
	}
	public String getDepartureTime(){
		return departTime;
	}
	public String getDepartureHalf(){
		return amPm;
	}
	public void setAuthority(int authority){
		trainAuthority = authority;
	}
	public String getAuthority(){
		String authority = Integer.toString(trainAuthority);
		return authority;
	}
	public void createStop(String newStop){
		trainStops.add(stopCount, newStop);
		stopCount++;
	}
	public void deleteStop(String stop){
		int k = trainStops.indexOf(stop);
		trainStops.set(k, null);
		int i = 0;
		ArrayList<String> newTrainStops = new ArrayList<String>();
		boolean notFinished = true;
		while(notFinished){
			if(trainStops.get(i) != null){
				newTrainStops.add(trainStops.get(i));
			}
			if(i == trainStops.size()-1){
				notFinished = false;
				stopCount -= 1;
			}
			i++;
		}
		trainStops = newTrainStops;
		/*for(int i = 0; i < stopCount; i++){
			if(stop == trainStops.get(i)){
				System.out.println(trainStops.get(i) + "cancer");
				k = i;
			}
		}
		for(int j = k; j < stopCount; j++){
			trainStops.set(j-1, trainStops.get(j));
			if(j == stopCount-1){
				trainStops.set(j, null);
			}
			System.out.println(trainStops.get(j-1) + "cooooncer" );
		}
		*/
		
	}
	public boolean hasSchedule(){
		return (!(trainStops.isEmpty()));
	}
	public void sendToYard(int yardBlock){
		for(int i = 0; i < trainStops.size(); i++){
			trainStops.add(i, null);
		}
		trainStops.add(0,Integer.toString(yardBlock));
	}
	public String[] getSchedule(){
		String[] trainSchedule = new String[stopCount];
		for(int i = 0; i < stopCount; i++){
			trainSchedule[i] = trainStops.get(i);
		}
		return trainSchedule;
	}
	public void setLocation(int block){
		location = block;
	}
	public int GetTickets(){
		return numOfTickets;
	}
	public void AddTickets(int newTickets){
		System.out.println(newTickets);
		numOfTickets += newTickets;
	}
	public String getLine(){
		return line;
	}
	public int getLocation(){
		return location;
	}
	public void setLine(String lineChoose){
		line = lineChoose;
	}
}