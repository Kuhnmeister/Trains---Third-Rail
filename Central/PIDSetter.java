import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PIDSetter {
    JTextField KpInput;
    JTextField KiInput;
    JButton OKButton;
    JPanel PanelMain;
    private TrainControllerUI trainControllerUI;

    class doneActionListener implements ActionListener{

        private JFrame toBeClose;

        public doneActionListener(JFrame toBeClose) {
            this.toBeClose = toBeClose;
        }

        public void actionPerformed(ActionEvent e) {
            trainControllerUI.currentController.Kp = Double.parseDouble(KpInput.getText());
            trainControllerUI.currentController.Ki = Double.parseDouble(KiInput.getText());
            //System.out.print("KKp:"+trainControllerUI.currentTrain.Kp);
            //trainControllerUI.currentTrain.resetPID();
            toBeClose.setVisible(false);
            toBeClose.dispose();
        }
    }
    public PIDSetter(TrainControllerUI ctl)
    {
        trainControllerUI = ctl;
        //OKButton.addActionListener(new doneActionListener(this));
    }
}
