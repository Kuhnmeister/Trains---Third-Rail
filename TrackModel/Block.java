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
        if(newInfrastructure.contains("Station")){
            isStation=true;
            String[] tempString=newInfrastructure.split(";");
            stationName=tempString[1];
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

    public int GetSpeedLimit(){
        return speedLimit;
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
    public boolean GetHasRailwayCrossing(){
        return hasRailwayCrossing;
    }
    public String PrintBlock(){
        return(line+","+section+","+blockNum+","+isOccupied+","+lightColor+","+ grade+","+speedLimit+","+isUnderground+ ","+isStation+","+stationName+","+hasSwitch+","+hasRailwayCrossing);
    }
    public boolean GetIsBidirectional(){
        return isBidirectional;
    }
    public void SetIsOccupied(boolean newIsOccupied){
        isOccupied=newIsOccupied;
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
}