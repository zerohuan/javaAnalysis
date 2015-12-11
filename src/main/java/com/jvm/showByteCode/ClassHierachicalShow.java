package com.jvm.showByteCode;

/**
 * Created by yjh on 15-12-9.
 */
public class ClassHierachicalShow {
    private static class SuperClass {
        protected static int j = 1;
        private int i = 9;

        public SuperClass() {
            int x = 0;
        }
    }

    private static class SubClass extends SuperClass {
        private static int n = j + 1;
        private int i = 10;

        public SubClass() {
            double x = 9;
        }
    }

    /* 类初始化
    static {};
    descriptor: ()V
    flags: ACC_STATIC
    Code:
      stack=2, locals=0, args_size=0
         0: getstatic     #5                  // Field j:I
         3: iconst_1
         4: iadd
         5: putstatic     #6                  // Field n:I
         8: return
      LineNumberTable:
        line 17: 0

     */
}
