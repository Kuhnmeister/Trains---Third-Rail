import java.util.Timer;
import java.util.TimerTask;
public class Train{
    private Block currentBlock;
    private Block nextBlock;
    private int direction;
    private int trainNum;
    private float currentVelocity=5;
    private float positionOnBlock;
    private TrackModel theModel;
    private int updateTimeMS = 1000;

    public Train(int newTrainNum,int newDirection,Block newCurrentBlock,TrackModel newModel) {
        trainNum = newTrainNum;
        direction = newDirection;
        currentBlock = newCurrentBlock;
        theModel = newModel;
        nextBlock = currentBlock.GetNextBlock(direction);
        Timer updatePositionTimer=new Timer();
        updatePositionTimer.schedule(new TrainUpdateTimer(updateTimeMS,this),0,updateTimeMS);
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
    public void UpdatePosition(int timeSinceLastUpdateMS){
        positionOnBlock=positionOnBlock+currentVelocity*(float)(timeSinceLastUpdateMS/1000);
        if(positionOnBlock>currentBlock.GetLength()){
            positionOnBlock=positionOnBlock-currentBlock.GetLength();
            MoveNextBlock();
            theModel.WaysideSendNewData();
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