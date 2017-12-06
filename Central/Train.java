import java.util.Timer;
import java.util.TimerTask;
import java.util.BitSet;
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
    private double authority=0;
    private boolean moveAtMaxSpeed=false;
    private Block prevBlock;
    private boolean realTrain=true;
    private int trainLength;
    private boolean prevBlockOccupied=false;
    private boolean integrated=false;
    private String line;
    public Train(){
        realTrain=false;
    }
    public boolean GetIsReal(){
        return realTrain;
    }
    public double GetVelocity(){
        return currentVelocity;
    }
    public Train(int newTrainNum,int newTrainLength,int newDirection,Block newCurrentBlock,Block newEndingBlock, String newLine, TrackModel newModel) {
        trainNum = newTrainNum;
        direction = newDirection;
        line = newLine;
        currentBlock = newCurrentBlock;
        endingBlock=newEndingBlock;
        trainLength=newTrainLength;
        theModel = newModel;
        theModel.AddOccupied(currentBlock);
        currentBlock.SetIsOccupied(true);
        nextBlock = currentBlock.GetNextBlock(direction);
        updatePositionTimer=new Timer();
        updatePositionTimer.schedule(new TrainUpdateTimer(updateTimeMS,this),0,updateTimeMS);
        realTrain=false;

    }
    public Train(int newTrainNum,int newTrainLength,int newDirection,Block newCurrentBlock,TrackModel newModel, String newLine,boolean newIntegrated) {
        trainNum = newTrainNum;
        direction = newDirection;
        currentBlock = newCurrentBlock;
        theModel = newModel;
        trainLength=newTrainLength;
        line=newLine;
        theModel.AddOccupied(currentBlock);
        currentBlock.SetIsOccupied(true);
        nextBlock = currentBlock.GetNextBlock(direction);
        integrated=newIntegrated;
        realTrain=true;

    }
    public Train(int newTrainNum,int newTrainLength,int newDirection,Block newCurrentBlock,TrackModel newModel,String newLine) {
        trainNum = newTrainNum;
        direction = newDirection;
        currentBlock = newCurrentBlock;
        trainLength=newTrainLength;
        realTrain=false;
        integrated=true;
        theModel = newModel;
        line=newLine;
        theModel.AddOccupied(currentBlock);
        currentBlock.SetIsOccupied(true);
        nextBlock = currentBlock.GetNextBlock(direction);
        updatePositionTimer=new Timer();
        updatePositionTimer.schedule(new TrainUpdateTimer(updateTimeMS,this),0,updateTimeMS);
    }
    public String GetLine(){
        return line;
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

            currentBlock = nextBlock;
            if(currentBlock.GetIsStation()){
                theModel.GenerateTickets(currentBlock.GetBlockNum(),line);
            }
            if(currentBlock.GetHasBeacon()){
                BitSet beaconMessage= currentBlock.GetBeaconData();
                theModel.ReportBeaconData(beaconMessage, trainNum);
            }
            nextBlock = currentBlock.GetNextBlock(direction);
            if(direction==0){
                if(currentBlock.IsDirectionChange1()){
                    direction=1;
                    System.out.println("Direction changed to 1 Next block: "+nextBlock.GetBlockNum());
                }
            }else{
                if(currentBlock.IsDirectionChange0()){
                    direction=0;
                }
            }
            if (!currentBlock.GetPowerFail() && !currentBlock.GetTrackCircuitFail() && !currentBlock.GetBrokenRail()) {
                theModel.AddOccupied(currentBlock);
                currentBlock.SetIsOccupied(true);
            }



        }else {

            if (!currentBlock.GetPowerFail() && !currentBlock.GetTrackCircuitFail() && !currentBlock.GetBrokenRail()) {
                prevBlock = currentBlock;
                prevBlockOccupied = true;
            }
            if (currentBlock != endingBlock) {

                currentBlock = nextBlock;
                if(currentBlock.GetIsStation()){
                    theModel.GenerateTickets(currentBlock.GetBlockNum(),line);
                }
                nextBlock = currentBlock.GetNextBlock(direction);
                if(direction==0){
                    if(currentBlock.IsDirectionChange1()){
                        direction=1;
                        System.out.println("Direction changed to 1 Next block: "+nextBlock.GetBlockNum());
                    }
                }else{
                    if(currentBlock.IsDirectionChange0()){
                        direction=0;
                    }
                }
                if (!currentBlock.GetPowerFail() && !currentBlock.GetTrackCircuitFail() && !currentBlock.GetBrokenRail()) {
                    theModel.AddOccupied(currentBlock);
                    currentBlock.SetIsOccupied(true);

                }

            } else {
                updatePositionTimer.cancel();
                currentBlock.SetIsOccupied(false);
                theModel.RemoveOccupied(currentBlock);
                trainActive = false;
                theModel.RemoveTrain(this);
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
        timeSinceLastUpdateMS=timeSinceLastUpdateMS*theModel.GetMultiplier();
        if(moveAtMaxSpeed) {
            positionOnBlock = positionOnBlock + currentVelocity * (double) (timeSinceLastUpdateMS / 1000);
            if (positionOnBlock > currentBlock.GetLength()) {
                positionOnBlock = positionOnBlock - currentBlock.GetLength();
                MoveNextBlock();
            }
            if(prevBlockOccupied){
                if(positionOnBlock>=trainLength){
                    if(!prevBlock.GetPowerFail() && !prevBlock.GetTrackCircuitFail() && !prevBlock.GetBrokenRail()) {
                        theModel.RemoveOccupied(prevBlock);
                        prevBlock.SetIsOccupied(false);
                        System.out.println("Finally left block: "+prevBlock.GetBlockNum()+ " After Travelling: "+positionOnBlock);
                        prevBlockOccupied=false;

                    }
                    if(currentBlock.GetToYard()){
                        if(currentBlock.GetYardSwitch()){
                            updatePositionTimer.cancel();
                            currentBlock.SetIsOccupied(false);
                            theModel.RemoveOccupied(currentBlock);
                            trainActive = false;

                            theModel.RemoveTrain(this);
                        }
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
                    if(positionOnBlock>=trainLength){
                        if(!prevBlock.GetPowerFail() && !prevBlock.GetTrackCircuitFail() && !prevBlock.GetBrokenRail()) {
                            theModel.RemoveOccupied(prevBlock);
                            prevBlock.SetIsOccupied(false);
                            System.out.println("Finally left block: "+prevBlock.GetBlockNum()+ " After Travelling: "+positionOnBlock);
                            prevBlockOccupied=false;
                        }
                        if(currentBlock.GetToYard()){
                            if(currentBlock.GetYardSwitch()){
                                updatePositionTimer.cancel();
                                currentBlock.SetIsOccupied(false);
                                theModel.RemoveOccupied(currentBlock);
                                trainActive = false;
                                theModel.RemoveTrain(this);
                            }
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
    public void SetAuthority(double newAuthority){
        authority=newAuthority;
    }
    public double GetAuthority(){
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
