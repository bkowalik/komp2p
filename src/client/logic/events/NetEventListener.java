package client.logic.events;


import java.util.EventListener;

public interface NetEventListener extends EventListener {
    public void onMessageIncome();
}
