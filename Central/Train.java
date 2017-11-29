import java.util.Timer;
import java.util.TimerTask;
public class Train{
    private Block endingBlock;
    private Block currentBlock;
    private Block nextBlock;
    private int direction;
    private int trainNum;
    private double currentVelocity=5;
    private double positionOnBlock;
    private TrackModel theModel;
    private int updateTimeMS = 1000;
    private Timer updatePositionTimer;
    private boolean trainActive=true;
    private int authority=0;
    private boolean moveAtMaxSpeed=false;
    private Block prevBlock;
    private int trainLength;
    private boolean prevBlockOccupied=false;
    private boolean integrated=false;

    public Train(int newTrainNum,int newTrainLength,int newDirection,Block newCurrentBlock,Block newEndingBlock,TrackModel newModel) {
        trainNum = newTrainNum;
        direction = newDirection;
        currentBlock = newCurrentBlock;
        endingBlock=newEndingBlock;
        trainLength=newTrainLength;
        theModel = newModel;
        theModel.AddOccupied(currentBlock);
        currentBlock.SetIsOccupied(true);
        nextBlock = currentBlock.GetNextBlock(direction);
        updatePositionTimer=new Timer();
        updatePositionTimer.schedule(new TrainUpdateTimer(updateTimeMS,this),0,updateTimeMS);

    }
    public Train(int newTrainNum,int newTrainLength,int newDirection,Block newCurrentBlock,TrackModel newModel,boolean newIntegrated) {
        trainNum = newTrainNum;
        direction = newDirection;
        currentBlock = newCurrentBlock;
        theModel = newModel;
        trainLength=newTrainLength;
        theModel.AddOccupied(currentBlock);
        currentBlock.SetIsOccupied(true);
        nextBlock = currentBlock.GetNextBlock(direction);
        integrated=newIntegrated;

    }
    public Train(int newTrainNum,int newTrainLength,int newDirection,Block newCurrentBlock,TrackModel newModel) {
        trainNum = newTrainNum;
        direction = newDirection;
        currentBlock = newCurrentBlock;
        trainLength=newTrainLength;
        theModel = newModel;
        theModel.AddOccupied(currentBlock);
        currentBlock.SetIsOccupied(true);
        nextBlock = currentBlock.GetNextBlock(direction);
        updatePositionTimer=new Timer();
        updatePositionTimer.schedule(new TrainUpdateTimer(updateTimeMS,this),0,updateTimeMS);
    }

    public Block GetCurrentBlock(){
        return currentBlock;
    }
    private void MoveNextBlock(){
        if(integrated){
            if (!currentBlock.GetPowerFail() && !currentBlock.GetTrackCircuitFail() && !currentBlock.GetBrokenRail()) {
                prevBlock = currentBlock;
                prevBlockOccupied = true;
            }
            nextBlock = nextBlock.GetNextBlock(direction);
            currentBlock = nextBlock;
            if(currentBlock.IsDirectionChange()){
                if(direction==0){
                    direction=1;
                }else{
                    direction=0;
                }
            }
            if (!currentBlock.GetPowerFail() && !currentBlock.GetTrackCircuitFail() && !currentBlock.GetBrokenRail()) {
                theModel.AddOccupied(currentBlock);
                currentBlock.SetIsOccupied(true);
            }
                nextBlock = nextBlock.GetNextBlock(direction);

        }else {
            if (!currentBlock.GetPowerFail() && !currentBlock.GetTrackCircuitFail() && !currentBlock.GetBrokenRail()) {
                prevBlock = currentBlock;
                prevBlockOccupied = true;
            }
            if (currentBlock != endingBlock) {
                nextBlock = nextBlock.GetNextBlock(direction);
                currentBlock = nextBlock;
                if(currentBlock.IsDirectionChange()){
                    if(direction==0){
                        direction=1;
                    }else{
                        direction=0;
                    }
                }
                if (!currentBlock.GetPowerFail() && !currentBlock.GetTrackCircuitFail() && !currentBlock.GetBrokenRail()) {
                    theModel.AddOccupied(currentBlock);
                    currentBlock.SetIsOccupied(true);

                }
                nextBlock = nextBlock.GetNextBlock(direction);
            } else {
                updatePositionTimer.cancel();
                trainActive = false;
            }
        }

    }
    public boolean GetActive(){
        return trainActive;
    }
    public void SetVelocity(double newVelocity){
        System.out.println("New Train Velocity: "+newVelocity);
        currentVelocity=newVelocity;
    }
    public int GetTrainNum(){
        return trainNum;
    }

