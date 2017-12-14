//Zachery Blouse COE 1186 Fall 2017 - The Third Rail
import java.util.BitSet;
public class Block {

    private String line;
    private String section;
    private int blockNum;
    private double length;
    private float grade;
    private int speedLimit;
    private String infraStructure;
    private boolean isUnderground;
    private String stationName;
    private boolean isStation;
    private boolean hasSwitch;
    private boolean hasBeacon;
    private Beacon thisBeacon;
    private boolean switchPosition;
    private boolean maintainance = false;
    private boolean hasRailwayCrossing;
    private boolean crossingOn;
    private boolean isOccupied = false;
    private String lightColor = "Green";
    private boolean isBidirectional;
    private Block nextBlockDirection0;
    private Block nextBlockDirection1;
    private Block nextBlockSwitch;
    private int nextBlockDirection0Num;
    private int nextBlockDirection1Num;
    private int nextBlockSwitchNum;
    private boolean hasHeater = false;
    private boolean trackCircuitFail = false;
    private boolean powerFail = false;
    private boolean brokenRail = false;
    private boolean forceMajeureTrainPresence = false;
    private Station thisStation;
    private boolean directionChange0 = false;
    private boolean directionChange1 = false;
    private boolean fromYard;
    private boolean toYard;
    private boolean yardSwitch = false;
    //Constructor
    public Block(String newLine, String newSection, int newBlockNum, double newLength, float newGrade, int newSpeedLimit, boolean newIsBidirectional, int newNextBlock0, int newNextBlock1, int newSwitchBlock, String newInfrastructure) {
        line = newLine;

        section = newSection;
        blockNum = newBlockNum;
        System.out.println("New Block: " + blockNum + " on line: " + line);
        length = newLength;
        grade = newGrade;
        nextBlockDirection0Num = newNextBlock0;
        nextBlockDirection1Num = newNextBlock1;
        nextBlockSwitchNum = newSwitchBlock;
        isBidirectional = newIsBidirectional;
        speedLimit = newSpeedLimit;
        if (newInfrastructure.contains("Underground")) {
            isUnderground = true;
        } else {
            isUnderground = false;
        }
        if (newInfrastructure.contains("Direction0")) {
            directionChange0 = true;
        } else {
            directionChange0 = false;
        }
        if (newInfrastructure.contains("Direction1")) {
            directionChange1 = true;
        } else {
            directionChange1 = false;
        }
        if (newInfrastructure.contains("Beacon")) {
            hasBeacon = true;
            thisBeacon = new Beacon();
            String[] tempString = newInfrastructure.split(";");
            String beaconMessage = tempString[1];
            thisBeacon.SetMessageString(beaconMessage);
            System.out.println("There is a beacon at block " + blockNum + " message: " + thisBeacon.GetMessage());
        } else {
            hasBeacon = false;
        }
        if (newInfrastructure.contains("Station")) {
            isStation = true;
            String[] tempString = newInfrastructure.split(";");
            stationName = tempString[1];
            thisStation = new Station(stationName);
        } else {
            isStation = false;
            stationName = "No Station";
        }
        if (newInfrastructure.contains("Crossing")) {
            hasRailwayCrossing = true;
        } else {
            hasRailwayCrossing = false;
        }
        if (newInfrastructure.contains("Switch")) {
            hasSwitch = true;
        } else {
            hasSwitch = false;
        }
        if (newInfrastructure.contains("FromYard")) {
            hasSwitch = true;
            fromYard = true;
        } else {
            fromYard = false;
        }
        if (newInfrastructure.contains("ToYard")) {
            hasSwitch = true;
            toYard = true;
        } else {
            toYard = false;
        }
    }
    //Returns true if this is the block trains need to be on to be sent to the yard
    public boolean GetToYard() {
        return toYard;
    }
    //returns tre if this block has a switch to get to the yard
    public boolean GetYardSwitch() {
        return yardSwitch;
    }
    //Sets the position of the Yard Switch
    public void SetYardSwitch(boolean position) {
        yardSwitch = position;
    }
    //Returns true if this is the block that trains leave the yard on
    public boolean GetFromYard() {
        return fromYard;
    }
    //Returns the station object that is on this block
    public Station GetStation() {
        return thisStation;
    }
    //Returns the number of the block the train will travel to depending on the direction the train is travelling
    public Block GetNextBlock(int trainDirection) {

        if (switchPosition) {
            return nextBlockSwitch;
        } else {
            if (trainDirection == 0) {
                return nextBlockDirection0;
            } else {
                return nextBlockDirection1;
            }
        }
    }
    //Returns the number of this block
    public int GetBlockNum() {
        return blockNum;
    }
    //Returns true if this block changes the direction to 0
    public boolean IsDirectionChange0() {
        return directionChange0;
    }
    //returns true if this block changes the direction to 1
    public boolean IsDirectionChange1() {
        return directionChange1;
    }
    //Returns the length of this block
    public double GetLength() {
        return length;
    }
    //Returns the sting of the line this block is on
    public String GetLine() {
        return line;
    }
    //Returns the name of the section
    public String GetSection() {
        return section;
    }
    //Returns the grade
    public float GetGrade() {
        return grade;
    }
    //Returns the maximum safe speed of this block
    public int GetSpeedLimit() {
        return speedLimit;
    }
    //Returns the occupied status of this block
    public boolean GetIsOccupied() {
        return isOccupied;
    }
    //Returns true if this block is underground
    public boolean GetIsUnderground() {
        return isUnderground;
    }
    //Returns the string name of the station
    public String GetStationName() {
        return stationName;
    }
    //Returns true if this block has a station
    public boolean GetIsStation() {
        return isStation;
    }
    //returns true if this block has a switch
    public boolean GetHasSwitch() {
        return hasSwitch;
    }
    //Returns the boolean is the switch has moved from its original position
    public boolean GetSwitchPosition() {
        return switchPosition;
    }
    //Flips the switch
    public void FlipSwitch(boolean flip) {
        if (GetHasSwitch() && flip && !powerFail) {
            switchPosition = !switchPosition;
        } else if (toYard) {
            yardSwitch = !yardSwitch;
        }
    }
    //returns true if this block has a railway crossing
    public boolean GetHasRailwayCrossing() {
        return hasRailwayCrossing;
    }
    //Turn the crossing on and off
    public void SwitchCrossing() {
        if(hasRailwayCrossing) {
            crossingOn = !crossingOn;
            System.out.println("CrossingOn is now: " + crossingOn);
        }
    }
    //Returns iif the railroad crossing is on or not
    public boolean GetCrossingOn() {
            return crossingOn;
    }
    //Formats the block data for printing
    public String PrintBlock(){
        return(blockNum+","+isOccupied+","+lightColor+","+ grade+","+speedLimit+","+isUnderground+ ","+isStation+","+stationName+","+hasSwitch+","+switchPosition+","+hasRailwayCrossing+","+crossingOn);
    }
    //Returns true if this block is bidirectional
    public boolean GetIsBidirectional(){
        return isBidirectional;
    }
    //Returns true if this block has a heater
    public boolean GetHasHeater(){return hasHeater;}
    //Turns the heater on and off
    public void SetHasHeater(boolean newHasHeater){
        hasHeater=newHasHeater;
    }
    //Sets the block to be occupied
    public void SetIsOccupied(boolean newIsOccupied) {
        if(!powerFail && !trackCircuitFail && !brokenRail ) {
            isOccupied = newIsOccupied;
            System.out.println("Block Num: " + blockNum + " is now occipied: " + isOccupied);
        }
        forceMajeureTrainPresence=newIsOccupied;
    }
    //Returns the number of the next block if the train is moving in direction 0
    public int GetDirection0Num(){
        return nextBlockDirection0Num;
    }
    //Returns the number of the next block if the train is moving in direction 1
    public int GetDirection1Num(){
        return nextBlockDirection1Num;
    }
    //Returns the number of the next block if the switch is on
    public int GetSwitchNum(){
        return nextBlockSwitchNum;
    }
    //Sets the string value of the light
    public void SetLightColor(String newColor){
        if(!powerFail && (newColor.equals("Green")||newColor.equals("Red"))) {
            lightColor = newColor;
        }
    }
    //Sets the value of the next block in direciton 0
    public void SetDirection0Block(Block new0Block){
        nextBlockDirection0=new0Block;
    }
    //Sets the value of the next block in direction 1
    public void SetDirection1Block(Block new1Block){
        nextBlockDirection1=new1Block;
    }
    //Sets the value of the next block in the switch direction
    public void SetSwitchBlock(Block newSwitchBlock)
    {
        nextBlockSwitch=newSwitchBlock;
    }
    //Sets a power failure on
    public void SetPowerFail() {
        lightColor=null;
        isOccupied=false;

        powerFail=true;
    }
    //Sets a track circuit fail on
    public void SetTrackCircuitFail(){
        isOccupied=true;
        trackCircuitFail=true;
    }
    //Sets a broken rail
    public void SetBrokenRail(){
        isOccupied=true;
        brokenRail=true;
    }
    //Returns if the track has a circuit failure
    public boolean GetTrackCircuitFail(){

        return trackCircuitFail;
    }
    //returns true if the block has a power failure
    public boolean GetPowerFail(){
        return powerFail;
    }
    //returns true if the block has a broken rail
    public boolean GetBrokenRail(){

        return brokenRail;
    }
    //Removes all force majeure
    public void RemoveAllForceMajeure(){
        brokenRail=false;
        trackCircuitFail=false;
        powerFail=false;
        isOccupied=forceMajeureTrainPresence;
    }
    //Used to track if a train is present on a block with force majeure
    public boolean GetForceMajeureTrainPresence(){
        return forceMajeureTrainPresence;
    }
    //Returns true if this block has a beacon
    public boolean GetHasBeacon(){
        return hasBeacon;
    }
    //Returns the beacon object on this block
    public Beacon GetBeacon(){
        return thisBeacon;
    }
    //Returns the BitSet value of the beacon on this block
    public BitSet GetBeaconData(){
        return thisBeacon.GetMessage();
    }
    //Allows you to set the data in the beacon with a string
    public void SetBeaconMessageString(String newMessage){
        if(GetHasBeacon()){
            thisBeacon.SetMessageString(newMessage);
        }
    }
    //Turns the maintainance on
    public void SetMaintainance(){

        if(maintainance){
            maintainance=!maintainance;
            isOccupied=true;
        }else{
            maintainance=!maintainance;
            isOccupied=false;
        }
    }
    //Returns the maintainance value
    public boolean GetMaintainance(){
        return maintainance;
    }
    //Generates tickets at the station on this block
    public int GenerateTickets(){
        int newTickets= thisStation.MakeTickets();
        return newTickets;
    }
}