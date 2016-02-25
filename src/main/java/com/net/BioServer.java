package com.net;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 阻塞IO线程池
 * Created by yjh on 15-12-1.
 */
public class BioServer extends AbstractServer {
    private static final Logger logger = LogManager.getLogger();
    private ServerSocket serverSocket;

    public BioServer(int port) {
        super(port, false);
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("serverSocket created fail.");
        }
    }

    @Override
    public void start() {
        run();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = this.serverSocket) {
            while(!isDone.get()) {
                Socket incoming = serverSocket.accept();
                ThreadPoolHolder.executor.execute(new SocketHandler(incoming));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 静态内部类，包含一个线程池实例，延迟加载
     */
    private static class ThreadPoolHolder {
        //延时加载，固定大小10的线程池
        private static final ExecutorService executor = Executors.newFixedThreadPool(10);
    }

    private static class SocketHandler implements Runnable {
        private Socket incoming;

        public SocketHandler(Socket incoming) {
            this.incoming = incoming;
            logger.info(Thread.currentThread().getName() + "'s connection is established");
        }

        @Override
        public void run() {
            try {
                try(Socket incoming = this.incoming) {
                    InputStream inputStream = incoming.getInputStream();
                    OutputStream outputStream = incoming.getOutputStream();

                    try(Scanner in = new Scanner(inputStream);
                        PrintWriter out = new PrintWriter(outputStream, true)) {
                        out.println("Accepted request, send '" + EOF  + "' to exit");

                        boolean isDone = false;
                        String line;
                        while(!isDone && in.hasNextLine()) {
                            line = in.nextLine();
                            out.println("Echo: " + line);
                            if(line.trim().equalsIgnoreCase(EOF) || StringUtils.isEmpty(line.trim())) isDone = true;
                        }
                    }
                }
            } catch (IOException e) {
                logger.error(e);
            }
            logger.info(Thread.currentThread().getName() + "'s connection is closed");
        }
    }

    @Override
    public void close() {
        while(isDone.compareAndSet(false, true)) {
            ThreadPoolHolder.executor.shutdownNow();
        }
    }

    public static void main(String[] args) {
        BioServer server = new BioServer(8081);
        server.start();
    }
}
