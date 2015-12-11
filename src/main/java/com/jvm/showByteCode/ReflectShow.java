package com.jvm.showByteCode;

import java.io.PrintStream;
import java.io.Serializable;

/**
 *
 * Created by yjh on 15-12-7.
 */
public class ReflectShow {

    public static void main(String[] args) {
        PrintStream out = System.out;
        Object a = new Object();
        boolean flag = a instanceof String;

        Serializable serializable = 1;
        boolean flag2 = serializable instanceof Comparable;

        out.println(flag2);
    }
}
