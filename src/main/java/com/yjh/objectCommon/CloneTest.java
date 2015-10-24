package com.yjh.objectCommon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * clone()
 *
 * Created by yjh on 15-10-22.
 */
public class CloneTest {
    public static void main(String[] args) {
        int[] ints = new int[10];

        ints.clone();

        Object o = new Object();
//        o.clone(); Object的clone是protected的

        List<Integer> integers = new ArrayList<>(Arrays.asList(3,4,5,7,8,10,2));
        Collections.sort(integers);
        System.out.println(integers);
    }

}
