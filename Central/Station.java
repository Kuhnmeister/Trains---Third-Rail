//Zachery Blouse COE 1186 Fall 2017 - The Third Rail
import java.util.Random;
public class Station {
    private String stationName;

    private int numTickets=0;
    //Default Constructor
    public Station (String newName){
        stationName=newName;
    }

    //Returns this stations name
    public String GetStationName(){
        return stationName;
    }
    //Adds tickets to the internal count of tickets
    public void AddTickets(int newTickets){
        numTickets=numTickets+newTickets;
    }
    //Returns the total number of tickets
    public int GetTicketNumbers(){
       return numTickets;
    }
    //This is the function that creates the random number of people getting on the train.
    public int MakeTickets(){
        Random r = new Random();
        int low = 0;
        int high = 35;
        int result = r.nextInt(high-low) + low;
        return result;
    }

}