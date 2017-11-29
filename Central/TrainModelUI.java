import javax.swing.*;
import java.awt.*;

public class TrainModelUI {
    JLabel commandSpeedText;
    JLabel currentPowerText;
    JLabel slopeText;
    JLabel currentSpeedText;
    JLabel totalWeightText;
    JComboBox trainSelector;
    JButton emergencyStop;
    private JPanel panelMain;
    JPanel leftDoorIndicator;
    JPanel rightDoorIndicator;
    JPanel lightIndicator;
    JLabel nextStopText;
    JLabel carFailureText;
    JLabel outdoorTempText;
    JLabel indoorTempText;
    JLabel passengerText;
    private JButton calcButton;

    Container getPanelMain() {
        return panelMain;
    }

}
