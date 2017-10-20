import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Timer;

public class CTC{
	
	public static void main(String args[]){
		//main window variables
		JFrame window = new JFrame( "Centralized Traffic Control" );
		Container content;
		
		//panel variables
		JPanel panelRight = new JPanel();
		JPanel panelLeftTop = new JPanel();
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
		
		int[] num = new int[1];
		num[0] = 0; 
		ArrayList<Trains> trainList = new ArrayList<Trains>();
		JButton dispatch = new JButton("Dispatch");
		dispatch.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(autoManState[0] == 0){
					trainList.add(new Trains());
					num[0] += 1;
					trainSelect.addItem("Train " + Integer.toString(num[0]));
					trainChoice.addItem("Train " + Integer.toString(num[0]));
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
		panelRight.setLayout(new GridLayout(1, 1));
		panelLeftTop.setLayout(new GridLayout(3, 1));
		panelLeftBottom.setLayout(new GridLayout(1, 1));
		content.add(panelLeftTop, 0);
		content.add(panelRight, 1);

		//content.add(panelBottomRight, 1);
		
		//adding components
		ImageIcon track = new ImageIcon("trackmodel.png");

		panelLeftTop.add(panelBL1);
		panelLeftTop.add(panelBL2);
		panelLeftTop.add(panelBL3);
		
		panelBL1.add(panelBL4);
		panelBL1.add(panelBL5);
		panelBL1.add(panelBL6);
		panelBL1.add(panelBL7);
		panelBL1.add(panelBL8);
		panelBL4.add(autoMan);
		panelBL5.add(dispatch);
		panelBL3.add(none);
		panelBL3.add(ten);
		panelBL3.add(hundred);
		
		JLabel trainChoose = new JLabel("Train Selection");
		panelBL6.add(trainChoose);
		panelBL6.add(trainSelect);
		
		//schedule upload functionality
		JButton fileOpen = new JButton("Schedule File");
		fileOpen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFrame fileWindow = new JFrame("Choose Schedule File");
				Container fileContainer = fileWindow.getContentPane();
				JFileChooser schedule = new JFileChooser();
				FileNameExtensionFilter text = new FileNameExtensionFilter("Text files", "txt");
				schedule.setFileFilter(text);
				fileContainer.setLayout(new GridLayout(1,1));
				JPanel trainPanel = new JPanel();
				fileContainer.add(trainPanel);
				trainPanel.add(schedule);
				fileWindow.setResizable(false);
				fileWindow.setSize(500, 350);
				fileWindow.setVisible(true);
			}
		});
		
		//schedule view functionality
		JButton scheduleView = new JButton("View Schedule");
		scheduleView.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFrame scheduleWindow = new JFrame("Train Schedule");
				Container scheduleContainer = scheduleWindow.getContentPane();
				JPanel schedulePanel = new JPanel();
				scheduleContainer.add(schedulePanel);
				schedulePanel.add(trainChoice);
				trainChoice.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String specificTrain = (String)trainChoice.getSelectedItem();
						JTextField trainview = new JTextField(specificTrain);
					}
				});
				scheduleWindow.setSize(500,300);
				scheduleWindow.setVisible(true);
			}
		});
		
		//adding new components
		panelBL7.add(fileOpen);
		panelBL8.add(scheduleView);
		
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
		panelBL2.add(line);
		panelBL3.add(clock);
		JLabel trackPic = new JLabel("", track, JLabel.CENTER);
		panelRight.add(trackPic);
		//window displaying
		window.setSize( 1000,562 );
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