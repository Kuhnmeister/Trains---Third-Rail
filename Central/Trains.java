public class Trains{
	protected static int numOfTrains = 0;
	protected static String[] greenStations = new String[18];
	protected static String[] redStations = new String[8];
	String trainName;
	int id;
	double trainSpeed;
	int trainAuthority;
	String[] trainStops = new String[26];
	int stopCount= 0;
	String departTime;
	String amPm;
	int location;
	String line;
	
	public Trains(){
		numOfTrains++; 
		trainName = "Train" + Integer.toString(numOfTrains);
		trainSpeed = 0;
		trainAuthority = 3;
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
		id = numOfTrains;
		return id;
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
		System.out.println(departTime);
		amPm = depart.substring(2,4);
		System.out.println(amPm);
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
		trainStops[stopCount] = newStop;
		stopCount++;
	}
	public boolean hasSchedule(){
		return (!(trainStops[0] == null));
	}
	public String[] getSchedule(){
		String[] trainSchedule = new String[stopCount];
		for(int i = 0; i < stopCount; i++){
			trainSchedule[i] = trainStops[i];
		}
		return trainSchedule;
	}
	public void setLocation(int block){
		location = block;
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