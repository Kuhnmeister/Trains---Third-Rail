public class Train{
    private Block currentBlock;
    private Block nextBlock;
    private int direction;
    private int trainNum;
    private float currentVelocity;
    private float positionOnBlock;
    private TrackModel theModel;
    public Train(int newTrainNum,int newDirection,Block newCurrentBlock,TrackModel newModel){
        trainNum=newTrainNum;
        direction=newDirection;
        currentBlock=newCurrentBlock;
        theModel=newModel;
        nextBlock=currentBlock.GetNextBlock(direction);

    }
    public Block GetCurrentBlock(){
        return currentBlock;
    }
    private void MoveNextBlock(){
        currentBlock.SetIsOccupied(false);
        currentBlock=nextBlock;
        currentBlock.SetIsOccupied(true);
        nextBlock=nextBlock.GetNextBlock(direction);

    }
    public void SetVelocity(float newVelocity){
        currentVelocity=newVelocity;
    }
    public void UpdatePosition(float timeSinceLastUpdate){
        positionOnBlock=positionOnBlock+currentVelocity*timeSinceLastUpdate;
        if(positionOnBlock>currentBlock.GetLength()){
            positionOnBlock=positionOnBlock-currentBlock.GetLength();
            MoveNextBlock();
            theModel.WaysideSendNewData();
        }
    }
}