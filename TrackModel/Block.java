public class Block{
    private String line;
    private char section;
    private int blockNum;
    private int length;
    private float grade;
    private int speedLimit;
    private String infraStructure;
    private boolean isUnderground;
    private String stationName;
    private boolean isStation;
    private boolean hasSwitch;
    private int switchPosition;
    private boolean hasRailwayCrossing;


    public Block(String newLine,char newSection, int newBlockNum, int newLength, float newGrade, int newSpeedLimit,String newInfrastructure){
        line = newLine;
        section=newSection;
        blockNum=newBlockNum;
        length=newLength;
        grade=newGrade;
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

    public int GetBlockNum(){
        return blockNum;
    }

    public int GetLength(){
        return length;
    }

    public String GetLine(){
        return line;
    }

    public char GetSection(){
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
    public int GetSwitchPosition(){
        return switchPosition;
    }
    public boolean GetHasRailwayCrossing(){
        return hasRailwayCrossing;
    }
}