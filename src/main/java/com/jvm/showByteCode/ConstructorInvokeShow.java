package com.jvm.showByteCode;

/**
 * Created by yjh on 15-12-9.
 */
public class ConstructorInvokeShow {
    static class A {}

    private int x = 1;

    public static void main(String[] args) {
        ConstructorInvokeShow invokeShow = new ConstructorInvokeShow();
    }

    /* 调用构造器
    Code:
      stack=2, locals=2, args_size=1
         0: new           #3                  //这时已经返回了objectreference class com/jvm/showByteCode/ConstructorInvokeShow
         3: dup
         4: invokespecial #4                  // Method "<init>":()V
         7: astore_1
         8: return
     */
}
