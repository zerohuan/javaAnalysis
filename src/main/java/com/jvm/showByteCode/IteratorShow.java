package com.jvm.showByteCode;

import java.util.List;

/**
 * Created by yjh on 15-12-26.
 */
public class IteratorShow {
    private static void forShow1(int[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            System.out.println(arr[i]);
        }
    }

    private static void forShow2(int[] arr) {
        for (int i : arr) {
            System.out.println(i);
        }
    }

    private static void forShow3(List<String> l) {
        for (String s : l) {

        }
    }
}
