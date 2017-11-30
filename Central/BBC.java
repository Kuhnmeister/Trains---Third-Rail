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
	Central central;
	static boolean firstTrackCheck = true;
	static boolean isTrackTrue = false;
	static Tracking tracking = new Tracking();
	JComboBox<String> line = new JComboBox<String>();
	static boolean switched = false;
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
		JComboBox<String> trainSelect = new JComboBox<String>();
		JComboBox<String> trainChoice = new JComboBox<String>();
		
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
				currentTimer[0] = OneTime(clockRun, timeConstant, time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice);
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
				currentTimer[0] = TenTime(clockRun, timeConstant, time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice);
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
				currentTimer[0] = HundredTime(clockRun, timeConstant, time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice);
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
				if (state == ItemEvent.SELECTED) {
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
						trainList.get(trainCount[0]).setLength(1);
						trainSelect.addItem("Train " + Integer.toString(trainCount[0]));
						trainChoice.addItem("Train " + Integer.toString(trainCount[0]));
					}
					else{
						trainListed[0]++;
						trainList.get(trainListed[0]).setLength(1);
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
		
		JComboBox<String> blockChoose = new JComboBox<String>();
		
		

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
	
	public BBC(Central newCentral){
		//main window variables
		central = newCentral;
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
		JComboBox<String> trainSelect = new JComboBox<String>();
		JComboBox<String> trainChoice = new JComboBox<String>();
		
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
				//central.UpdateTrainModel(1);
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
				currentTimer[0] = OneTimes(clockRun, timeConstant, time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice, central, line);
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
				currentTimer[0] = TenTimes(clockRun, timeConstant, time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice, central, line);
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
				currentTimer[0] = HundredTimes(clockRun, timeConstant, time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice, central, line);
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
				if (state == ItemEvent.SELECTED) {
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
							if(trainList.get(trainSelect.getSelectedIndex()).hasSchedule()){
							//fix for all possible lines
								/*if(trainList.get(trainSelect.getSelectedIndex()).getLine() == "Red ")
								{
									trainList.get(trainSelect.getSelectedIndex()).setLocation(96);
								}
								else{
									trainList.get(trainSelect.getSelectedIndex()).setLocation(95);
								}*/
								System.out.println(trainList.get(trainSelect.getSelectedIndex()).getLocation());
								System.out.println(trainList.get(trainSelect.getSelectedIndex()).getLine());
								trainList.get(trainSelect.getSelectedIndex()).setSpeed(55.0);
								int length = 1;
								central.CreateTrain(trainList.get(trainSelect.getSelectedIndex()).getId(), trainList.get(trainSelect.getSelectedIndex()).getLength(), 0, 1, trainList.get(trainSelect.getSelectedIndex()).getLine());
							}
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
							String checker = split[j];
							String thisLine = checker;
							trainList.get(i).setDeparture(thisLine);
						}
						else{
							if(Character.isDigit(split[j].charAt(1))){
								trainList.get(i).createStop(split[j]);
							}
							else{
								System.out.println(split[j]);
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
		
		JComboBox<Integer> blockChoose = new JComboBox<Integer>();
		
		JButton showBlocks = new JButton("Show line blocks");
		
		showBlocks.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String choice = (String)line.getSelectedItem();
				ArrayList<Integer> block = tracking.blocks(choice);
				blockChoose.removeAllItems();
				for(int i = 0; i < block.size(); i++){
					blockChoose.addItem(block.get(i));
				}
			}
		});
		blockChoose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int choice = blockChoose.getSelectedIndex();
				int block = (int)blockChoose.getSelectedItem();
				String lineChoice = (String)line.getSelectedItem();
				LineView(choice, block, lineChoice);
			}
		});
		
		
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
		panelBL1.add(trainChoose);
		panelBL2.add(hundred);
		panelBL1.add(fileOpen);
		panelBL1.add(panelBL8);
		panelBL1.add(trainSelect);
		panelBL1.add(panelBL9);
		panelBL1.add(panelBL10);
		panelBL1.add(showBlocks);
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
	public void lineAdd(){
		String[] lineNames = tracking.getLines();
		for(int i = 0; i < lineNames.length; i++){
			line.addItem(lineNames[i]);
		}
	}
	public void trackReceived(boolean track){
	}
	public void receiveTrackData(HashMap<String, HashMap<String, ArrayList<Block>>> track){
		isTrackTrue = true;
		tracking.receiveTrackData(track);
		lineAdd();
	}
	public void createFrame( ArrayList<JFrame> trainWindow, int[] windowNum, ArrayList<Trains> trainList, int open){
		windowNum[0] += 1;
		JComboBox<Integer> stations = new JComboBox<Integer>();
		String line;
		if(trainList.get(open).hasSchedule()){
			line = trainList.get(open).getLine();
			ArrayList<Integer> theBlocks = tracking.blocks(line);
			for(int i = 0; i < theBlocks.size(); i++){
				stations.addItem(theBlocks.get(i));			
			}
		}
		else{
			String[] lines = tracking.getLines();
			for(int i = 0; i < lines.length; i++){
				ArrayList<Integer> theBlocks = tracking.blocks(lines[i]);
				for(int j = 0; j < theBlocks.size();j++){
					stations.addItem(theBlocks.get(i));
				}
			}
		}
				
		JButton createStop = new JButton("Create Stop");
			createStop.addActionListener( new ActionListener(){
				public void actionPerformed(ActionEvent e){
					String newStop = Integer.toString((Integer)stations.getSelectedItem());
					trainList.get(open).createStop(newStop);
				}
			});

		JFrame newWindow = new JFrame("Train " + (Integer.toString(open+1)));
		Container trainContainer = newWindow.getContentPane();
		
		JTextField speed = new JTextField(trainList.get(open).getSpeed(), 5);
		speed.addActionListener(new ActionListener(){	
			public void actionPerformed(ActionEvent e){
				String speedChange = speed.getText();
				trainList.get(open).setSpeed(Double.parseDouble(speedChange));
				int id = trainList.get(open).getId();
				int block = trainList.get(open).getLocation();
				central.SuggestSpeed(block, id, Double.parseDouble(speedChange));
			}
		});
		
		JTextField authority = new JTextField(trainList.get(open).getAuthority(), 5);
		authority.addActionListener(new ActionListener(){	
			public void actionPerformed(ActionEvent e){
				String authorityChange = authority.getText();
				trainList.get(open).setAuthority(Integer.parseInt(authorityChange));
				int location = trainList.get(open).getLocation();
				int authority = trainList.get(open).getLocation() + Integer.parseInt(trainList.get(open).getAuthority());
				central.CTCAuthority(location, authority, trainList.get(open).getId());
			}
		});
		JTextField length = new JTextField();
		length.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String trainLength = length.getText();
				trainList.get(open).setLength(Integer.parseInt(trainLength));
			}
		});
		JPanel newPanel1 = new JPanel();
		JPanel newPanel2 = new JPanel();
		newPanel1.setLayout(new GridLayout(4,1));
		newPanel2.setLayout(new GridLayout(6,1));
		
		
		JPanel newPanel11 = new JPanel();
		JPanel newPanel12 = new JPanel();
		JPanel newPanel13 = new JPanel();
		JPanel newPanel14 = new JPanel();
		JPanel newPanel21 = new JPanel();
		JPanel newPanel22 = new JPanel();
		JPanel newPanel23 = new JPanel();
		JPanel newPanel24 = new JPanel();
		JPanel newPanel25 = new JPanel();
		JPanel newPanel26 = new JPanel();
		
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
		newPanel2.add(newPanel25);
		newPanel2.add(newPanel26);
		
		Label lengthLabel = new Label("Enter number of cars");
		Label stationsLabel = new Label("Selected Station");
		Label speedLabel = new Label("Enter Speed");
		Label authorityLabel = new Label("Enter Authority");
		Label mph = new Label("m/h");
		Label blocks = new Label("blocks");
		Label schedule = new Label("Schedule:");
		String scheduleData = Arrays.toString(trainList.get(open).getSchedule());
		String amPm = trainList.get(open).getDepartureHalf();
		String time = trainList.get(open).getDepartureTime();

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
		newPanel26.add(length);
		newPanel25.add(lengthLabel);
		trainContainer.setLayout(new GridLayout(2,1));
		
		newWindow.setSize( 250,555 );
		newWindow.setVisible( true );
	}
	public static void receiveTickets(int numOfTickets){

	}
	
	private void LineView(int choice, int block, String lineChoice){
		JPanel box = new JPanel();
		JPanel crossings = new JPanel();
		JPanel undergrounds = new JPanel();
		JPanel theSwitches = new JPanel();
		JPanel switchButton = new JPanel();
		JPanel switchDisplay = new JPanel();
		JFrame lineView = new JFrame("Block: " + Integer.toString(block));
		box.setLayout(new GridLayout(1,2));
		JLabel header = new JLabel();
		Container lineContainer = lineView.getContentPane();
		lineContainer.setLayout(new GridLayout(5,1));
		lineContainer.add(crossings);
		lineContainer.add(undergrounds);
		lineContainer.add(theSwitches);
		lineContainer.add(switchButton);
		lineContainer.add(switchDisplay);
		ImageIcon Crossing = new ImageIcon("Crossing.png");
		ImageIcon Underground = new ImageIcon("Underground.png");
		ImageIcon trackSwitch = new ImageIcon("Switch.png");
		box.add(header);
		crossings.removeAll();
		undergrounds.removeAll();
		theSwitches.removeAll();
		JTextField[] switchState = new JTextField[1];
		
		JButton Switch = new JButton("Move Switch");
		if(tracking.hasCrossing(block, lineChoice)){
			JLabel Cross = new JLabel(Crossing);
			crossings.add(Cross);
		}
		if(tracking.isUnderground(block, lineChoice)){
			JLabel Under = new JLabel(Underground);
			undergrounds.add(Under);
		}
		if(tracking.hasSwitch(block, lineChoice)){
			JLabel switches = new JLabel(trackSwitch);
			theSwitches.add(switches);
			switchButton.add(Switch);
			switchState[0] = new JTextField(Integer.toString(block) + tracking.GetBlock(block, lineChoice).GetSwitchNum());
			switchDisplay.add(switchState[0]);
		}
		if(tracking.hasStation(block, lineChoice)){
			lineView = new JFrame("Block: " + Integer.toString(block) + "Line " + lineChoice + " STATION");
		}
		Switch.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				central.CTCMoveSwitch(block, !switched);
				switchState[0] = new JTextField(Integer.toString(block) + tracking.GetBlock(block, lineChoice).GetSwitchNum());
			}
		});
		lineView.setSize( 500,500 );
		lineView.setVisible( true );
	}
	
	
	public static void Purge(Timer t){
		t.cancel();
		t.purge();
	}
	
		public static Timer OneTime( TimerTask clockRun, int[] timeConstant, int[] time, JLabel clock,
	String[] amPm, ArrayList<Trains> trainList, int[] autoManState, int[] trainListed, int[] trainCount, JComboBox<String> trainSelect, JComboBox<String> trainChoice)
	{
		timeConstant[0] = 1000;
		Timer t = new Timer();
		TimerTask newTask;
		int multiplier = 1;
		newTask = ClockRuns(time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice, multiplier);
		t.schedule(newTask,timeConstant[0], timeConstant[0]);
		return t;
	}
	public static Timer TenTime( TimerTask clockRun, int[] timeConstant, int[] time, JLabel clock, 
	String[] amPm, ArrayList<Trains> trainList, int[] autoManState, int[] trainListed, int[] trainCount, JComboBox<String> trainSelect, JComboBox<String> trainChoice)
	{
		timeConstant[0] = 100;
		Timer t = new Timer();
		TimerTask newTask;
		int multiplier = 10;
		newTask = ClockRuns(time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice, multiplier);
		t.schedule(newTask,timeConstant[0], timeConstant[0]);
		return t;
	}
	public static Timer HundredTime( TimerTask clockRun, int[] timeConstant, int[] time, JLabel clock,
	String[] amPm, ArrayList<Trains> trainList, int[] autoManState, int[] trainListed, int[] trainCount, JComboBox<String> trainSelect, JComboBox<String> trainChoice)
	{
		timeConstant[0] = 10;
		Timer t = new Timer();
		TimerTask newTask;
		int multiplier = 100;
		newTask = ClockRuns(time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice, multiplier);
		t.schedule(newTask,timeConstant[0], timeConstant[0]);
		return t;
	}
	
	
	
	
	
	
	public static Timer OneTimes( TimerTask clockRun, int[] timeConstant, int[] time, JLabel clock,
	String[] amPm, ArrayList<Trains> trainList, int[] autoManState, int[] trainListed, int[] trainCount, JComboBox<String> trainSelect, JComboBox<String> trainChoice, Central central, JComboBox<String> line)
	{
		timeConstant[0] = 1000;
		Timer t = new Timer();
		TimerTask newTask;
		int multiplier = 1;
		newTask = ClockRun(time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice, multiplier, central, line);
		t.schedule(newTask,timeConstant[0], timeConstant[0]);
		return t;
	}
	public static Timer TenTimes( TimerTask clockRun, int[] timeConstant, int[] time, JLabel clock, 
	String[] amPm, ArrayList<Trains> trainList, int[] autoManState, int[] trainListed, int[] trainCount, JComboBox<String> trainSelect, JComboBox<String> trainChoice, Central central, JComboBox<String> line)
	{
		timeConstant[0] = 100;
		Timer t = new Timer();
		TimerTask newTask;
		int multiplier = 10;
		newTask = ClockRun(time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice, multiplier, central, line);
		t.schedule(newTask,timeConstant[0], timeConstant[0]);
		return t;
	}
	public static Timer HundredTimes( TimerTask clockRun, int[] timeConstant, int[] time, JLabel clock,
	String[] amPm, ArrayList<Trains> trainList, int[] autoManState, int[] trainListed, int[] trainCount, JComboBox<String> trainSelect, JComboBox<String> trainChoice, Central central, JComboBox<String> line)
	{
		timeConstant[0] = 10;
		Timer t = new Timer();
		TimerTask newTask;
		int multiplier = 100;
		newTask = ClockRun(time, clock, amPm, trainList, autoManState, trainListed, trainCount, trainSelect, trainChoice, multiplier, central, line);
		t.schedule(newTask,timeConstant[0], timeConstant[0]);
		return t;
	}
	
	
	public static TimerTask ClockRun(int[] time, JLabel clock, String[] amPm, ArrayList<Trains> trainList, int[] autoManState,
	int[] trainListed, int[] trainCount, JComboBox<String> trainSelect, JComboBox<String> trainChoice, int multiplier, Central central, JComboBox<String> line){
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
	
	public static TimerTask ClockRuns(int[] time, JLabel clock, String[] amPm, ArrayList<Trains> trainList, int[] autoManState,
	int[] trainListed, int[] trainCount, JComboBox<String> trainSelect, JComboBox<String> trainChoice, int multiplier){
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
			//central.UpdateTrainModel(multiplier);
			}
		};
		return clockRun;
	}
}
