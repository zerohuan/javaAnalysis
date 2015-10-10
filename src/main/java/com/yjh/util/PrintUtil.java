package com.yjh.util;

/**
 * print tools
 *
 * Created by yjh on 15-10-10.
 */
public class PrintUtil {
    public static String toHex(int num) {
        return String.format("%x", num);
    }

    public static String toHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for(byte b : bytes) {
            stringBuilder.append(String.format("%02x", b)).append(" ");
        }
        return stringBuilder.toString();
    }
}
