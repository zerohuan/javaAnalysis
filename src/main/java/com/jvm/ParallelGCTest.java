package com.jvm;

/**
 * 测试 并行回收器
 * -Xms20M -Xmx20M -Xmn10M -XX:+UseParallelGC -XX:+PrintGCDetails
 *
 * TODO stop the world 发生的时机，OOM发生的时机，不同GC方法的参数调节
 *
 * Created by yjh on 15-10-25.
 */
public class ParallelGCTest {
    public static void main(String[] args) {
        int m = 1024 * 1024;
        byte[] b = new byte[2 * m];
        byte[] b2 = new byte[2 * m];
        byte[] b3 = new byte[2 * m];
        byte[] b4 = new byte[2 * m];
        byte[] b5 = new byte[2 * m];
        byte[] b6 = new byte[2 * m];
        byte[] b7 = new byte[2 * m];

    }

}
