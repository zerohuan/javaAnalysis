package com.net;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Server骨架类
 * Created by yjh on 15-12-1.
 */
public abstract class AbstractServer implements Server {
    protected static final String EOF = "EOF";

    protected volatile AtomicBoolean isDone = new AtomicBoolean(false);
    protected final int port;
    protected final boolean isNIO; //false使用BIO

    protected AbstractServer(int port, boolean isNIO) {
        this.port = port;
        this.isNIO = isNIO;

    }
}
