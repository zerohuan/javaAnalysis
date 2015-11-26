package com.designmodel.commandModel.standard;

/**
 * 具体的命令
 *
 * Created by yjh on 15-11-25.
 */
public class ConcreteCommand implements Command {
    private Receiver receiver;

    @Override
    public void execute() {
        if(receiver != null)
            receiver.action();
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }
}
