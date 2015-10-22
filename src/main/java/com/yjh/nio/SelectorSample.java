package com.yjh.nio;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by yjh on 15-10-21.
 */
public class SelectorSample {
    private List<SelectableChannel> channels;
    private boolean isListening = true;

    public SelectorSample(List<SelectableChannel> channels) {
        this.channels = channels;
    }

    public void doHandle() {
        try(Selector selector = Selector.open()) {
            for(SelectableChannel channel : channels) {
                channel.configureBlocking(false); //非阻塞
                channel.register(selector, SelectionKey.OP_ACCEPT | SelectionKey.OP_CONNECT
                        | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            }
            while(isListening) {
                int ready = selector.select();
                if(ready == 0) continue;
                Set<SelectionKey> selectionKeys = selector.keys();
                for(Iterator<SelectionKey> iterator = selectionKeys.iterator(); iterator.hasNext();) {
                    SelectionKey key = iterator.next();
                    if(key.isAcceptable()) {
                        System.out.println("doSomething when be acceptable");
                    } else if(key.isConnectable()) {
                        System.out.println("doSomething when be able to connect");
                    } else if(key.isReadable()) {
                        System.out.println("doSomething when be readable");
                    } else if(key.isWritable()) {
                        System.out.println("doSomething when be writable");
                    }
                    iterator.remove(); //注意要从就绪集合中删除，下次就绪有selector添加
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
