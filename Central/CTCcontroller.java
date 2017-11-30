import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Timer;
import java.util.List;

public class CTCcontroller{
	BBC thisBBC;
	public static void main(String[] args){
		CTCcontroller thisController = new CTCcontroller();
	}
	public CTCcontroller(Central central){
		thisBBC = new BBC(central);
	}
	public CTCcontroller(){
		thisBBC = new BBC();
	}
	public void receiveTrackData(HashMap<String, HashMap<String, ArrayList<Block>>> track){
		thisBBC.receiveTrackData(track);
	}

}
