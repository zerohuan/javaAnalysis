package com.exercise.bop;

/**
 * Created by yjh on 2016/3/15.
 */
public class Ex_2_6_PreciseFloat {
    public static String preciseFloat(double num) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(String.format("%.0f", num));

        

        return buffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(preciseFloat(12.32324232));
    }
}
