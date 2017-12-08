import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PIDSetter {
    JTextField KpInput;
    JTextField KiInput;
    JButton OKButton;
    JPanel PanelMain;
    private TrainControllerUISwing trainControllerUISwing;

    class doneActionListener implements ActionListener{

        private JFrame toBeClose;

        public doneActionListener(JFrame toBeClose) {
            this.toBeClose = toBeClose;
        }

        public void actionPerformed(ActionEvent e) {
            trainControllerUISwing.currentController.Kp = Double.parseDouble(KpInput.getText());
            trainControllerUISwing.currentController.Ki = Double.parseDouble(KiInput.getText());
            //System.out.print("KKp:"+trainControllerUISwing.currentTrain.Kp);
            //trainControllerUISwing.currentTrain.resetPID();
            toBeClose.setVisible(false);
            toBeClose.dispose();
        }
    }
    public PIDSetter(TrainControllerUISwing ctl)
    {
        trainControllerUISwing = ctl;
        //OKButton.addActionListener(new doneActionListener(this));
    }
}
