package com.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * 简单Java服务器
 *
 * Created by yjh on 15-12-1.
 */
public final class SimpleServer {
    private static final int PORT = 8189;
    private static final String END_TAG = "EOF";
    private static final String ACCEPTED_MSG =
            "Accepted Successful, Send '" + END_TAG + "' to exit";

    public void init() throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            try(Socket socket = serverSocket.accept()) { //LISTEN
                InputStream inputStream = socket.getInputStream();//SYN_RCVD ESTABLISHED
                OutputStream outputStream = socket.getOutputStream();

                try(Scanner in = new Scanner(inputStream);
                    PrintWriter out = new PrintWriter(outputStream, true)) {
                    out.println(ACCEPTED_MSG);

                    boolean isDone = false;
                    while(!isDone && in.hasNextLine()) {
                        String line = in.nextLine();
                        out.println("Echo: " + line);
                        if(line.trim().equals(END_TAG)) isDone = true;
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        SimpleServer server = new SimpleServer();
        server.init();
    }
}
