import java.util.Timer;
import java.util.TimerTask;
public class Train{
    private Block endingBlock;
    private Block currentBlock;
    private Block nextBlock;
    private int direction;
    private int trainNum;
    private float currentVelocity=5;
    private float positionOnBlock;
    private TrackModel theModel;
    private int updateTimeMS = 1000;
    private Timer updatePositionTimer;

    public Train(int newTrainNum,int newDirection,Block newCurrentBlock,Block newEndingBlock,TrackModel newModel) {
        trainNum = newTrainNum;
        direction = newDirection;
        currentBlock = newCurrentBlock;
        endingBlock=newEndingBlock;
        theModel = newModel;
        theModel.AddOccupied(currentBlock);
        nextBlock = currentBlock.GetNextBlock(direction);
        updatePositionTimer=new Timer();
        updatePositionTimer.schedule(new TrainUpdateTimer(updateTimeMS,this),0,updateTimeMS);
    }

    public Block GetCurrentBlock(){
        return currentBlock;
    }
    private void MoveNextBlock(){
        theModel.RemoveOccupied(currentBlock);
        currentBlock.SetIsOccupied(false);
        if(currentBlock != endingBlock) {
            currentBlock = nextBlock;
            theModel.AddOccupied(currentBlock);
            currentBlock.SetIsOccupied(true);
            nextBlock = nextBlock.GetNextBlock(direction);
        }else{
            updatePositionTimer.cancel();
        }

    }
    public void SetVelocity(float newVelocity){
        currentVelocity=newVelocity;
    }
    public void UpdatePosition(int timeSinceLastUpdateMS){
        positionOnBlock=positionOnBlock+currentVelocity*(float)(timeSinceLastUpdateMS/1000);
        if(positionOnBlock>currentBlock.GetLength()){
            positionOnBlock=positionOnBlock-currentBlock.GetLength();
            MoveNextBlock();
        }
    }
    public float GetUpdateTime(){
        return updateTimeMS;
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
        thisTrain.UpdatePosition(updateTime);
    }
}