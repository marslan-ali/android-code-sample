package co.appdev.invited.util;

public class MainBus extends RxEventBus {

    private static MainBus instance;

    public static MainBus getInstance() {
        if (instance == null)
            instance = new MainBus();
        return instance;
    }

    private MainBus() {
    }

}
