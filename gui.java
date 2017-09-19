package group.trains;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class gui
{
	// the main window which contains everything
	JFrame window;  
	Container content;
	
	//instantiating necessary variables, names correspond to their function
	int buttonNum;
	int rowsInWindow;
	int colsInWindow;
	JButton[] ops = new JButton[buttonNum]; 
	ButtonListener listener;

	//primary function of class, constructor, takes window rows and cols from client and creates the window, 
	//reads values insantiated in methods that take variables from client
	public gui(int rowsInWindow, int colsInWindow)
	{
		window = new JFrame( "Module interface" );
		content = window.getContentPane();
		content.setLayout(new GridLayout(rowsInWindow, colsInWindow));
		listener = new ButtonListener();
	}
		
	//creates new panel, as desired by client, to send to constructor
	public boolean newPanel(JPanel newPanel, int rows, int cols)
	{
		newPanel.setLayout(new GridLayout(rows, cols));
		content.add(newPanel);
		return true;
	}
	
	//creates new text field, as desired by client, to send to constructor
	public boolean newField(JTextField newField, JPanel panel)
	{
		newField.setFont(new Font("verdana", Font.BOLD, 16));
		newField.setText("");
		panel.add(newField);
		return true;
	}
	
	//creates new button, as desired by client, to send to constructor
	public boolean newButton(JButton newButton, JPanel buttonLocation)
	{
		buttonLocation.add( newButton );
		newButton.addActionListener( listener ); 
		return true;
	}
	
	//sets window size, as specified by client, to send to constructor
	public boolean windowSizer(int x, int y)
	{
		window.setSize( x,y );
		window.setVisible( true );
		return true;
	}


	//actions to perform on button press
	//not yet developed
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//actions go here			
		}
	}
}
