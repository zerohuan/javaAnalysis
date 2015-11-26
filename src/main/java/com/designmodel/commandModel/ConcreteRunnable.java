package com.designmodel.commandModel;

/**
 * 命令模式：具体的命令
 *
 * Created by yjh on 15-11-25.
 */
public class ConcreteRunnable implements Runnable {
    private Receiver receiver;

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void run() {
        if(receiver != null)
            receiver.action();
    }
}
