package com.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 非阻塞IO
 * Created by yjh on 15-12-1.
 */
public class NioServer extends AbstractServer {
    private static final Logger logger = LogManager.getLogger();

    private final ServerSocketChannel serverChannel;
    private final Selector selector;
    private static final int BufferSize = 4096;
    private static final int TIME_OUT = 3000;

    public NioServer(int port, boolean isNIO) {
        super(port, isNIO);
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(!isNIO);
        } catch (IOException e) {
            throw new RuntimeException("serverSocket created fail.");
        }
    }

    @Override
    public void start() throws IOException {
        serverChannel.socket().bind(new InetSocketAddress(port));
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        run();
    }

    @Override
    public void close() throws Exception {
        try {
            serverChannel.close();
        } finally {
            isDone.compareAndSet(false, true);
        }
    }

    @Override
    public void run() {
        Protocol protocol = new NioProtocol(BufferSize);
        while(!isDone.get()) {
            Iterator<SelectionKey> iterator = null;
            try {
                if(selector.select(TIME_OUT) == 0) continue;

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for(iterator = selectionKeys.iterator(); iterator.hasNext();) {
                    SelectionKey key = iterator.next();
                    if(key.isAcceptable()) {
                        protocol.handleAccept(key);
                    } else if(key.isConnectable()) {
                        System.out.println("doSomething when be able to connect");
                    } else if(key.isReadable()) {
                        protocol.handleRead(key);
                    } else if(key.isWritable()) {
                        protocol.handleWrite(key);
                    }
                    iterator.remove(); //注意要从就绪集合中删除，下次就绪有selector添加
                }
            } catch (Exception e) {
                logger.error(e);
                if(iterator != null)
                    iterator.remove();
            }
        }
    }

}
