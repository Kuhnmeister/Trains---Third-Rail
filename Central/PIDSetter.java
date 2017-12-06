import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PIDSetter {
    JTextField KpInput;
    JTextField KiInput;
    JButton OKButton;
    JPanel PanelMain;
    private TrainControllerUI trainctl;

    class doneActionListener implements ActionListener{

        private JFrame toBeClose;

        public doneActionListener(JFrame toBeClose) {
            this.toBeClose = toBeClose;
        }

        public void actionPerformed(ActionEvent e) {
            trainctl.currentController.Kp = Double.parseDouble(KpInput.getText());
            trainctl.currentController.Ki = Double.parseDouble(KiInput.getText());
            //System.out.print("KKp:"+trainctl.currentTrain.Kp);
            //trainctl.currentTrain.resetPID();
            toBeClose.setVisible(false);
            toBeClose.dispose();
        }
    }
    public PIDSetter(TrainControllerUI ctl)
    {
        trainctl = ctl;
        //OKButton.addActionListener(new doneActionListener(this));
    }
}
