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

    public boolean GetToYard() {
        return toYard;
    }

    public boolean GetYardSwitch() {
        return yardSwitch;
    }

    public void SetYardSwitch(boolean position) {
        yardSwitch = position;
    }

    public boolean GetFromYard() {
        return fromYard;
    }

    public Station GetStation() {
        return thisStation;
    }

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

    public int GetBlockNum() {
        return blockNum;
    }

    public boolean IsDirectionChange0() {
        return directionChange0;
    }

    public boolean IsDirectionChange1() {
        return directionChange1;
    }

    public double GetLength() {
        return length;
    }

    public String GetLine() {
        return line;
    }

    public String GetSection() {
        return section;
    }

    public float GetGrade() {
        return grade;
    }

    public static void main(String[] args) {
        System.out.println("Ran the main");
    }

    public int GetSpeedLimit() {
        return speedLimit;
    }

    public boolean GetIsOccupied() {
        return isOccupied;
    }

    public boolean GetIsUnderground() {
        return isUnderground;
    }

    public String GetStationName() {
        return stationName;
    }

    public boolean GetIsStation() {
        return isStation;
    }

    public boolean GetHasSwitch() {
        return hasSwitch;
    }

    public boolean GetSwitchPosition() {
        return switchPosition;
    }

    public void FlipSwitch(boolean flip) {
        if (GetHasSwitch() && flip && !powerFail) {
            switchPosition = !switchPosition;
        } else if (toYard) {
            yardSwitch = !yardSwitch;
        }
    }

    public boolean GetHasRailwayCrossing() {
        return hasRailwayCrossing;
    }

    public void SwitchCrossing() {
        if(hasRailwayCrossing) {
            crossingOn = !crossingOn;
            System.out.println("CrossingOn is now: " + crossingOn);
        }
    }

    public boolean GetCrossingOn() {
            return crossingOn;
    }
    public String PrintBlock(){
        return(blockNum+","+isOccupied+","+lightColor+","+ grade+","+speedLimit+","+isUnderground+ ","+isStation+","+stationName+","+hasSwitch+","+switchPosition+","+hasRailwayCrossing+","+crossingOn);
    }
    public boolean GetIsBidirectional(){
        return isBidirectional;
    }
    public boolean GetHasHeater(){return hasHeater;}
    public void SetHasHeater(boolean newHasHeater){
        hasHeater=newHasHeater;
    }
    public void SetIsOccupied(boolean newIsOccupied) {
        if(!powerFail && !trackCircuitFail && !brokenRail ) {
            isOccupied = newIsOccupied;
            System.out.println("Block Num: " + blockNum + " is now occipied: " + isOccupied);
        }
        forceMajeureTrainPresence=newIsOccupied;
    }
    public int GetDirection0Num(){
        return nextBlockDirection0Num;
    }
    public int GetDirection1Num(){
        return nextBlockDirection1Num;
    }
    public int GetSwitchNum(){
        return nextBlockSwitchNum;
    }
    public void SetLightColor(String newColor){
        if(!powerFail && (newColor.equals("Green")||newColor.equals("Red"))) {
            lightColor = newColor;
        }
    }
    public void SetDirection0Block(Block new0Block){
        nextBlockDirection0=new0Block;
    }
    public void SetDirection1Block(Block new1Block){
        nextBlockDirection1=new1Block;
    }
    public void SetSwitchBlock(Block newSwitchBlock)
    {
        nextBlockSwitch=newSwitchBlock;
    }
    public void SetPowerFail() {
        lightColor=null;
        isOccupied=false;

        powerFail=true;
    }
    public void SetTrackCircuitFail(){
        isOccupied=true;
        trackCircuitFail=true;
    }
    public void SetBrokenRail(){
        isOccupied=true;
        brokenRail=true;
    }
    public boolean GetTrackCircuitFail(){

        return trackCircuitFail;
    }
    public boolean GetPowerFail(){
        return powerFail;
    }
    public boolean GetBrokenRail(){

        return brokenRail;
    }
    public void RemoveAllForceMajeure(){
        brokenRail=false;
        trackCircuitFail=false;
        powerFail=false;
        isOccupied=forceMajeureTrainPresence;
    }
    public boolean GetForceMajeureTrainPresence(){
        return forceMajeureTrainPresence;
    }
    public boolean GetHasBeacon(){
        return hasBeacon;
    }
    public Beacon GetBeacon(){
        return thisBeacon;
    }
    public BitSet GetBeaconData(){
        return thisBeacon.GetMessage();
    }
    public void SetBeaconMessageString(String newMessage){
        if(GetHasBeacon()){
            thisBeacon.SetMessageString(newMessage);
        }
    }
    public void SetMaintainance(){

        if(maintainace){
            maintainance=!maintainance;
            isOccupied=true;
        }else{
            maintainance=!maintainance;
            isOccupied=false;
        }
    }
    public boolean GetMaintainance(){
        return maintainance;
    }
    public int GenerateTickets(){
        int newTickets= thisStation.MakeTickets();
        return newTickets;
    }
}