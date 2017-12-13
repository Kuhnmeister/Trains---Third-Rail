import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.BitSet;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class TrainModel {
    public static final Double INTERVAL_LEN = 1.0;
    public static final Double G = 9.8; // m/s^2
    public static final Double FRICTION_RATE = 0.16;
    public static final Integer NUM_OF_WHEELS = 12;
    public static final Double ACCEL_MAX = 0.5; //m/s^2
    public static final Double BRAKE_SAFE_DIST = 5.0; //m
    public static final Double SERVICE_BRAKE_ACCEL = -1.2; // m/s^2
    public static final Double EMERGENCY_STOP_ACCEL = -2.73; // m/s^2
    public static final Double MILE2M = 1609.34; // a mile => meters
    public static final Double MPH2MS = 0.44704;
    public static final Double EPS = 0.00001;


    public static HashMap<String, String> stationNames;

    public static void initStationNames()
    {
        stationNames = new HashMap<>();
        stationNames.put("00000", "Shadyside");
        stationNames.put("00001", "Herron Ave");
        stationNames.put("00010", "Swissvale");
        stationNames.put("00011", "Penn Station");
        stationNames.put("00100", "Steel Plaza");
        stationNames.put("00101", "First Ave");
        stationNames.put("00110", "Station Square");
        stationNames.put("00111", "South Hills Junction");

        stationNames.put("01000", "Pioneer");
        stationNames.put("01001", "Edgebrook");
        stationNames.put("01010", "Willow");
        stationNames.put("01011", "Whited");
        stationNames.put("01100", "South Bank");
        stationNames.put("01101", "Central");
        stationNames.put("01110", "Inglewood");
        stationNames.put("01111", "Overbrook");
        stationNames.put("10000", "Glenbury");
        stationNames.put("10001", "Dormont");
        stationNames.put("10010", "Mt. Lebanon");
        stationNames.put("10011", "Poplar");
        stationNames.put("10100", "Castle Shannon");
    }


    Central theCentral;
    // Errors
    String carErrorString = "OK";
    Boolean hasCarError = false;
    Boolean hasPowerError = false;
    Boolean hasBrakeError = false;
    Boolean powerLimitReached = false;
    // Whether the train is on emergency stop state, this will override other things
    Boolean enforceEmergencyStop = false;

    Boolean leftDoorOpen = false;
    Boolean rightDoorOpen = false;
    Boolean lightOn = false;
    Boolean inYard = true;
    // Display speed are in mph. Non-display speed are in m/s
    Double currentSpeed = 0.0;
    Double displayCurrentSpeed = 0.0;
    // commandSpeed, targetSpeed and speedLimit are in controller
    Double currentAccel = 0.0;
    Double authority = 0.0; // in m
    Double displayAuthority = 0.0; //in mile
    Double slope = 0.0; // in radius
    Double displaySlope = 0.0; // in degree
    Double currentPower = 0.0; // in kW
    Double maxPower = 400.0;
    // Station Info
    BitSet lastBeacon;
    String nextStation = "";
    String[] stations;
    Integer stationIndex = 0;
    Boolean stopped = true;
    Boolean stopAtStation = false;
    Boolean toOpenLeftDoor = false;
    Boolean toOpenRightDoor = false;
    Integer passengerGetOn = 0;
    // Whether is using brake because of authority
    Boolean onAuthorityBrake = false;
    // Whether the emergency brake on the train is on
    Boolean emergencyBrakeCommand = false;
    // Whether any brake is current active. These links to physical brake directly
    Boolean serviceBrakeActive = false;
    Boolean emergencyStopActive = false; // emergency brake will override service brake
    // Number of passgenrs
    Integer passengerNum = 0;
    Double totalWeight = 100.0; // in Tons
    // TrainId
    Integer id;

    public TrainController controller;
    public TrainModelUISwing ui;

    public TrainModel(Integer id, Central central)
    {
        initStationNames();
        this.id = id;
        this.theCentral = central;
    }

    Boolean getSchedule(String[] stations)
    {
        this.stations = stations;
        if(stations == null || stations.length <=0)
        {
            System.out.println("TM--Warning: Schedule with 0 length");
            return false;
        } else {
            this.nextStation = stations[0];
            stationIndex = 0;
            System.out.println("TM--Train "+this.id+" received schedule "+stations);
        }
        return true;
    }

    void setSlope(Double slope)
    {
        this.slope = slope;
        updateInfo();
    }

    void setAuthority(Double authority) {
        this.authority = authority*MILE2M;
        convertDisplayUnit();
        updateInfo();
        // ui.update() will be called in the controller
    }

    void setTotalWeight(Double weight) {
        this.totalWeight = weight;
        convertDisplayUnit();
        updateInfo();
        // ui.update() will be called in the controller
    }

    void setMaxPower(Double maxPower) {
        this.maxPower = maxPower;
        convertDisplayUnit();
        updateInfo();
        // ui.update() will be called in the controller
    }

    void setHasCarError(Boolean hasError, String errorString)
    {
        this.hasCarError = hasError;
        this.carErrorString = hasError?errorString:"OK";
        updateInfo();
    }

    void setHasPowerError(Boolean hasError)
    {
        this.hasPowerError = hasError;
        updateInfo();
    }

    void setHasBrakeError(Boolean hasError)
    {
        this.hasBrakeError = hasError;
        updateInfo();
    }

    void setPassengerGetOn(Integer num)
    {
        passengerGetOn = num;
    }

    static String toBinaryString(BitSet set)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < set.length(); i++) {
            stringBuilder.append(set.get(i)?"1":"0");
        }
        return stringBuilder.toString();
    }

    void processBeacon(BitSet beacon)
    {
        Boolean isFront = beacon.get(0);
        if(!isFront)
        {
            return;
        }
        if(lastBeacon != null && beacon.equals(lastBeacon))
        {
            lastBeacon = beacon;
            stopAtStation = true;
        }
        BitSet stationId = beacon.get(1,6);
        String stationString = toBinaryString(stationId);
        nextStation = stationNames.get(stationString);
        toOpenLeftDoor = beacon.get(6);
        toOpenRightDoor = beacon.get(7);
        System.out.println("TM--Train get beacon of station "+nextStation+". Doors:"+toOpenLeftDoor+" "+toOpenLeftDoor);
        updateInfo();
    }

    void emegencyCommand() {
        this.emergencyBrakeCommand = !this.emergencyBrakeCommand;
        updateInfo();
        // ui.update() will be called in the controller
    }


    void updateInfo()
    {
        if(controller == null)
        {
            ui.update();
            return;
        }
        controller.update();
        // ui.update() will be called in the controller
    }

    void updateUI()
    {
        convertDisplayUnit();
        if(ui == null)
        {
            return;
        }
        ui.update();
    }

    Double brakeDistance()
    {
        Double v = 0.0;
        Double v0 = currentSpeed;
        Double a = SERVICE_BRAKE_ACCEL;
        Double s;

        // v^2-v0^2 = 2as
        s = (v*v - v0*v0) / (2*a);

        return s;
    }

    void convertDisplayUnit()
    {
        displayCurrentSpeed = currentSpeed/MPH2MS;
        displayAuthority = authority/MILE2M;
        displaySlope = Math.toDegrees(slope);
    }

    void step()
    {
        Double force, friction;
        if(this.controller != null)
        {
            currentPower = this.controller.getPower(currentSpeed, INTERVAL_LEN);
            System.out.println("TM--Current Power:"+currentPower.toString());
        }

        if(brakeDistance() >= authority + BRAKE_SAFE_DIST/MILE2M)
        {
            onAuthorityBrake = true;
            serviceBrakeActive = true;
        }else{
            onAuthorityBrake = false;
            serviceBrakeActive = false;
        }

        //v = F/P
        //Can't divide by 0
        if(currentSpeed <= EPS)
        {
            force = currentPower * 1000 / 1;
        } else {
            force = currentPower * 1000 / currentSpeed;
        }
        friction = (totalWeight * 1000 * G * FRICTION_RATE)/NUM_OF_WHEELS;
        //friction = (totalWeight * 1000 /NUM_OF_WHEELS * G * Math.sin(slope))
        //        + (friction * Math.cos(slope));

        force = force - friction;
        if(Math.abs(force) < EPS)
        {
            force = 0.0;
        }
        currentAccel = force/(totalWeight*1000);
        if(currentAccel > ACCEL_MAX)
        {
            currentAccel = ACCEL_MAX;
        }
        if(emergencyStopActive)
        {
            currentAccel = EMERGENCY_STOP_ACCEL;
        }else if(serviceBrakeActive)
        {
            currentAccel = SERVICE_BRAKE_ACCEL;
        }
        Double lastSpeed = currentSpeed;
        currentSpeed = currentSpeed + currentAccel*INTERVAL_LEN;
        if(currentSpeed <= EPS)
        {
            currentSpeed = 0.0;
            // No friction when not moving
            if(currentAccel < 0)
            {
                currentAccel = 0.0;
            }
            if(!stopped)
            {
                stopped = true;
                System.out.println("TM--Train Stopped.");
                if(stopAtStation)
                {
                    System.out.println("TM--Train Stopped at Staion "+stations[stationIndex]+".");
                    // Open the door if there is a command
                    controller.setLeftDoorCommand(toOpenLeftDoor);
                    controller.setRightDoorComand(toOpenRightDoor);
                    Integer passengerGetOff = ThreadLocalRandom.current().nextInt(0, passengerNum+1);
                    passengerNum -= passengerGetOff;
                    passengerNum += passengerGetOn;
                    this.theCentral.UpdatePassegnerFromTrain(this.id, passengerGetOff, passengerNum);
                    passengerGetOn = 0;
                    // already stopped, not to stop at station
                    stopAtStation = false;
                }
            }
        } else {
            if(stopped) {
                System.out.println("TM--Train Started.");
                stationIndex += 1;
                nextStation = stations[stationIndex];
                stopped = false;
                // Close the door anyway when moving
                controller.setLeftDoorCommand(leftDoorOpen);
                controller.setLeftDoorCommand(rightDoorOpen);
            }
        }

        Double distanceTraveled = lastSpeed * INTERVAL_LEN + (1/2 * currentAccel * Math.pow(INTERVAL_LEN, 2));
        this.theCentral.UpdateTrainDistance(id, distanceTraveled);

        displayCurrentSpeed = currentSpeed/MPH2MS;
        System.out.println("TM--current Speed: "+displayCurrentSpeed);
        if(stopped)
        {
            serviceBrakeActive = false;
            emergencyStopActive = false;
        }
        updateInfo();
    }

}

