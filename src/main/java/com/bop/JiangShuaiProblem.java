package com.bop;

/**
 * 1.2 将帅问题
 *
 * Created by yjh on 15-10-25.
 */
public class JiangShuaiProblem {

    public static String resolve(byte b) {
        return "A: " + ((b & 0xc0) >> 6) + " " + ((b & 0x30) >> 4) +
                " B: " + ((b & 0x0c) >> 2) + " " + (b & 0x03);
    }

    public static void result() {
        byte b;
        for(int i = 0; i < 9; i++) {
            b = (byte)(((i / 3 + 1) << 2) + (i % 3 + 1));
            b <<= 4;
            for(int j = 0; j < 9; j++) {
                if(j / 3 != i / 3) {
                    b += ((j / 3 + 1) << 2) + (j % 3 + 1);
                    System.out.println(resolve(b));
                    b &= 0xf0;
                }
            }
        }
    }

    public static void main(String[] args) {
        result();
    }
}
