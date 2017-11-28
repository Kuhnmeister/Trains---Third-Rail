import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Timer;
import java.util.List;

public class BBC{
	public static void main(String args[]){
		BBC thisBBC = new BBC();
	}
	
	public BBC(){
		//main window variables
		JFrame window = new JFrame( "Centralized Traffic Control" );
		Container content;
		
		//panel variables
		JPanel panelRight = new JPanel();
		JPanel panelLeft = new JPanel();
		JPanel panelLeftBottom = new JPanel();
		
		//further panel breakdown
		JPanel panelBL1 = new JPanel();
		JPanel panelBL2 = new JPanel();
		JPanel panelBL3 = new JPanel();
		JPanel panelBL4 = new JPanel();
		JPanel panelBL5 = new JPanel();
		JPanel panelBL6 = new JPanel();
		JPanel panelBL7 = new JPanel();
		JPanel panelBL8 = new JPanel();
		JPanel panelBL9 = new JPanel();
		JPanel panelBL10 = new JPanel();
		JPanel panelBL11 = new JPanel();
		JPanel panelBL12 = new JPanel();
		
		int ticketSales = 0;
		Label thruPut = new Label("Ticket Sales: " + ticketSales);
		int[] autoManState = new int[1];
		autoManState[0] = 0;
		JComboBox trainSelect = new JComboBox();
		JComboBox trainChoice = new JComboBox();
		
		int[] trainCount = new int[1];
		trainCount[0] = 0;
		int[] trainListed = new int[1];
		trainListed[0] = 0;	
		//clock creation		
		ArrayList<Trains> trainList = new ArrayList<Trains>();
		String[] amPm = new String[1];
		amPm[0] = "am";
		Timer[] currentTimer = new Timer[1];
		int[] timeConstant = new int[1];
		timeConstant[0] = 1000;
		int[] time = new int[1];
		time[0] = 90000;
		Timer t = new Timer();
		JLabel clock = new JLabel();
		ButtonGroup speedChoice = new ButtonGroup();
		JRadioButton none = new JRadioButton("1x");
		JRadioButton ten = new JRadioButton("10x");
		JRadioButton hundred = new JRadioButton("100x");
		String autoSwitch = "Manual";
		JToggleButton autoMan = new JToggleButton(autoSwitch);
		JButton create = new JButton("Create Train");
		ArrayList<JFrame> trainWindow = new ArrayList<JFrame>();
		int[] windowNum = new int[1];
		windowNum[0] = 0;
		JButton dispatch = new JButton("Dispatch");
		//Constructing the window
		content = window.getContentPane();
		content.setLayout(new GridLayout(1,2));
		panelRight.setLayout(new GridLayout(1, 2));
		panelLeftBottom.setLayout(new GridLayout(1, 1));


		//content.add(panelBottomRight, 1);
		
		//adding components
		ImageIcon track = new ImageIcon("trackmodel.png");


		
		List<String> list = new ArrayList<String>();
		String[] file = new String[1];
		JButton fileOpen = new JButton("Schedule File");

		

		JLabel trainChoose = new JLabel("Train Selection");

		
		//schedule upload functionality

		
		//schedule view functionality
		ArrayList<Integer> scheduleOpened = new ArrayList<Integer>();
		JButton scheduleView = new JButton("View Schedule");
		List<JLabel> trainViews = new ArrayList<JLabel>();
		List<JLabel> scheduleTexts = new ArrayList<JLabel>();
		JLabel[] firstView = new JLabel[1];
		JLabel[] firstText = new JLabel[1];		
		int[] jlabelNum = new int[1];
		jlabelNum[0] = 0;
		
		//track interactivity functionality
		JComboBox line = new JComboBox();
		
		

		
		
		
		
		
		
		TimerTask clockRun = new TimerTask(){
			public void run(){
				if((time[0]/10000) == 12){
					if((time[0]/100)%100 == 59){
						if(time[0]%100 == 59){
							time[0] = (time[0] = 9999);
						}
					}
				}
				if((time[0]/100)%100 == 59){
					if(time[0]%100 == 59){
						time[0] = (time[0]+4040);
						if((time[0]/10000) == 11){
							if(amPm[0] == "am"){
								amPm[0] = "pm";
							}
							else{
								amPm[0] = "am";
							}
						}
					}
				}
				else if(time[0]%100 == 59){
					time[0] = (time[0]+40);
				}
				time[0]++;
				
				if((time[0]/100)%100 < 10){
					if(time[0]%100 < 10){
						clock.setText(""+((time[0]/10000))+":"+"0"+((time[0]/100)%100)+":"+"0"+(time[0]%100)+" "+amPm[0]);
					}
					else{
						clock.setText(""+((time[0]/10000))+":"+"0"+((time[0]/100)%100)+":"+(time[0]%100)+" "+amPm[0]);
					}
				}
				else if(time[0]%100 < 10){
					clock.setText(""+((time[0]/10000))+":"+((time[0]/100)%100)+":"+"0"+(time[0]%100)+" "+amPm[0]);
				}
				else{
					clock.setText(""+((time[0]/10000))+":"+((time[0]/100)%100)+":"+(time[0]%100)+" "+amPm[0]);
				}
				if(autoManState[0] == 1 ){	
					if(trainListed[0] != trainCount[0]){				
						int diff = trainCount[0] - trainListed[0];
						for(int x = 0; x < diff; x++){
							trainListed[0]++;
							trainSelect.addItem("Train " + Integer.toString(trainListed[0]));
							trainChoice.addItem("Train " + Integer.toString(trainListed[0]));
						}
					}
					if(!trainList.isEmpty()){
						for(int i = 0; i < trainList.size(); i++){
							if(trainList.get(i).hasSchedule()){
								String dispatchTime = new String(trainList.get(i).getDepartureTime());
								String half = new String(trainList.get(i).getDepartureHalf());
								if(Integer.parseInt(dispatchTime) == time[0]/10000){
									if(half.equals(amPm[0])){
										trainList.get(i).setSpeed(55);
									}
								}
							}
						}
					}
				}
			}
		};
		t.schedule(clockRun, timeConstant[0], timeConstant[0]);
		//line view variables
	
		none.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(t != null){
					Purge(t);
				}
				if(currentTimer[0] != null){
					Purge(currentTimer[0]);
				}
				currentTimer[0] = OneTimes(clockRun, timeConstant, time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice);
			}
		});
		ten.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(t != null){
					Purge(t);
				}
				if(currentTimer[0] != null){
					Purge(currentTimer[0]);
				}
				currentTimer[0] = TenTimes(clockRun, timeConstant, time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice);
			}
		});
		hundred.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(t != null){
					Purge(t);
				}
				if(currentTimer[0] != null){
					Purge(currentTimer[0]);
				}
				currentTimer[0] = HundredTimes(clockRun, timeConstant, time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice);
			}
		});
		//auto/manual button creation

		ItemListener autoListener = new ItemListener() {
			public void itemStateChanged(ItemEvent autoListener) {
				if(autoManState[0] == 0){
					autoManState[0] = 1;
				}
				else{
					autoManState[0] = 0;
				}
				String temp;
				int state = autoListener.getStateChange();
				if (state == autoListener.SELECTED) {
					autoMan.setText("Automatic"); 
				} 
				else {
					autoMan.setText("Manual");
				}
			}
		};
		autoMan.addItemListener(autoListener);
		
		//Building functionality for train data viewing/selecting		

		create.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(autoManState[0] == 0){
					if(trainListed[0] == trainCount[0]){		
						trainList.add(new Trains());
						trainCount[0] += 1;
						trainListed[0]++;
						trainSelect.addItem("Train " + Integer.toString(trainCount[0]));
						trainChoice.addItem("Train " + Integer.toString(trainCount[0]));
					}
					else{
						trainListed[0]++;
						trainSelect.addItem("Train " + Integer.toString(trainListed[0]));
						trainChoice.addItem("Train " + Integer.toString(trainListed[0]));
					}
				}
			}
		});

		dispatch.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(autoManState[0] == 0){
					if(!trainList.isEmpty()){
						if(trainList.get(trainSelect.getSelectedIndex()).getSpeedDouble() == 0.0){
						//fix for all possible lines
							if(trainList.get(trainSelect.getSelectedIndex()).getLine() == "Red ")
							{
								trainList.get(trainSelect.getSelectedIndex()).setLocation(96);
							}
							else{
								trainList.get(trainSelect.getSelectedIndex()).setLocation(95);
							}
							System.out.println(trainList.get(trainSelect.getSelectedIndex()).getLocation());
							System.out.println(trainList.get(trainSelect.getSelectedIndex()).getLine());
							trainList.get(trainSelect.getSelectedIndex()).setSpeed(55.0);
						}
					}
				}
			}
		});
		

		trainSelect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int open = trainSelect.getSelectedIndex();
				createFrame(trainWindow, windowNum, trainList, open);
			}
		});
		


		fileOpen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Blah.print();
				String fileLine;
				JFileChooser schedule = new JFileChooser();
				FileNameExtensionFilter text = new FileNameExtensionFilter("Text files", "txt");
				schedule.setFileFilter(text);
				int returnVal = schedule.showOpenDialog(schedule);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					file[0] = schedule.getSelectedFile().getName();
				}
				try{
					BufferedReader read = new BufferedReader(new FileReader(file[0]));
				while((fileLine = read.readLine()) != null){
					list.add(fileLine);
					}
				}
				catch(Exception a){
					System.err.format("Error trying to read your file");
					a.printStackTrace();
				}
				String[] scheduling = list.toArray(new String[list.size()]);
				String[] split;
				List<String> singleTrainSchedule = new ArrayList<String>();
				for(int i = 0; i < scheduling.length; i++){
					split = (scheduling[i].split(" "));
					if(trainList.size() == i){
						trainList.add(new Trains());
						trainCount[0]++;
					}
					for(int j = 0; j < split.length; j++){
						if(Character.isDigit(split[j].charAt(0))){
							System.out.println(split[j]);
							String checker = split[j];
							String line = checker;
							trainList.get(i).setDeparture(line);
						}
						else{
							if(Character.isDigit(split[j].charAt(1))){
								trainList.get(i).createStop(split[j]);
							}
							else{
								trainList.get(i).setLine(split[j]);
							}
						}
					}
				}
			}
		});


		scheduleView.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(scheduleOpened.isEmpty()){
					for(int i = 0; i < trainList.size(); i++){
						scheduleOpened.add(i, 0);
					}
				}
				int openData = trainSelect.getSelectedIndex();
				String specificTrain = (String)trainSelect.getSelectedItem();
				if(scheduleOpened.get(openData) == 1){
					if(jlabelNum[0] == 1){
						panelBL3.removeAll();
					}
					System.out.println(openData);
					JLabel remove = trainViews.get(openData);
					panelBL3.remove(remove);
					trainViews.set(openData, null);
					panelBL3.remove(scheduleTexts.get(openData));
					scheduleTexts.set(openData, null);
					scheduleOpened.set(openData, 10);
					jlabelNum[0]--;
				}
				else if(openData <= list.size()){
					jlabelNum[0]++;
					JLabel trainLabel = new JLabel(specificTrain);
					trainViews.add(openData, trainLabel);
					String scheduleData = Arrays.toString(trainList.get(openData).getSchedule());
					JLabel scheduleLabel = new JLabel(trainList.get(openData).getDepartureTime()+trainList.get(openData).getDepartureHalf() +" " + scheduleData);
					scheduleTexts.add(openData, scheduleLabel);
					scheduleOpened.set(openData, 1);
					panelBL3.add(trainViews.get(openData));
					panelBL3.add(scheduleTexts.get(openData));
				}
			}
		});
		
		JComboBox blockChoose = new JComboBox();
		
		
	//	line.addActionListener(new ActionListener(){
		//	public void actionPerformed(ActionEvent e){
		//		int choice = line.getSelectedIndex();
		//		ArrayList<String> blocks = Tracking.getSection(choice);
		//		blockChoose.removeAllItems();
		//		for(int i = 0; i < blocks.size(); i++){
		//			blockChoose.addItem(blocks.get(i));
	//			}
			//}
		//});
	//	blockChoose.addActionListener(new ActionListener(){
			//public void actionPerformed(ActionEvent e){
			//	int choice = blockChoose.getSelectedIndex();
				
				//String block = (String)blockChoose.getSelectedItem();
				//LineView(choice, block);
			//}
		//});
		//adding in components and opening window
		speedChoice.add(none);
		speedChoice.add(ten);
		speedChoice.add(hundred);
		content.add(panelLeft, 0);
		content.add(panelRight, 1);
		content.add(panelRight,1);
		content.add(panelLeft, 0);
		panelLeft.setLayout(new GridLayout(3,0));
		panelLeft.add(panelBL1);
		panelLeft.add(panelBL2);
		panelLeft.add(panelBL3);
		panelBL1.setLayout(new GridLayout(5,2));
		panelBL1.add(autoMan);
		panelBL1.add(create);
		panelBL1.add(dispatch);
		panelBL1.add(panelBL5);
		panelBL2.setLayout(new GridLayout(0, 4));
		panelBL2.add(none);
		panelBL1.add(panelBL6);
		panelBL2.add(ten);
		panelBL1.add(panelBL7);
		panelBL2.add(hundred);
		panelBL1.add(fileOpen);
		panelBL1.add(panelBL8);
		panelBL1.add(trainChoose);
		panelBL1.add(panelBL9);
		panelBL1.add(panelBL10);
		panelBL1.add(trainSelect);
		panelBL3.add(thruPut);
		
		panelBL1.add(scheduleView);
		panelBL1.add(line);
		panelBL1.add(blockChoose);
		panelBL2.add(clock);
		JLabel trackPic = new JLabel("", track, JLabel.CENTER);
		panelRight.add(trackPic);
		//window displaying
		window.setSize( 1000,575 );
		window.setVisible( true );
	}
	public static void createFrame( ArrayList<JFrame> trainWindow, int[] windowNum, ArrayList<Trains> trainList, int open){
		windowNum[0] += 1;
		Trains trainData = trainList.get(open);
		JComboBox stations = new JComboBox();
		stations.addItem("Pioneer");
		stations.addItem("Edgebrook");
		stations.addItem("Station");
		stations.addItem("Whited");
		stations.addItem("South bank");
		stations.addItem("Central - I");
		stations.addItem("Inglewood - I");
		stations.addItem("Overbrook - I");
		stations.addItem("Glenbury - K");
		stations.addItem("Dormont - L");
		stations.addItem("Mt Lebanon");
		stations.addItem("Poplar");
		stations.addItem("Castle Shannon");
		stations.addItem("Dormont - T");
		stations.addItem("Glenbury - U");
		stations.addItem("Overbrook - W");
		stations.addItem("Inglewood - W");
		stations.addItem("Central - W");
		stations.addItem("Shadyside");
		stations.addItem("Herron Ave");
		stations.addItem("Swissville");
		stations.addItem("Penn Station");
		stations.addItem("Steel Plaza");
		stations.addItem("First Ave");
		stations.addItem("Station Square");
		stations.addItem("South Hills Junction");
		
		JButton createStop = new JButton("Create Stop");
			createStop.addActionListener( new ActionListener(){
				public void actionPerformed(ActionEvent e){
					String newStop = (String)stations.getSelectedItem();
					trainData.createStop("newStop");
				}
			});

		JFrame newWindow = new JFrame("Train " + (Integer.toString(open+1)));
		Container trainContainer = newWindow.getContentPane();
		
		JTextField speed = new JTextField(trainData.getSpeed(), 5);
		speed.addActionListener(new ActionListener(){	
			public void actionPerformed(ActionEvent e){
				String speedChange = speed.getText();
				trainData.setSpeed(Double.parseDouble(speedChange));
			}
		});
		
		JTextField authority = new JTextField(trainData.getAuthority(), 5);
		authority.addActionListener(new ActionListener(){	
			public void actionPerformed(ActionEvent e){
				String authorityChange = authority.getText();
				trainData.setAuthority(Integer.parseInt(authorityChange));
			}
		});

		JPanel newPanel1 = new JPanel();
		JPanel newPanel2 = new JPanel();
		newPanel1.setLayout(new GridLayout(4,1));
		newPanel2.setLayout(new GridLayout(4,1));
		
		
		JPanel newPanel11 = new JPanel();
		JPanel newPanel12 = new JPanel();
		JPanel newPanel13 = new JPanel();
		JPanel newPanel14 = new JPanel();
		JPanel newPanel21 = new JPanel();
		JPanel newPanel22 = new JPanel();
		JPanel newPanel23 = new JPanel();
		JPanel newPanel24 = new JPanel();
		
		trainContainer.add(newPanel1);
		trainContainer.add(newPanel2);
		
		newPanel1.add(newPanel11);
		newPanel1.add(newPanel12);
		newPanel1.add(newPanel13);
		newPanel1.add(newPanel14);
		newPanel2.add(newPanel21);
		newPanel2.add(newPanel22);
		newPanel2.add(newPanel23);
		newPanel2.add(newPanel24);
		
		
		Label stationsLabel = new Label("Selected Station");
		Label speedLabel = new Label("Enter Speed");
		Label authorityLabel = new Label("Enter Authority");
		Label mph = new Label("m/h");
		Label blocks = new Label("blocks");
		Label schedule = new Label("Schedule:");
		String scheduleData = Arrays.toString(trainData.getSchedule());
		String amPm = trainData.getDepartureHalf();
		String time = trainData.getDepartureTime();
		String line = trainData.getLine();
		Label scheduleInfo = new Label(line + " " + time + amPm +" "+ scheduleData);
		
		newPanel11.add(authorityLabel);
		newPanel12.add(blocks);
		newPanel12.add(authority);
		newPanel13.add(speedLabel);
		newPanel14.add(speed);
		newPanel14.add(mph);
		newPanel21.add(stationsLabel);
		newPanel21.add(stations);
		newPanel22.add(createStop);
		newPanel23.add(schedule);
		newPanel24.add(scheduleInfo);
		trainContainer.setLayout(new GridLayout(2,1));
		
		newWindow.setSize( 250,515 );
		newWindow.setVisible( true );
	}
	public static void receiveTickets(int numOfTickets){

	}
	
	public static void Purge(Timer t){
		t.cancel();
		t.purge();
	}
	public static Timer OneTimes( TimerTask clockRun, int[] timeConstant, int[] time, JLabel clock,
	String[] amPm, ArrayList<Trains> trainList, int[] autoManState, int[] trainListed, int[] trainCount, JComboBox trainSelect, JComboBox trainChoice)
	{
		timeConstant[0] = 1000;
		Timer t = new Timer();
		TimerTask newTask;
		newTask = ClockRun(time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice);
		t.schedule(newTask,timeConstant[0], timeConstant[0]);
		return t;
	}
	public static Timer TenTimes( TimerTask clockRun, int[] timeConstant, int[] time, JLabel clock, 
	String[] amPm, ArrayList<Trains> trainList, int[] autoManState, int[] trainListed, int[] trainCount, JComboBox trainSelect, JComboBox trainChoice)
	{
		timeConstant[0] = 100;
		Timer t = new Timer();
		TimerTask newTask;
		newTask = ClockRun(time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice);
		t.schedule(newTask,timeConstant[0], timeConstant[0]);
		return t;
	}
	public static Timer HundredTimes( TimerTask clockRun, int[] timeConstant, int[] time, JLabel clock,
	String[] amPm, ArrayList<Trains> trainList, int[] autoManState, int[] trainListed, int[] trainCount, JComboBox trainSelect, JComboBox trainChoice)
	{
		timeConstant[0] = 10;
		Timer t = new Timer();
		TimerTask newTask;
		newTask = ClockRun(time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice);
		t.schedule(newTask,timeConstant[0], timeConstant[0]);
		return t;
	}
	public static TimerTask ClockRun(int[] time, JLabel clock, String[] amPm, ArrayList<Trains> trainList, int[] autoManState,
	int[] trainListed, int[] trainCount, JComboBox trainSelect, JComboBox trainChoice){
		TimerTask clockRun = new TimerTask(){
			public void run(){
				if((time[0]/10000) == 12){
					if((time[0]/100)%100 == 59){
						if(time[0]%100 == 59){
							time[0] = (time[0] = 9999);
						}
					}
				}
				if((time[0]/100)%100 == 59){
					if(time[0]%100 == 59){
						time[0] = (time[0]+4040);
						if((time[0]/10000) == 11){
							if(amPm[0] == "am"){
								amPm[0] = "pm";
							}
							else{
								amPm[0] = "am";
							}
						}
					}
				}
				else if(time[0]%100 == 59){
					time[0] = (time[0]+40);
				}
				time[0]++;
				
				if((time[0]/100)%100 < 10){
					if(time[0]%100 < 10){
						clock.setText(""+((time[0]/10000))+":"+"0"+((time[0]/100)%100)+":"+"0"+(time[0]%100)+" "+amPm[0]);
					}
					else{
						clock.setText(""+((time[0]/10000))+":"+"0"+((time[0]/100)%100)+":"+(time[0]%100)+" "+amPm[0]);
					}
				}
				else if(time[0]%100 < 10){
					clock.setText(""+((time[0]/10000))+":"+((time[0]/100)%100)+":"+"0"+(time[0]%100)+" "+amPm[0]);
				}
				else{
					clock.setText(""+((time[0]/10000))+":"+((time[0]/100)%100)+":"+(time[0]%100)+" "+amPm[0]);
				}			
				if(autoManState[0] == 1 ){			
					if(trainListed[0] != trainCount[0]){				
						int diff = trainCount[0] - trainListed[0];
						int start = trainListed[0];
						for(int x = (start+1); x < (diff+1); x++){
							trainListed[0]++;
							trainSelect.addItem("Train " + Integer.toString(x));
							trainChoice.addItem("Train " + Integer.toString(x));
						}
					}
					if(!trainList.isEmpty()){
						for(int i = 0; i < trainList.size(); i++){
							if(trainList.get(i).hasSchedule()){
								String dispatchTime = new String(trainList.get(i).getDepartureTime());
								String half = new String(trainList.get(i).getDepartureHalf());
								if(Integer.parseInt(dispatchTime) == time[0]/10000){
									if(half.equals(amPm[0])){
										trainList.get(i).setSpeed(55);
									}
								}
							}
						}
					}
				}
			}
		};
		return clockRun;
	}
}