/*public class train.TrainModel implements TrainControllerOutputReceiver {
    public static final String onIndicatorColor = "#E8E70C";
    public static final String indicatorOffColor = "#E8E8E8";
    private JFrame frameMain;
    private TrainModelUISwing ui;
    private static ArrayList<TrainStatus> Trains;
    private TrainStatus currentTrain;

    public train.TrainModel(ArrayList<TrainStatus> trains)
    {
        Trains = trains;
        CreateGUI();
        Trains.forEach(t -> ui.trainSelector.addItem(t.getName()));
        currentTrain = Trains.get(0);
        ui.trainSelector.addActionListener(e ->{
            int index = ui.trainSelector.getSelectedIndex();
            currentTrain = Trains.get(index);
        });
        update();
    }

    private void CreateGUI()
    {
        ui = new TrainModelUISwing();
        frameMain = new JFrame("Train Model");
        frameMain.setContentPane(ui.getPanelMain());
        frameMain.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameMain.pack();
        frameMain.setVisible(true);
        ui.emergencyBrakeCommand.addActionListener(e -> {
            currentTrain.setEmergencyCommand(true);
            currentTrain.update();
        });
    }

    @Override
    public void update() {
        ui.currentPowerText.setText(currentTrain.getCurrentPower().toString());
        //System.out.println("cspeed:"+currentTrain.getCommandSpeed().toString());
        ui.commandSpeedText.setText(currentTrain.getCommandSpeed().toString());
        ui.currentSpeedText.setText(currentTrain.getCurrentSpeed().toString());
        ui.totalWeightText.setText(currentTrain.getTotalWeight().toString());
        ui.nextStopText.setText(currentTrain.getNextStation());
        ui.carFailureText.setText(currentTrain.getCarStatus());
        if(currentTrain.getLeftDoorOpen()) {
            ui.leftDoorIndicator.setBackground(Color.decode(onIndicatorColor));
        } else {
            ui.leftDoorIndicator.setBackground(Color.decode(indicatorOffColor));
        }
        if(currentTrain.getRightDoorOpen()) {
            ui.rightDoorIndicator.setBackground(Color.decode(onIndicatorColor));
        } else {
            ui.rightDoorIndicator.setBackground(Color.decode(indicatorOffColor));
        }
        if(currentTrain.getLightOn()) {
            ui.lightIndicator.setBackground(Color.decode(onIndicatorColor));
        } else {
            ui.lightIndicator.setBackground(Color.decode(indicatorOffColor));
        }
    }

}
*/
