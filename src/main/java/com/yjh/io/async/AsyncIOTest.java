package com.yjh.io.async;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.TimeUnit;

/**
 * Created by yjh on 16-2-13.
 */
public class AsyncIOTest {
    /*
    java.nio.channels.AsynchronousServerSocketChannel;
    java.nio.channels.AsynchronousSocketChannel;
    java.nio.channels.CompletionHandler;
     */
    private static final int PORT = 8081;
    private static final int BUFFER_SIZE = 2048;
    private static final CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();

    private int port = PORT;
    private AsynchronousServerSocketChannel serverChannel;

    public AsyncIOTest(int port) throws IOException {
        this.port = port;
//        this.decoder = Charset.forName(CHARSET).newDecoder();
    }

    public void listen() throws IOException {
        this.serverChannel = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port), 100);
        this.serverChannel.accept(this, new AcceptHandler());
        System.out.println("main thread: " + Thread.currentThread());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        TimeUnit.SECONDS.sleep(5);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void close() throws IOException {
        if (serverChannel != null) {
            serverChannel.close();
        }
    }

    private static class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncIOTest> {
        @Override
        public void completed(AsynchronousSocketChannel client, AsyncIOTest attachment) {
            try {
                System.out.println("远程地址：" + client.getRemoteAddress());
                System.out.println("Accept Thread: " + Thread.currentThread());
                //tcp各项参数
                client.setOption(StandardSocketOptions.TCP_NODELAY, true);
                client.setOption(StandardSocketOptions.SO_SNDBUF, 1024);
                client.setOption(StandardSocketOptions.SO_RCVBUF, 1024);

                if (client.isOpen()) {
                    System.out.println("client.isOpen：" + client.getRemoteAddress());
                    final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                    buffer.clear();
                    client.read(buffer, client, new ReadHandler(buffer, attachment));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                attachment.serverChannel.accept(attachment, this);// 监听新的请求，递归调用。
            }
        }

        @Override
        public void failed(Throwable exc, AsyncIOTest attachment) {
            try {
                exc.printStackTrace();
            } finally {
                attachment.serverChannel.accept(attachment, this);// 监听新的请求，递归调用。
            }
        }
    }

    private static class ReadHandler implements CompletionHandler<Integer, AsynchronousSocketChannel> {
        private ByteBuffer buffer;
        private AsyncIOTest server;

        public ReadHandler(ByteBuffer buffer, AsyncIOTest server) {
            this.buffer = buffer;
            this.server = server;
        }

        @Override
        public void completed(Integer result, AsynchronousSocketChannel attachment) {
            try {
                if (result < 0) {// 客户端关闭了连接
                    attachment.close();
                } else if (result == 0) {
                    System.out.println("空数据"); // 处理空数据
                } else {
                    // 读取请求，处理客户端发送的数据
                    buffer.flip();
                    CharBuffer charBuffer = decoder.decode(buffer);
                    System.out.println(charBuffer.toString()); //接收请求

                    //响应操作，服务器响应结果
                    buffer.clear();
                    String res = "HTTP/1.1 200 OK" + "\r\n\r\n" + "hellworld"; //CRLF
                    buffer = ByteBuffer.wrap(res.getBytes());
                    attachment.write(buffer, attachment, new WriteHandler(buffer, server));//Response：响应。
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class WriteHandler implements CompletionHandler<Integer, AsynchronousSocketChannel> {
        private ByteBuffer buffer;
        private AsyncIOTest server;

        public WriteHandler(ByteBuffer buffer, AsyncIOTest server) {
            this.buffer = buffer;
            this.server = server;
        }

        @Override
        public void completed(Integer result, AsynchronousSocketChannel attachment) {
            buffer.clear();
            try {
                attachment.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
            exc.printStackTrace();
            try {
                attachment.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("正在启动服务...");
            AsyncIOTest server = new AsyncIOTest(PORT);
            server.listen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
