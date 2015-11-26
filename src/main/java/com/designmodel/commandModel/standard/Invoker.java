package com.designmodel.commandModel.standard;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 调用者
 *
 * Created by yjh on 15-11-25.
 */
public class Invoker {
    //命令队列
    private final Queue<Command> queue = new PriorityQueue<>();

    public static void main(String[] args) {
        Thread t = new Thread();
    }
}
