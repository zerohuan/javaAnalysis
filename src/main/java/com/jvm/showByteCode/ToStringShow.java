package com.jvm.showByteCode;

/**
 * Created by yjh on 15-12-18.
 */
public class ToStringShow {
    private int a = 1;
    private int b = 2;
    private int c = 3;

    @Override
    public String toString() {
        return "ToStringShow{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }

    public static void main(String[] args) {

    }
}
