package com.concurrent.lock;

/**
 * Created by yjh on 15-12-23.
 */
public class LockAndException {
    private static final Object mutex = new Object();

    //最基本的语义实现
    private void synchronizedAndException() {
        synchronized (mutex) {
            //异常并不会影响锁的释放
            throw new IllegalStateException();
        }
    }
    /*
    0: getstatic     #2                  // Field mutex:Ljava/lang/Object;
         3: dup
         4: astore_1
         5: monitorenter
         6: new           #3                  // class java/lang/IllegalStateException
         9: dup
        10: invokespecial #4                  // Method java/lang/IllegalStateException."<init>":()V
        13: athrow
        14: astore_2
        15: aload_1
        16: monitorexit
        17: aload_2 //抛出异常是在退出同步块之后
        18: athrow

         Exception table:
         from    to  target type
             6    17    14   any
     */



    public static void main(String[] args) {

    }
}
