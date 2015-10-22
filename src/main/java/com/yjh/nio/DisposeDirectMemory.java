package com.yjh.nio;

import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

/**
 * Created by yjh on 15-10-22.
 */
public class DisposeDirectMemory {

    public static void main(String[] args) throws Exception{
        //释放allocateDirect分配的内存
        TimeUnit.SECONDS.sleep(5);
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024 * 500);
        TimeUnit.SECONDS.sleep(5);
        Cleaner cleaner = ((DirectBuffer)buffer).cleaner();
        cleaner.clean();

        //释放mapped
        TimeUnit.SECONDS.sleep(5);
        FileChannel fileChannel = new FileInputStream("/home/yjh/test.file").getChannel();
        MappedByteBuffer buffer1 = fileChannel.map(FileChannel.MapMode.READ_ONLY,
                0, fileChannel.size());
        TimeUnit.SECONDS.sleep(5);

        Cleaner cleaner1 = ((DirectBuffer)buffer1).cleaner();
        cleaner1.clean();
        fileChannel.close();
        TimeUnit.SECONDS.sleep(5);
    }
}
