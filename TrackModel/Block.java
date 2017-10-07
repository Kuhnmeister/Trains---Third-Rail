public class Block{
    private String line;
    private char section;
    private int blockNum;
    private int length;
    private float grade;
    private int speedLimit;
    private boolean isUnderground;
    private String infraStructure;
    private boolean isUnderground;
    private boolean hasSwitch;
    private int switchPosition;

    public Block(int newBlockNum, int newLength, int newSomething){
            blockNum=newBlockNum;
            length=newLength;
            something=newSomething;
    }



    public int GetBlockNum(){
        return blockNum;
    }
    public int GetLength(){
        return length;
    }
    public int GetSomething(){
        return something;
    }
}