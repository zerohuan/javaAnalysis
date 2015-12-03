package com.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * 基于TCP的非阻塞io处理协议
 *
 * Created by yjh on 15-12-1.
 */
public class NioProtocol implements Protocol {
    private static final Logger logger = LogManager.getLogger();

    private final int bufferSize; //缓冲区大小

    public NioProtocol(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public void handleAccept(SelectionKey key) throws IOException {
        SocketChannel channel = ((ServerSocketChannel)key.channel()).accept();
        channel.configureBlocking(false);
        channel.register(key.selector(), SelectionKey.OP_READ,
                ByteBuffer.allocate(bufferSize));
        logger.info(channel.getRemoteAddress() + "'s connection is established.");
    }

    @Override
    public void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        //创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        buffer.clear();
        long len = 0;
        StringBuilder sb = new StringBuilder();
        Charset charset = Charset.forName("UTF8");
        while ((len = channel.read(buffer)) > 0) {
            buffer.flip();
            sb.append(charset.decode(buffer).toString());
            buffer.clear();
        }
        logger.debug(sb.toString());
        if (sb.length() == 0) {
            logger.info(channel.getRemoteAddress() + "'s connection is closed.");
            channel.close();
        } else {
            //TODO 抽象出事件处理机制，类似Servlet的回调，分发给特定的处理点
            byte[] bytes = sb.toString().getBytes("UTF8");
            ByteBuffer responseBuffer = ByteBuffer.allocate(bytes.length);
            responseBuffer.put(bytes);
            responseBuffer.flip();
            channel.write(responseBuffer);
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
    }

    @Override
    public void handleWrite(SelectionKey key) throws IOException {

    }
}
