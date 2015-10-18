package com.yjh.initializationAndClean.singleton;

/**
 *
 *
 * Created by yjh on 15-10-18.
 */
public class ArrayInitialization {
    private static int[] ar;

    public static void acceptArray(int[] ints) {

    }

    public static void main(String[] args) {
        int[] a = {1,2};
        acceptArray(new int[]{1,2,3,4});
        System.out.println(ar == null);
    }
}
