public class TrainController {
    public static final Double MILE2M = 1609.34; // a mile => meters
    public static final Double MPH2MS = 0.44704;
    public static final Double EPS = 0.00001;
    Central theCentral;
    TrainModel model;
    TrainControllerUISwing ui;

    Double targetSpeed = 0.0;
    Double displayTargetSpeed = 0.0;
    Double commandSpeed = 0.0;
    Double displayCommandSpeed = 0.0;
    Double speedLimit = 70.0*MPH2MS;
    Double displaySpeedLimit = 70.0;

    Double vIntegration = 0.0, vIntegrationPrev = 0.0;
    Double vError = 0.0, vErrorPrev = 0.0;
    Double Kp = 100.0, Ki = 2.0;
    Boolean motorOff = false;
    Double powerOutput = 0.0;

    Boolean isAutoMode = true;
    Boolean autoModeOverride = false;

    Boolean systemError = false;
    Boolean exceedSpeedLimit = false;
    Boolean authorityError = false;
    Boolean authorityEmergencyStop = false;

    //commands
    Boolean leftDoorCommand = false;
    Boolean rightDoorComand = false;
    Boolean lightCommand = false;
    //emergency stop switch on controller
    Boolean emergencyBrakeCommand = false;
    //service brake switch on controller
    Boolean serviceBrakeCommand = false;

    public TrainController(TrainModel model, Central central)
    {
        this.model = model;
        this.theCentral = central;
    }

    public void update() {
        if(model == null)
        {
            return;
        }
        Boolean hasError = model.hasBrakeError || model.hasCarError || model.hasPowerError;
        autoModeOverride = hasError || model.onAuthorityBrake || authorityEmergencyStop;

        if(!isAutoMode)
        {
            if(serviceBrakeCommand)
            {
                model.serviceBrakeActive = true;
            }else{
                model.serviceBrakeActive = false;
            }
        }

        if(hasError || authorityEmergencyStop || emergencyBrakeCommand || model.emergencyBrakeCommand)
        {
            model.emergencyStopActive = true;
        } else {
            model.emergencyStopActive = false;
        }

        //System.out.println("isAutoMode: "+isAutoMode.toString());
        //System.out.println("AutoModeOverrride: "+autoModeOverride.toString());

        if(autoModeOverride) {
            isAutoMode = true;
        }
        if(isAutoMode) {
            targetSpeed = commandSpeed;
        }
        // Outside signals swiching doors
        if(leftDoorCommand)
        {
            // Close door operation is always OK
            if(model.leftDoorOpen)
            {
                model.leftDoorOpen = false;
            }else{
                // No way if train is moving
                if(model.currentSpeed >= EPS)
                {
                    systemError = true;
                }else{
                    model.leftDoorOpen = true;
                }
            }
            leftDoorCommand = false;
        }
        if(rightDoorComand)
        {
            if(model.rightDoorOpen)
            {
                model.rightDoorOpen = false;
            }else{
                if(model.currentSpeed >= EPS)
                {
                    systemError = true;
                }else{
                    model.rightDoorOpen = true;
                }
            }
            rightDoorComand = false;
        }
        if(lightCommand)
        {
            model.lightOn = !model.lightOn;
            lightCommand = false;
        }
        convertDisplayUnit();
        updateUI();
        //update model UI here
        model.updateUI();
    }

    private void convertDisplayUnit()
    {
        displaySpeedLimit = speedLimit/MPH2MS;
        displayCommandSpeed = commandSpeed/MPH2MS;
        displayTargetSpeed = targetSpeed/MPH2MS;
    }

    public void updateUI()
    {
        convertDisplayUnit();
        if(ui != null)
        {
            ui.update();
        }
    }

    // in mph
    void setCommandSpeed(Double commandSpeed) {
        //reset indicator
        exceedSpeedLimit = false;
        authorityError = false;
        if(authorityEmergencyStop){
            authorityError = true;
            return;
        }
        if(commandSpeed > displaySpeedLimit)
        {
            exceedSpeedLimit = true;
        }else {
            this.commandSpeed = commandSpeed * MPH2MS;
        }
        update();
    }

    // in mph
    void setSpeedLimit(Double speedLimit) {
        //reset indicator
        exceedSpeedLimit = false;
        this.displaySpeedLimit = speedLimit;
        this.speedLimit = speedLimit * MPH2MS;
        update();
    }

    void setLeftDoorCommand(Boolean leftDoorCommand) {
        this.leftDoorCommand = leftDoorCommand;
        update();
    }

    void setRightDoorComand(Boolean rightDoorComand) {
        this.rightDoorComand = rightDoorComand;
        update();
    }

    void setLightCommand(Boolean lightCommand) {
        this.lightCommand = lightCommand;
        update();
    }

    void emergencyCommand() {
        emergencyBrakeCommand = !emergencyBrakeCommand;
        update();
    }

    void setAuthorityEmergencyStop(Boolean authorityEmergencyStop) {
        this.authorityEmergencyStop = authorityEmergencyStop;
        update();
    }

    //in mph
    void setInputSpeed(Double inputSpeed)
    {
        //reset indicator
        exceedSpeedLimit = false;
        if(inputSpeed > displayCommandSpeed)
        {
            exceedSpeedLimit = true;
        }else{
            targetSpeed = inputSpeed * MPH2MS;
        }
        update();
    }

    Double getPower(Double currentSpeed, Double interval)
    {
        vError = this.targetSpeed - currentSpeed;
        vIntegration = vIntegrationPrev + ((interval / 2) * (vError + vErrorPrev));
        powerOutput = (Kp * vError) + (Ki * vIntegration);
        if(powerOutput > model.maxPower)
        {
            vIntegration = vIntegrationPrev;
            powerOutput = model.maxPower;
            model.powerLimitReached = true;
        } else {
            model.powerLimitReached = false;
        }
        vErrorPrev = vError;
        vIntegrationPrev = vIntegration;
        powerOutput = powerOutput<0?0:powerOutput;
        motorOff = model.serviceBrakeActive || model.emergencyStopActive
                || this.targetSpeed <= EPS;
        if(motorOff || model.hasPowerError)
        {
            model.powerLimitReached = false;
            powerOutput = 0.0;
        }
        return powerOutput;
    }
}
