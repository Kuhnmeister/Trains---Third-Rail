//Zachery Blouse COE 1186 Fall 2017 - The Third Rail
public class Station {
    private String stationName;
    private int numTickets=0;

    public Station (String newName){
        stationName=newName;
    }


    public String GetStationName(){
        return stationName;
    }
    public void AddTickets(int newTickets){
        numTickets=numTickets+newTickets;
    }
    public int GetTicketNumbers(){
       return numTickets;
    }


}