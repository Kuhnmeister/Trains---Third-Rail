//Zachery Blouse COE 1186 Fall 2017 - The Third Rail
public class Block{
    private String line;
    private String section;
    private int blockNum;
    private int length;
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
    private boolean hasRailwayCrossing;
    private boolean isOccupied=false;
    private String lightColor="Green";
    private boolean isBidirectional;
    private Block nextBlockDirection0;
    private Block nextBlockDirection1;
    private Block nextBlockSwitch;
    private int nextBlockDirection0Num;
    private int nextBlockDirection1Num;
    private int nextBlockSwitchNum;
    private boolean hasHeater=false;
    private boolean trackCircuitFail=false;
    private boolean powerFail=false;
    private boolean brokenRail=false;
    private boolean forceMajeureTrainPresence=false;
    private Station thisStation;

    public Block(String newLine,String newSection, int newBlockNum, int newLength, float newGrade, int newSpeedLimit, boolean newIsBidirectional,int newNextBlock0,int newNextBlock1,int newSwitchBlock,String newInfrastructure){
        line = newLine;
        section=newSection;
        blockNum=newBlockNum;
        length=newLength;
        grade=newGrade;
        nextBlockDirection0Num=newNextBlock0;
        nextBlockDirection1Num=newNextBlock1;
        nextBlockSwitchNum=newSwitchBlock;
        isBidirectional=newIsBidirectional;
        speedLimit=newSpeedLimit;
        if(newInfrastructure.contains("Underground")){
            isUnderground=true;
        }else{
            isUnderground=false;
        }
        if(newInfrastructure.contains("Beacon")){
            hasBeacon=true;
            thisBeacon=new Beacon();
            System.out.println("There is a beacon at block "+blockNum);
        }else{
            hasBeacon=false;
        }
        if(newInfrastructure.contains("Station")){
            isStation=true;
            String[] tempString=newInfrastructure.split(";");
            stationName=tempString[1];
            thisStation = new Station(stationName);
        }else{
            isStation=false;
            stationName="No Station";
        }
        if(newInfrastructure.contains("Crossing")){
            hasRailwayCrossing=true;
        }else{
            hasRailwayCrossing=false;
        }
        if(newInfrastructure.contains("Switch")){
            hasSwitch=true;
        }else{
            hasSwitch=false;
        }
    }

    public Station GetStation(){
        return thisStation;
    }
    public Block GetNextBlock(int trainDirection){

        if(switchPosition){
            return nextBlockSwitch;
        }else{
            if(trainDirection==0){
                return nextBlockDirection0;
            }else{
                return nextBlockDirection1;
            }
        }
    }
    public int GetBlockNum(){
        return blockNum;
    }

    public int GetLength(){
        return length;
    }

    public String GetLine(){
        return line;
    }

    public String GetSection(){
        return section;
    }

    public float GetGrade(){
        return grade;
    }
    public static void main(String[] args){
        System.out.println("Ran the main");
    }

    public int GetSpeedLimit(){
        return speedLimit;
    }
    public boolean GetIsOccupied(){
        return isOccupied;
    }
    public boolean GetIsUnderground(){
        return isUnderground;
    }
    public String GetStationName(){
        return stationName;
    }
    public boolean GetIsStation(){
        return isStation;
    }
    public boolean GetHasSwitch(){
        return hasSwitch;
    }
    public boolean GetSwitchPosition(){
        return switchPosition;
    }
    public void FlipSwitch(boolean flip){
        if(GetHasSwitch() && flip && !powerFail){
            switchPosition=!switchPosition;
        }
    }
    public boolean GetHasRailwayCrossing(){
        return hasRailwayCrossing;
    }
    public String PrintBlock(){
        return(blockNum+","+isOccupied+","+lightColor+","+ grade+","+speedLimit+","+isUnderground+ ","+isStation+","+stationName+","+hasSwitch+","+switchPosition+","+hasRailwayCrossing);
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
    public void SetBeaconMessageString(String newMessage){
        if(GetHasBeacon()){
            thisBeacon.SetMessageString(newMessage);
        }
    }
}