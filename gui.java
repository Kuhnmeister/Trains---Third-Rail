package group.trains;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class gui
{
	JFrame window;  // the main window which contains everything
	Container content ;
	int buttonNum;
	int row;
	int col;
	JButton[] ops = new JButton[buttonNum]; 
	ButtonListener listener;

	public gui(int row, int col)
	{
		window = new JFrame( "Module interface" );
		content = window.getContentPane();
		content.setLayout(new GridLayout(row, col));
		listener = new ButtonListener();
	}
		
	public boolean newPanel(JPanel newPanel, int rows, int cols)
	{
		newPanel.setLayout(new GridLayout(rows, cols));
		content.add(newPanel);
		return true;
	}
	public boolean newField(JTextField newField, JPanel panel)
	{
		newField.setFont(new Font("verdana", Font.BOLD, 16));
		newField.setText("");
		panel.add(newField);
		return true;
	}
	public boolean newButton(JButton newButton, JPanel buttonLocation)
	{
		buttonLocation.add( newButton );
		newButton.addActionListener( listener ); 
		return true;
	}
	public boolean windowSizer(int x, int y)
	{
		window.setSize( x,y );
		window.setVisible( true );
		return true;
	}


	// We are again using an inner class here so that we can access
	// components from within the listener.  Note the different ways
	// of getting the int counts into the String of the label
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			// need to add tests for other controls that may have been
			// click that got us in here. Write code to handle those			
		}
	}
}
