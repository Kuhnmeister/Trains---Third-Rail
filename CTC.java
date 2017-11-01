import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Timer;
import java.util.List;

public class CTC{
	
	public static void main(String args[]){
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
		
		int[] autoManState = new int[1];
		autoManState[0] = 0;
		
		int[] trainCount = new int[1];
			
		//clock creation		
		Timer[] currentTimer = new Timer[1];
		int[] timeConstant = new int[1];
		timeConstant[0] = 1000;
		int[] time = new int[1];
		time[0] = 90000;
		Timer t = new Timer();
		JLabel clock = new JLabel();
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
					}
				}
				else if(time[0]%100 == 59){
					time[0] = (time[0]+40);
				}
				time[0]++;
				
				if((time[0]/100)%100 < 10){
					if(time[0]%100 < 10){
						clock.setText(""+((time[0]/10000))+":"+"0"+((time[0]/100)%100)+":"+"0"+(time[0]%100));
					}
					else{
						clock.setText(""+((time[0]/10000))+":"+"0"+((time[0]/100)%100)+":"+(time[0]%100));
					}
				}
				else if(time[0]%100 < 10){
					clock.setText(""+((time[0]/10000))+":"+((time[0]/100)%100)+":"+"0"+(time[0]%100));
				}
				else{
					clock.setText(""+((time[0]/10000))+":"+((time[0]/100)%100)+":"+(time[0]%100));
				}
			}
		};
		t.schedule(clockRun, timeConstant[0], timeConstant[0]);
		//line view variables

		ButtonGroup speedChoice = new ButtonGroup();
		JRadioButton none = new JRadioButton("1x");
		JRadioButton ten = new JRadioButton("10x");
		JRadioButton hundred = new JRadioButton("100x");
		speedChoice.add(none);
		speedChoice.add(ten);
		speedChoice.add(hundred);
		
		none.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(t != null){
					Purge(t);
				}
				if(currentTimer[0] != null){
					Purge(currentTimer[0]);
				}
				currentTimer[0] = OneTimes(clockRun, timeConstant, time, clock);
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
				currentTimer[0] = TenTimes(clockRun, timeConstant, time, clock);
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
				currentTimer[0] = HundredTimes(clockRun, timeConstant, time, clock);
			}
		});
		//auto/manual button creation
		String autoSwitch = "Manual";
		JToggleButton autoMan = new JToggleButton(autoSwitch);
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
		JComboBox trainSelect = new JComboBox();
		JComboBox trainChoice = new JComboBox();
		
		trainCount[0] = 0;
		int[] trainListed = new int[1];
		trainListed[0] = 0;
		ArrayList<Trains> trainList = new ArrayList<Trains>();
		JButton dispatch = new JButton("Dispatch");
		dispatch.addActionListener( new ActionListener(){
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
		ArrayList<JFrame> trainWindow = new ArrayList<JFrame>();
		int[] windowNum = new int[1];
		windowNum[0] = 0;
		trainSelect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int open = trainSelect.getSelectedIndex();
				createFrame(trainWindow, windowNum, trainList, open);
			}
		});
		
		//Constructing the window
		content = window.getContentPane();
		content.setLayout(new GridLayout(1,2));
		panelRight.setLayout(new GridLayout(1, 2));
		panelLeftBottom.setLayout(new GridLayout(1, 1));
		content.add(panelLeft, 0);
		content.add(panelRight, 1);

		//content.add(panelBottomRight, 1);
		
		//adding components
		ImageIcon track = new ImageIcon("trackmodel.png");

		content.add(panelRight,1);
		content.add(panelLeft, 0);
		panelLeft.setLayout(new GridLayout(3,0));
		panelLeft.add(panelBL1);
		panelLeft.add(panelBL2);
		panelLeft.add(panelBL3);
		panelBL1.setLayout(new GridLayout(5,2));
		panelBL1.add(autoMan);
		panelBL1.add(panelBL4);
		panelBL1.add(dispatch);
		panelBL1.add(panelBL5);
		panelBL2.setLayout(new GridLayout(0, 4));
		panelBL2.add(none);
		panelBL1.add(panelBL6);
		panelBL2.add(ten);
		panelBL1.add(panelBL7);
		panelBL2.add(hundred);
		
		List<String> list = new ArrayList<String>();
		String[] file = new String[1];
		JButton fileOpen = new JButton("Schedule File");
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
							String departure = split[j];
							System.out.println(departure);
							trainList.get(i).setDeparture(departure);
						}
						else{
							trainList.get(i).createStop(split[j]);
						}
					}
				}
			}
		});


		
		panelBL1.add(fileOpen);
		panelBL1.add(panelBL8);
		JLabel trainChoose = new JLabel("Train Selection");
		panelBL1.add(trainChoose);
		panelBL1.add(panelBL9);
		panelBL1.add(panelBL10);
		panelBL1.add(trainSelect);
		panelBL3.setLayout(new GridLayout(5,0));
		
		//schedule upload functionality

		
		//schedule view functionality
		int[] scheduleOpened = new int[100];
		JButton scheduleView = new JButton("View Schedule");
		JLabel[] trainViews = new JLabel[100];
		JLabel[] scheduleTexts = new JLabel[100];
		scheduleView.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int openData = trainSelect.getSelectedIndex();
				String specificTrain = (String)trainSelect.getSelectedItem();
				if(scheduleOpened[openData] == 1){
					JLabel remove = trainViews[openData];
					panelBL3.remove(remove);
					trainViews[openData] = null;
					panelBL3.remove(scheduleTexts[openData]);
					scheduleTexts[openData] = null;
					scheduleOpened[openData] =  10;
					System.out.println(openData);
				}
				else if(openData <= list.size()){
					
					JLabel trainLabel = new JLabel(specificTrain);
					trainViews[openData] = trainLabel;
					String scheduleData = Arrays.toString(trainList.get(openData).getSchedule());
					System.out.println(trainList.get(openData).getDeparture());
					JLabel scheduleLabel = new JLabel(trainList.get(openData).getDeparture() +" " + scheduleData);
					scheduleTexts[openData] = scheduleLabel;
					scheduleOpened[openData] = 1;
					panelBL3.add(trainViews[openData]);
					panelBL3.add(scheduleTexts[openData]);
					System.out.println(openData);
				}
			}
		});
		
		//adding new components
		panelBL1.add(scheduleView);
		
		//track interactivity functionality
		JComboBox line = new JComboBox();
		line.addItem("Red");
		line.addItem("Green");
		line.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int choice = line.getSelectedIndex();
				LineView(choice);
			}
		});
		
		//adding final components and opening window
		panelBL1.add(line);
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
		newPanel1.setLayout(new GridLayout(3,1));
		newPanel2.setLayout(new GridLayout(3,1));
		
		
		JPanel newPanel11 = new JPanel();
		JPanel newPanel12 = new JPanel();
		JPanel newPanel13 = new JPanel();
		JPanel newPanel21 = new JPanel();
		JPanel newPanel22 = new JPanel();
		JPanel newPanel23 = new JPanel();
		
		
		trainContainer.add(newPanel1);
		trainContainer.add(newPanel2);
		
		newPanel1.add(newPanel11);
		newPanel1.add(newPanel12);
		newPanel1.add(newPanel13);
		newPanel2.add(newPanel21);
		newPanel2.add(newPanel22);
		newPanel2.add(newPanel23);
		
		Label stationsLabel = new Label("Selected Station");
		Label speedLabel = new Label("Enter Speed");
		Label authorityLabel = new Label("Enter Authority");
		Label mph = new Label("m/h");
		Label blocks = new Label("blocks");
		
		
		newPanel13.add(speedLabel);
		newPanel11.add(authorityLabel);
		newPanel21.add(speed);
		newPanel21.add(mph);
		newPanel12.add(authority);
		newPanel12.add(blocks);
		newPanel22.add(stationsLabel);
		newPanel22.add(stations);
		newPanel23.add(createStop);
		trainContainer.setLayout(new GridLayout(2,3));
		
		newWindow.setSize( 250,400 );
		newWindow.setVisible( true );
	}
	private static void LineView(int choice){
		if(choice == 0){
			JFrame lineView = new JFrame("Red Line");
			Container lineContainer = lineView.getContentPane();
			JPanel linePanel = new JPanel();
			lineContainer.add(linePanel);
			JComboBox blockChoice = new JComboBox();
			for(int i = 1; i < 77; i++){
				blockChoice.addItem(Integer.toString(i));
			}
			linePanel.add(blockChoice);
			lineView.setSize( 250,400 );
			lineView.setVisible( true );
		}
		if(choice == 1){
			JFrame lineView = new JFrame("Green Line");
			Container lineContainer = lineView.getContentPane();
			JPanel linePanel = new JPanel();
			lineContainer.add(linePanel);
			JComboBox blockChoice = new JComboBox();
			for(int i = 1; i < 77; i++){
				blockChoice.addItem(Integer.toString(i));
			}
			linePanel.add(blockChoice);
			lineView.setSize( 250,400 );
			lineView.setVisible( true );
		}
	}
	public static void Purge(Timer t){
		t.cancel();
		t.purge();
	}
	public static Timer OneTimes( TimerTask clockRun, int[] timeConstant, int[] time, JLabel clock){
		timeConstant[0] = 1000;
		Timer t = new Timer();
		TimerTask newTask;
		newTask = ClockRun(time, clock);
		t.schedule(newTask,timeConstant[0], timeConstant[0]);
		return t;
	}
	public static Timer TenTimes( TimerTask clockRun, int[] timeConstant, int[] time, JLabel clock){
		timeConstant[0] = 100;
		Timer t = new Timer();
		TimerTask newTask;
		newTask = ClockRun(time, clock);
		t.schedule(newTask,timeConstant[0], timeConstant[0]);
		return t;
	}
	public static Timer HundredTimes( TimerTask clockRun, int[] timeConstant, int[] time, JLabel clock){
		timeConstant[0] = 10;
		Timer t = new Timer();
		TimerTask newTask;
		newTask = ClockRun(time, clock);
		t.schedule(newTask,timeConstant[0], timeConstant[0]);
		return t;
	}
	public static TimerTask ClockRun(int[] time, JLabel clock){
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
					}
				}
				else if(time[0]%100 == 59){
					time[0] = (time[0]+40);
				}
				time[0]++;
				
				if((time[0]/100)%100 < 10){
					if(time[0]%100 < 10){
						clock.setText(""+((time[0]/10000))+":"+"0"+((time[0]/100)%100)+":"+"0"+(time[0]%100));
					}
					else{
						clock.setText(""+((time[0]/10000))+":"+"0"+((time[0]/100)%100)+":"+(time[0]%100));
					}
				}
				else if(time[0]%100 < 10){
					clock.setText(""+((time[0]/10000))+":"+((time[0]/100)%100)+":"+"0"+(time[0]%100));
				}
				else{
					clock.setText(""+((time[0]/10000))+":"+((time[0]/100)%100)+":"+(time[0]%100));
				}
			}
			
		};
		return clockRun;
	}
}
