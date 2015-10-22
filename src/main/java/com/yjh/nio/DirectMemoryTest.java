package com.yjh.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.function.Consumer;

/**
 * Test allocateDirect and map()
 *
 * Created by yjh on 15-10-22.
 */
public class DirectMemoryTest {
    private static final String TEST_FILE = "/home/yjh/test.file";

    public static void testNormal(String TEST_FILE) {
        System.out.print("Normal ");
        try(FileInputStream inputStream = new FileInputStream(TEST_FILE);
            FileChannel fileChannel = inputStream.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while(fileChannel.read(buffer) != -1) {
                buffer.flip();
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testDirect(String TEST_FILE) {
        System.out.print("Direct ");
        try(FileInputStream inputStream = new FileInputStream(TEST_FILE);
            FileChannel fileChannel = inputStream.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            while(fileChannel.read(buffer) != -1) {
                buffer.flip();
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testMapped(String TEST_FILE) {
        System.out.print("Mapped ");
        try(FileInputStream inputStream = new FileInputStream(TEST_FILE);
            FileChannel fileChannel = inputStream.getChannel()) {
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            byte[] b = new byte[1024];
            while(buffer.get(b).position() < buffer.limit()) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void test(Consumer<String> consumer) {
        long startTime = System.currentTimeMillis();

        consumer.accept(TEST_FILE);

        long endTime = System.currentTimeMillis();

        System.out.println("Time consume: " + (endTime - startTime));
    }

    public static void main(String[] args) {
        test(DirectMemoryTest::testNormal);
        test(DirectMemoryTest::testDirect);
        test(DirectMemoryTest::testMapped);
    }
}
