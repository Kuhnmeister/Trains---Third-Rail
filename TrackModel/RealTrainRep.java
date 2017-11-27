public class RealTrainRep{
    private Block endingBlock;
    private Block currentBlock;
    private Block nextBlock;
    private int direction;
    private int trainNum;
    private TrackModel theModel;
    private boolean trainActive=true;
    private int authority=0;
    private float distanceSinceLastBlockSwitch;

    public RealTrainRep(int newTrainNum,int newDirection,Block newCurrentBlock,Block newEndingBlock,TrackModel newModel) {
        trainNum = newTrainNum;
        direction = newDirection;
        currentBlock = newCurrentBlock;
        endingBlock=newEndingBlock;
        theModel = newModel;
        theModel.AddOccupied(currentBlock);
        currentBlock.SetIsOccupied(true);
        nextBlock = currentBlock.GetNextBlock(direction);
    }

    public Block GetCurrentBlock(){
        return currentBlock;
    }
    private void MoveNextBlock(){
        if(!currentBlock.GetPowerFail() && !currentBlock.GetTrackCircuitFail() && !currentBlock.GetBrokenRail()) {
            theModel.RemoveOccupied(currentBlock);
            currentBlock.SetIsOccupied(false);
        }
        if(currentBlock != endingBlock) {
            currentBlock = nextBlock;
            if(!currentBlock.GetPowerFail() && !currentBlock.GetTrackCircuitFail() && !currentBlock.GetBrokenRail()) {
                theModel.AddOccupied(currentBlock);
                currentBlock.SetIsOccupied(true);

            }
            nextBlock = nextBlock.GetNextBlock(direction);
        }else{
            updatePositionTimer.cancel();
            trainActive=false;
        }

    }
    public boolean GetActive(){
        return trainActive;
    }
    public void SetVelocity(float newVelocity){
        currentVelocity=newVelocity;
    }
    public int GetTrainNum(){
        return trainNum;
    }
    public int GetDirection(){
        return direction;
    }
    public void UpdatePosition(float timeSinceLastUpdateMS){
        if(moveAtMaxSpeed) {
            positionOnBlock = positionOnBlock + currentVelocity * (float) (timeSinceLastUpdateMS / 1000);
            if (positionOnBlock > currentBlock.GetLength()) {
                positionOnBlock = positionOnBlock - currentBlock.GetLength();
                MoveNextBlock();
            }

        }
    }
    public void WaysideInput(int newAuthority,boolean newMoving){
        moveAtMaxSpeed=newMoving;
        authority=newAuthority;
    }
    public int GetAuthority(){
        return authority;
    }
    public void Update(float timePassed){
       UpdatePosition(timePassed);
    }
}


}