import java.util.ArrayList;
import java.util.BitSet;

public class TrainPool {
    Central theCentral;
    ArrayList<TrainModel> trains;
    TrainControllerUISwing controllerUI;
    TrainModelUISwing modelUI;
    public TrainPool(TrainControllerUISwing controllerUI, TrainModelUISwing modelUI, Central central)
    {
        trains = new ArrayList<>();
        this.controllerUI = controllerUI;
        this.modelUI = modelUI;
        this.theCentral = central;
        controllerUI.trains = trains;
        modelUI.trains = trains;
        initialize();
    }

    public TrainPool(Central central)
    {
        trains = new ArrayList<>();
        this.theCentral = central;
        System.out.println("Created Train Pool");
    }

    private void initialize()
    {
        controllerUI.update();
        modelUI.update();
    }

    private Integer findTrainIndexById(Integer id)
    {
        for(int i=0; i<trains.size(); i++)
        {
            if(trains.get(i).id.equals(id))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Create a new train in the pool
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean createNewTrain(Integer id)
    {
        if(findTrainIndexById(id) != -1)
        {
            return false;
        }

        TrainModel a_train = new TrainModel(id, this.theCentral);
        if(this.modelUI == null || this.controllerUI == null)
        {
            System.out.print("Please open TrainModel and Controller BEFORE creating train!!!");
            return false;
        }
        a_train.ui = this.modelUI;
        TrainController a_controller = new TrainController(a_train, this.theCentral);
        a_controller.ui = this.controllerUI;
        a_train.controller = a_controller;
        trains.add(a_train);

        controllerUI.addTrainToSelector(id);
        modelUI.addTrainToSelector(id);

        return true;
    }

    /**
     * Remove the train of certain id
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean removeTrain(Integer id)
    {
        Integer index = findTrainIndexById(id);
        if(index == -1)
        {
            return false;
        }

        controllerUI.removeTrainFromSelector(index);
        modelUI.removeTrainFromSelector(index);

        trains.remove(index);
        return true;
    }

    /**
     * Set inYard status of the train.
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean setInYard(Integer id, Boolean inYard)
    {
        Integer index = findTrainIndexById(id);
        if(index == -1)
        {
            return false;
        }
        trains.get(index).inYard = inYard;
        controllerUI.update();
        modelUI.update();
        return true;
    }

    /**
     * Set the speed limit of the train (in mph).
     * Command speed can not exceed speed limit
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean setSpeedLimit(Integer id, Double speedLimit)
    {
        Integer index = findTrainIndexById(id);
        if(index == -1 || speedLimit <0)
        {
            return false;
        }
        trains.get(index).controller.setSpeedLimit(speedLimit);
        controllerUI.update();
        modelUI.update();
        return true;
    }

    /**
     * Set the command speed of the train (in mph).
     * In auto mode, command speed will be the target speed for the train.
     * In manual mode, target speed can not exceed command speed.
     * The controller will check speed limit before setting.
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean setCommandSpeed(Integer id, Double commandSpeed)
    {
        System.out.println("Train Pool sending command speed "+commandSpeed+" to "+id);
        Integer index = findTrainIndexById(id);
        if(index == -1 || commandSpeed <0)
        {
            return false;
        }
        trains.get(index).controller.setCommandSpeed(commandSpeed);
        controllerUI.update();
        modelUI.update();
        return true;
    }

    /**
     * Set authority distance of the train (in miles).
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean setAuthority(Integer id, Double authority)
    {
        System.out.println("Train Pool sending authority "+authority+" to "+id);
        Integer index = findTrainIndexById(id);
        System.out.print(index.toString());
        if(index == -1 || authority < 0)
        {
            return false;
        }
        trains.get(index).setAuthority(authority);
        // ModelUI and controllerUI will be updated in setAuthority
        return true;
    }

    /**
     * Set angle of slope (in radian).
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean setGrade(Integer id, Double grade)
    {
        System.out.println("Train Pool sending grade: "+grade+" to "+id);
        Integer index = findTrainIndexById(id);
        System.out.print(index.toString());
        if(index == -1)
        {
            return false;
        }
        trains.get(index).setSlope(grade);
        return true;
    }

    /**
     * Set total weight of the train (in tons).
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean setTotalWeight(Integer id, Double weight)
    {
        Integer index = findTrainIndexById(id);
        if(index == -1 || weight <=0)
        {
            return false;
        }
        trains.get(index).setTotalWeight(weight);
        // ModelUI and controllerUI will be updated in setTotalWeight
        return true;
    }

    /**
     * Set max power of the train (in kW).
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean setMaxPower(Integer id, Double maxPower)
    {
        Integer index = findTrainIndexById(id);
        if(index == -1 || maxPower <0)
        {
            return false;
        }
        trains.get(index).setMaxPower(maxPower);
        // ModelUI and controllerUI will be updated in setMaxPower
        return true;
    }

    /**
     * Set power error state of the train.
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean setPowerError(Integer id, Boolean hasError)
    {
        Integer index = findTrainIndexById(id);
        if(index == -1)
        {
            return false;
        }
        trains.get(index).setHasPowerError(hasError);
        return true;
    }

    /**
     * Set brake error state of the train.
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean setBrakeError(Integer id, Boolean hasError)
    {
        Integer index = findTrainIndexById(id);
        if(index == -1)
        {
            return false;
        }
        trains.get(index).setHasBrakeError(hasError);
        return true;
    }

    /**
     * Set brake error state of the train (in kW).
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean setCarError(Integer id, String errorString)
    {
        Integer index = findTrainIndexById(id);
        if(index == -1)
        {
            return false;
        }
        trains.get(index).setHasCarError(!errorString.equals("OK"), errorString);
        return true;
    }


    /**
     * Set emergency stop state from outside.
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean emegencyStop(Integer id, Boolean emegencyStopState)
    {
        System.out.println("Train Pool sending emergency stop: "+emegencyStopState+" to "+id);
        Integer index = findTrainIndexById(id);
        if(index == -1)
        {
            return false;
        }
        trains.get(index).controller.setAuthorityEmergencyStop(emegencyStopState);
        return true;
    }

    /**
     * Trigger left door switching command. The controller of the train will check before execute.
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean openLeftDoor(Integer id)
    {
        Integer index = findTrainIndexById(id);
        if(index == -1)
        {
            return false;
        }
        trains.get(index).controller.setLeftDoorCommand(true);
        return true;
    }

    /**
     * Trigger right door switching command. The controller of the train will check before execute.
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean openRightDoor(Integer id)
    {
        Integer index = findTrainIndexById(id);
        if(index == -1)
        {
            return false;
        }
        trains.get(index).controller.setRightDoorComand(true);
        return true;
    }

    /**
     * Whether the train is in tunnel?
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean inTunnel(Integer id, Boolean inTunnel)
    {
        System.out.println("Train Pool sending in tunnel info: "+inTunnel+" to "+id);
        Integer index = findTrainIndexById(id);
        if(index == -1)
        {
            return false;
        }
        trains.get(index).controller.setLightCommand(inTunnel);
        return true;
    }

    /**
     * Send the beacon data to the train
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean setBeaconData(Integer id, BitSet beacon)
    {
        System.out.println("Train Pool sending beacon: "+beacon+" to "+id);
        Integer index = findTrainIndexById(id);
        if(index == -1)
        {
            return false;
        }
        trains.get(index).processBeacon(beacon);
        return true;
    }

    /**
     * Send the number of passengers get on the train
     *
     * @author Yincheng He
     * @return succeed?
     *
     */
    public Boolean setPassengerGetOn(Integer id, Integer num)
    {
        System.out.println("Train Pool sending passenger getting on: "+num+" to "+id);
        Integer index = findTrainIndexById(id);
        if(index == -1)
        {
            return false;
        }
        trains.get(index).setPassengerGetOn(num);
        return true;
    }

    /*
    *  Step for one time interval
    *
    *  @author Yincheng He
    * */
    public void Step()
    {
        trains.forEach(t -> t.step());
    }
}
