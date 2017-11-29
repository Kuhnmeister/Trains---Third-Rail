import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

/*
    Created by yih on 09/16/2017
*/
public class TrainController {
    private JPanel PanelMain;
    private JComboBox trainSelector;
    private JRadioButton autoMode;
    private JRadioButton manualMode;
    private JTextField speedInput;
    private JButton speedOK;
    private JPanel safeIndicator;
    private JLabel safeText;
    private JButton emergencyStopButton;
    private JButton frontDoorButton;
    private JButton backDoorButton;
    private JPanel frontDoorIndicator;
    private JPanel backDoorIndicator;
    private boolean isAutoMode = false;
    private boolean frontDoorOpen = false;
    private boolean backDoorOpen = false;
    static final int DEFAULT = 50;
    private static final String doorIndicatorColor = "#E8E70C";
    private static final String indicatorOffColor = "#E8E8E8";

    public TrainController() {
        String[] Trains = {"Train 1", "Train 2", "Train 3"};
        trainSelector.addItem("Train 1");
        trainSelector.addItem("Train 2");
        autoMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isAutoMode = true;
                speedInput.setEnabled(false);
                speedOK.setEnabled(false);
                frontDoorButton.setEnabled(false);
                backDoorButton.setEnabled(false);
            }
        });
        manualMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isAutoMode = false;
                speedInput.setEnabled(true);
                speedOK.setEnabled(true);
                frontDoorButton.setEnabled(true);
                backDoorButton.setEnabled(true);
            }
        });
        frontDoorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frontDoorOpen = !frontDoorOpen;
                if(frontDoorOpen) {
                    frontDoorButton.setText("Close Front Door");
                    frontDoorIndicator.setBackground(Color.decode(doorIndicatorColor));
                } else {
                    frontDoorButton.setText("Open Front Door");
                    frontDoorIndicator.setBackground(Color.decode(indicatorOffColor));
                }
            }
        });
        backDoorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backDoorOpen = !backDoorOpen;
                if(backDoorOpen) {
                    backDoorButton.setText("Close Back Door");
                    backDoorIndicator.setBackground(Color.decode(doorIndicatorColor));
                } else {
                    backDoorButton.setText(" Open Back Door");
                    backDoorIndicator.setBackground(Color.decode(indicatorOffColor));
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Train Controller");
        frame.setContentPane(new TrainController().PanelMain);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }


}
