import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Timer;
import java.util.List;

public class Tracking{
	public static ArrayList<String> getLines(){
		ArrayList<String> red = new ArrayList<String>();
		red.add("red");
		return red;
	}
	public static int getBlockNums(int choice){
		return 69;
	}
	public static String getLineName(int choice){
		return "red";
	}
	public static ArrayList<String> getSection(int choice){
		ArrayList<String> bob = new ArrayList<String>();
		bob.add("F69");
		bob.add("G16");
		return bob;
	}
	public static boolean hasCrossing(int choice){
		if(choice == 0){
			return true;
		}
		else{
			return true;
		}
	}
	public static boolean isUnderground(int choice){
		if(choice == 0){
			return false;
		}
		else{
			return true;
		}
	}
	public static boolean hasSwitch(int choice){
		if(choice == 0){
			return true;
		}
		else{
			return true;
		}
	}	
}