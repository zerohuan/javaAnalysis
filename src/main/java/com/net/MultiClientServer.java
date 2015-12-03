package com.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 简单服务器
 * 支持NIO和BIO选择
 *
 * Created by yjh on 15-12-1.
 */
public final class MultiClientServer implements Server {
    private static final Logger logger = LogManager.getLogger();
    private Server server;

    private MultiClientServer(int port, boolean isNIO) {
        server = isNIO ? new NioServer(port, true) : new BioServer(port);
    }

    @Override
    public void run() {
        server.run();
    }

    @Override
    public void start() throws IOException {
        logger.info("多线程服务器启动");
        server.start();
    }

    @Override
    public void close() {
        try {
            server.close();
        } catch (Exception e){
            logger.error(e);
        } finally {
            logger.info("服务器关闭");
        }
    }

    private static class InnerClass {
        //延时加载，固定大小10的线程池
        private static final ExecutorService executor = Executors.newFixedThreadPool(10);
    }


    public static void main(String[] args) throws Exception {
        MultiClientServer server = new MultiClientServer(8190, true);
        server.start();
    }
}
