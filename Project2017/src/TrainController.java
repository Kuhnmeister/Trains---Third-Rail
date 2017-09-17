import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JComboBox comboBox1;
    boolean isAutoMode = false;

    public TrainController() {
        autoMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isAutoMode = true;
                speedInput.setEnabled(false);
                speedOK.setEnabled(false);
                brakeButton.setEnabled(false);
            }
        });
        manualMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isAutoMode = false;
                speedInput.setEnabled(true);
                speedOK.setEnabled(true);
                brakeButton.setEnabled(true);
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
