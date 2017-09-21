import javax.swing.*;
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
    private JButton brakeButton;
    private JSlider accSlider;
    private JSlider brakeSlider;
    private JButton accelerateButton;
    boolean isAutoMode = false;
    static final int DEFAULT = 50;

    public TrainController() {
        String[] Trains = {"Train 1", "Train 2", "Train 3"};
        trainSelector.addItem("Train 1");
        trainSelector.addItem("Train 2");
        Hashtable labelTable = new Hashtable();
        labelTable.put( new Integer( 0 ), new JLabel("Min") );
        labelTable.put( new Integer( 50 ), new JLabel("Default") );
        labelTable.put( new Integer( 100 ), new JLabel("Max") );
        accSlider.setLabelTable( labelTable );
        brakeSlider.setLabelTable( labelTable );
        autoMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isAutoMode = true;
                speedInput.setEnabled(false);
                speedOK.setEnabled(false);
                accSlider.setEnabled(false);
                brakeSlider.setEnabled(false);
                accSlider.setValue(DEFAULT);
                brakeSlider.setValue(DEFAULT);
                brakeButton.setEnabled(false);
                accelerateButton.setEnabled(false);
            }
        });
        manualMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isAutoMode = false;
                speedInput.setEnabled(true);
                speedOK.setEnabled(true);
                accSlider.setEnabled(true);
                brakeSlider.setEnabled(true);
                brakeButton.setEnabled(true);
                accelerateButton.setEnabled(true);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Train Controller");
        frame.setContentPane(new TrainController().PanelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }


}
