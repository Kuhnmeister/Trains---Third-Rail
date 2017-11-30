import com.stormbots.MiniPID;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class TrainStatus {
    static final Double NORMAL_BRAKE_RATE = 1.0;
    static final Double SERVICE_BRAKE_RATE = 3.0;
    static final Double EMERGENCY_BRAKE_RATE = 5.0;
    static final Double Ton2Kg = 1000.0;
    static final Double G = 9.8;
    static final Double MPH2MS = 0.44704;
    static final Double WATT2HP = 0.00134102;
    static final Double S = 1.0;
    static final Double CARWEIGHT = 50.0;
    static final Double EPS = 0.00001;

    String name = "Train";
    Integer id;
    Boolean isAutoMode = true;
    Double totalWeight = 250.0;
    Boolean hasCarError = false;
    Boolean hasBrakeError = false;
    Boolean hasPowerError = false;
    Boolean hasError = false;
    String carStatus = "OK";
    String carInput = "1";
    String brakeStatus = "OK";
    String powerStatus = "OK";
    Integer maxPower = 500000;
    Double currentSpeed = 0.0;
    Double currentAccel = 0.5;
    Double currentPower = 50.0;
    //Double currentBrakeRate = 5.0;
    String currentBrakeRate = "null";
    String nextStation = "Train Center";
    Double speedLimit = 70.0;
    Double inputSpeed = 0.0;
    Double commandSpeed = 0.0;
    Double authority = 0.0;
    Boolean authorityEmergencyStop = false;
    Boolean emergencyCommand = false;
    Boolean isEmergencyStop = false;
    Boolean isServiceBrake = false;
    Boolean leftDoorCommand = false;
    Boolean leftDoorOpen = false;
    Boolean rightDoorCommand = false;
    Boolean rightDoorOpen = false;
    Boolean lightCommand = false;
    Boolean lightOn = false;
    Boolean isSystemError = false;
    Boolean isPowerLimitReached = false;
    Boolean isSpeedLimitReached = false;
    Boolean isAuthorityError = false;
    Boolean inYard = false;
    Integer carNumber = 1;
    Boolean motorOff = false;
    Boolean stopAtStation = false;
    TrainWithController parent;
    ArrayList<String> stopNames;
    Integer stopIndex = 0;

    public MiniPID PID;
    public Double Kp = 100.0;
    public Double Ki = 100.0;

    public TrainStatus(String name, Integer carNumber, TrainWithController parent, ArrayList<String> stopNames) {
        this.name = name;
        this.carNumber = carNumber;
        this.parent = parent;
        this.stopNames = stopNames;
        if (stopNames.size() > 0)
        {
            nextStation = stopNames.get(0);
        }
        totalWeight = carNumber*CARWEIGHT;
        PID = new MiniPID(Kp, Ki, 0.0);
    }

    public void resetPID()
    {
        PID.setP(Kp);
        PID.setI(Ki);
    }

    public void update()
    {
        if(carInput.compareTo("1") == 0)
        {
            carStatus = "OK";
            hasCarError = false;
        } else {
            carStatus = carInput;
            hasCarError = true;
        }
        System.out.println("Train: "+ name);
        //System.out.println("Car Input:"+carInput);
        //System.out.println("Car Error:"+hasCarError);
        if(hasBrakeError)
        {
            brakeStatus = "Error";
        } else {
            brakeStatus = "OK";
        }
        if(hasPowerError)
        {
            powerStatus = "Error";
        } else {
            powerStatus = "OK";
        }
        hasError = hasCarError | hasBrakeError | hasPowerError;
        System.out.println("Car Error:"+hasCarError);
        System.out.println("Brake Error:"+hasBrakeError);
        System.out.println("Power Error:"+hasPowerError);
        if(hasError || authorityEmergencyStop)
        {
            isAutoMode = true;
        }
        if(hasError && currentSpeed > 0 || authorityEmergencyStop)
        {
            isEmergencyStop = true;
            commandSpeed = 0.0;
            inputSpeed = 0.0;
        }

        if(emergencyCommand)
        {
            if(currentSpeed <=0 && !authorityEmergencyStop)
            {
                isEmergencyStop = !isEmergencyStop;
            }else {
                if (authorityEmergencyStop) {
                    isAuthorityError = true;
                } else if (!hasError) {
                    isEmergencyStop = !isEmergencyStop;
                    System.out.println("ES:" + isEmergencyStop);
                    isAuthorityError = false;
                }
            }
            emergencyCommand = false;
            parent.triggerEmergencyStop(id, emergencyCommand);
        }

        if(hasError)
        {
            inputSpeed = 0.0;
            //currentAccel = 0.0;
        }

        if(inputSpeed > speedLimit)
        {
            isSpeedLimitReached = true;
        } else {
            commandSpeed = inputSpeed;
            isSpeedLimitReached = false;
        }

        if(commandSpeed >= currentSpeed)
        {
            currentBrakeRate = "null";
        } else {
            currentBrakeRate = "fricRate";
        }
        //currentBrakeRate = NORMAL_BRAKE_RATE;
        if(isServiceBrake)
        {
            currentBrakeRate = "SBRate";
            //currentBrakeRate = SERVICE_BRAKE_RATE;
        }
        if(isEmergencyStop)
        {
            //currentBrakeRate = EMERGENCY_BRAKE_RATE;
            currentBrakeRate = "EMRate";
        }

        if(leftDoorCommand)
        {
            if(currentSpeed > 0)
            {
                isSystemError = true;
            }else{
                isSystemError = false;
                leftDoorOpen = !leftDoorOpen;
            }
            leftDoorCommand = false;
        }
        if(rightDoorCommand)
        {
            if(currentSpeed > 0)
            {
                isSystemError = true;
            }else{
                isSystemError = false;
                rightDoorOpen = !rightDoorOpen;
            }
            rightDoorCommand = false;
        }
        if(lightCommand)
        {
            lightOn = !lightOn;
            lightCommand = false;
        }

        //commandSpeed < currentSpeed
        if(isEmergencyStop || isServiceBrake)
        {
            motorOff = true;
            //currentPower = 0.0;
            //return;
        } else {
            motorOff = false;
        }

        /* TODO: power Calculation */
        /*PID.setSetpoint(commandSpeed);
        Double powerCalculated = (totalWeight*Ton2Kg)*currentAccel*G*(currentSpeed*MPH2MS)*WATT2HP;
        powerCalculated = Math.round(powerCalculated * 100.0) / 100.0;

        if(powerCalculated > maxPower)
        {
            isPowerLimitReached = true;
            currentPower = Double.valueOf(maxPower);
        } else {
            currentPower = powerCalculated;
            isPowerLimitReached = false;
        }*/

    }

    public void Calc()
    {
        if(abs(currentSpeed)<EPS)
        {
            currentSpeed = 0.0;
        }
        Double speedMS = currentSpeed*MPH2MS;
        Double currentForce;

        if(currentPower >= EPS) {
            currentForce = currentPower / speedMS;
        } else {
            currentForce = 0.000;
        }
        currentForce -= totalWeight*G*NORMAL_BRAKE_RATE;
        System.out.println("speedMS:"+speedMS+" currentPower:"+currentPower+" Current Force:"+currentForce);
        Double accel = currentForce/(totalWeight*Ton2Kg);
        currentAccel = accel;
        speedMS = speedMS + accel*S;
        currentSpeed = speedMS/MPH2MS;
        parent.updateTrainDistance(id, currentSpeed);

        Double powerCalculated = 0.0;
        if(!motorOff) {
            powerCalculated = PID.getOutput(currentSpeed, commandSpeed);
        }

        System.out.println("Kp:"+Kp+" Ki:"+Ki);
        System.out.println("Cur:"+currentSpeed.toString()+" Cmd:"+commandSpeed.toString()+" P:"+powerCalculated.toString());

        if(powerCalculated > maxPower)
        {
            isPowerLimitReached = true;
            currentPower = Double.valueOf(maxPower);
        } else {
            currentPower = powerCalculated;
            isPowerLimitReached = false;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAutoMode() {
        return isAutoMode;
    }

    public void setAutoMode(Boolean autoMode) {
        isAutoMode = autoMode;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Boolean getHasCarError() {
        return hasCarError;
    }

    public void setHasCarError(Boolean hasCarError) {
        this.hasCarError = hasCarError;
    }

    public Boolean getHasBrakeError() {
        return hasBrakeError;
    }

    public void setHasBrakeError(Boolean hasBrakeError) {
        this.hasBrakeError = hasBrakeError;
    }

    public Boolean getHasPowerError() {
        return hasPowerError;
    }

    public void setHasPowerError(Boolean hasPowerError) {
        this.hasPowerError = hasPowerError;
    }

    public Boolean getHasError() {
        return hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public String getCarInput() {
        return carInput;
    }

    public void setCarInput(String carInput) {
        this.carInput = carInput;
    }

    public String getBrakeStatus() {
        return brakeStatus;
    }

    public void setBrakeStatus(String brakeStatus) {
        this.brakeStatus = brakeStatus;
    }

    public String getPowerStatus() {
        return powerStatus;
    }

    public void setPowerStatus(String powerStatus) {
        this.powerStatus = powerStatus;
    }

    public Integer getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(Integer maxPower) {
        this.maxPower = maxPower;
    }

    public Double getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(Double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public Double getCurrentAccel() {
        return currentAccel;
    }

    public void setCurrentAccel(Double currentAccel) {
        this.currentAccel = currentAccel;
    }

    public Double getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(Double currentPower) {
        this.currentPower = currentPower;
    }

    public String getCurrentBrakeRate() {
        return currentBrakeRate;
    }

    public void setCurrentBrakeRate(String currentBrakeRate) {
        this.currentBrakeRate = currentBrakeRate;
    }

    public String getNextStation() {
        return nextStation;
    }

    public void setNextStation(String nextStation) {
        this.nextStation = nextStation;
    }

    public Double getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(Double speedLimit) {
        this.speedLimit = speedLimit;
    }

    public Boolean getSpeedLimitReached() {
        return isSpeedLimitReached;
    }

    public void setSpeedLimitReached(Boolean speedLimitReached) {
        isSpeedLimitReached = speedLimitReached;
    }


    public Boolean getAuthorityError() {
        return isAuthorityError;
    }

    public void setAuthorityError(Boolean authorityError) {
        isAuthorityError = authorityError;
    }

    public Boolean getInYard() {
        return inYard;
    }

    public void setInYard(Boolean inYard) {
        this.inYard = inYard;
    }

    public Double getInputSpeed() {
        return inputSpeed;
    }

    public void setInputSpeed(Double inputSpeed) {
        this.inputSpeed = inputSpeed;
    }

    public Double getCommandSpeed() {
        return commandSpeed;
    }

    public void setCommandSpeed(Double commandSpeed) {
        this.commandSpeed = commandSpeed;
    }

    public Double getAuthority() {
        return authority;
    }

    public void setAuthority(Double authority) {
        this.authority = authority;
    }

    public Boolean getAuthorityEmergencyStop() {
        return authorityEmergencyStop;
    }

    public void setAuthorityEmergencyStop(Boolean authorityEmergencyStop) {
        this.authorityEmergencyStop = authorityEmergencyStop;
    }

    public Boolean getEmergencyCommand() {
        return emergencyCommand;
    }

    public void setEmergencyCommand(Boolean emergencyCommand) {
        this.emergencyCommand = emergencyCommand;
    }

    public Boolean getEmergencyStop() {
        return isEmergencyStop;
    }

    public void setEmergencyStop(Boolean emergencyStop) {
        isEmergencyStop = emergencyStop;
    }

    public Boolean getServiceBrake() {
        return isServiceBrake;
    }

    public void setServiceBrake(Boolean serviceBrake) {
        isServiceBrake = serviceBrake;
    }

    public Boolean getLeftDoorCommand() {
        return leftDoorCommand;
    }

    public void setLeftDoorCommand(Boolean leftDoorCommand) {
        this.leftDoorCommand = leftDoorCommand;
    }

    public Boolean getLeftDoorOpen() {
        return leftDoorOpen;
    }

    public void setLeftDoorOpen(Boolean leftDoorOpen) {
        this.leftDoorOpen = leftDoorOpen;
    }

    public Boolean getRightDoorCommand() {
        return rightDoorCommand;
    }

    public void setRightDoorCommand(Boolean rightDoorCommand) {
        this.rightDoorCommand = rightDoorCommand;
    }

    public Boolean getRightDoorOpen() {
        return rightDoorOpen;
    }

    public void setRightDoorOpen(Boolean rightDoorOpen) {
        this.rightDoorOpen = rightDoorOpen;
    }

    public Boolean getLightCommand() {
        return lightCommand;
    }

    public void setLightCommand(Boolean lightCommand) {
        this.lightCommand = lightCommand;
    }

    public Boolean getLightOn() {
        return lightOn;
    }

    public void setLightOn(Boolean lightOn) {
        this.lightOn = lightOn;
    }

    public Boolean getSystemError() {
        return isSystemError;
    }

    public void setSystemError(Boolean systemError) {
        isSystemError = systemError;
    }

    public Boolean getPowerLimitReached() {
        return isPowerLimitReached;
    }

    public void setPowerLimit(Boolean powerLimit) {
        isPowerLimitReached = powerLimit;
    }
}