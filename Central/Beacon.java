//Zachery Blouse COE 1186 Fall 2017 - The Third Rail
import java.util.BitSet;
public class Beacon{
    private BitSet message = new BitSet(32);
    //returns the stored message
    public BitSet GetMessage(){
        return message;
    }
    //Allows the message to be changed
    public void SetMessage(BitSet newMessage){
        message = newMessage;
    }
    //Allows the bits to be loaded in as a string and then converted to the bitset format.
    public void SetMessageString(String newMessage){
        message = new BitSet(32);
        int len = newMessage.length();
        for (int i = len-1; i >= 0; i--) {
            if (newMessage.charAt(i) == '1') {
               message.set(len-i-1);
            }
        }
        System.out.println(message);
    }
}