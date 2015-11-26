package com.jvm.showByteCode;

/**
 * Created by yjh on 15-11-25.
 */
public class SynchronizedByteCode {
    private void t() {
        synchronized (this) {
            System.out.println("同步测试");
        }
    }
}
