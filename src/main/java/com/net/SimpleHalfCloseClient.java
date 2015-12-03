package com.net;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * 支持版关闭的简单服务器，用于测试服务器连接
 *
 * Socket关于半关闭的4个方法：
 * shutdownOutput：关闭输出流
 * shutdownInput：关闭输入流
 * isOutputStream；
 * isInputStream；
 *
 * Created by yjh on 15-12-1.
 */
public class SimpleHalfCloseClient {
    //超时时间5秒
    private static final int TIME_OUT = 5000;
    private String host;
    private int port;

    public SimpleHalfCloseClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void init() throws IOException {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), TIME_OUT);

            try (Scanner in = new Scanner(socket.getInputStream());
                 PrintWriter out = new PrintWriter(socket.getOutputStream())) {
                out.println("request DATA");
                out.flush(); //手动刷新缓存区

                socket.shutdownOutput();

                while(in.hasNextLine()) {
                    System.out.println(in.nextLine());
                }
                socket.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        SimpleHalfCloseClient client = new SimpleHalfCloseClient("localhost", 8189);
        client.init();
    }
}
