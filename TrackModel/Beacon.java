import java.util.BitSet;
public class Beacon{
    private BitSet message = new BitSet(32);
    public BitSet GetMessage(){
        return message;
    }
    public void SetMessage(BitSet newMessage){
        message = newMessage;
    }
}