    public int GetDirection(){
        return direction;
    }
    public void UpdatePosition(int timeSinceLastUpdateMS){
        System.out.println("Train is updating");
        if(moveAtMaxSpeed) {
            positionOnBlock = positionOnBlock + currentVelocity * (double) (timeSinceLastUpdateMS / 1000);
            if (positionOnBlock > currentBlock.GetLength()) {
                positionOnBlock = positionOnBlock - currentBlock.GetLength();
                MoveNextBlock();
            }
            if(prevBlockOccupied){
                if(positionOnBlock>trainLength){
                    if(!prevBlock.GetPowerFail() && !prevBlock.GetTrackCircuitFail() && !prevBlock.GetBrokenRail()) {
                        theModel.RemoveOccupied(prevBlock);
                        prevBlock.SetIsOccupied(false);
                        System.out.println("Finally left block: "+prevBlock.GetBlockNum()+ " After Travelling: "+positionOnBlock);
                        prevBlockOccupied=false;

                    }
                }
            }

        }else{
            if(authority>=1){
                positionOnBlock = positionOnBlock + currentVelocity * (double) (timeSinceLastUpdateMS / 1000);
                if (positionOnBlock > currentBlock.GetLength()) {
                    positionOnBlock = positionOnBlock - currentBlock.GetLength();
                    MoveNextBlock();
                }
                if(prevBlockOccupied){
                    if(positionOnBlock>trainLength){
                        if(!prevBlock.GetPowerFail() && !prevBlock.GetTrackCircuitFail() && !prevBlock.GetBrokenRail()) {
                            theModel.RemoveOccupied(prevBlock);
                            prevBlock.SetIsOccupied(false);
                            System.out.println("Finally left block: "+prevBlock.GetBlockNum()+ " After Travelling: "+positionOnBlock);
                            prevBlockOccupied=false;

                        }
                    }
                }
            }
        }
    }
    public void UpdatePositionIntegrated(double travelledDistance){
        positionOnBlock = positionOnBlock + travelledDistance;
        if (positionOnBlock > currentBlock.GetLength()) {
            positionOnBlock = positionOnBlock - currentBlock.GetLength();
            MoveNextBlock();
        }
        if(prevBlockOccupied){
            if(positionOnBlock>trainLength){
                if(!prevBlock.GetPowerFail() && !prevBlock.GetTrackCircuitFail() && !prevBlock.GetBrokenRail()) {
                    theModel.RemoveOccupied(prevBlock);
                    prevBlock.SetIsOccupied(false);
                    prevBlockOccupied=false;
                }
            }
        }
    }
    public float GetUpdateTime(){
        return updateTimeMS;
    }
    public void WaysideInput(int newAuthority,boolean newMoving){
        moveAtMaxSpeed=newMoving;
        authority=newAuthority;
    }
    public void SetAuthority(int newAuthority){
        authority=newAuthority;
    }
    public int GetAuthority(){
        return authority;
    }
}
class TrainUpdateTimer extends TimerTask{
    int updateTime;
    Train thisTrain;
    public TrainUpdateTimer(int updateTime, Train thisTrain){
        this.updateTime=updateTime;
        this.thisTrain=thisTrain;
    }
    public void run(){
        System.out.println("Train update timer: "+updateTime);
        thisTrain.UpdatePosition(updateTime);
    }
}