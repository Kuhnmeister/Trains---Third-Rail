package train;

public class TrainMain {
    public static void main(String[] args) {
        TrainPool trainPool;
        System.out.println("Hello!");
        TrainControllerUI cui = TrainControllerUI.createTrainControllerGUI();
        TrainModelUI mui = TrainModelUI.createTrainModelGUI();
        trainPool = new TrainPool(cui, mui);
        TrainControllerTestbench testbench = new TrainControllerTestbench(trainPool);
    }
}
