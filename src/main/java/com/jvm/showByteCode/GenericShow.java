package com.jvm.showByteCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 *
 * Created by yjh on 15-12-6.
 */
public class GenericShow {
    private static class Pair<T> {
        private T first;
        private T second;
        public T getFirst() {return first;}
        public void setFirst(T first) {this.first = first;}
        public T getSecond() {return second;}
        public void setSecond(T second) {this.second = second;}
    }

    private static class DateInterval extends Pair<Date> {
        @Override
        public void setSecond(Date second) {
            Throwable t = new Throwable();
            for(StackTraceElement element : t.getStackTrace()) {
                System.out.println(element);
            }
        }

        @Override
        public Date getFirst() {
            return super.getFirst();
        }
    }

    private List<String> list;
    private List<? extends Cloneable> list2;
    private static class TestA<T extends Comparable<? super T>> {
        public T get(T t) {
            return t;
        }
    }
    private void baseShow() {
        List<String> list = new ArrayList<>();
        list.add("123");
        String s = list.get(0);
        List<String> list1 = list;
        TestA<String> testA = new TestA<>();
        Comparable<String> c = testA.get("123");
    }

    private static <T>T backT(T...t) {
        if(t.length > 0)
            return t[0];
        else
            return null;
    }

    private static <T extends Comparable> void comp(T t) {

    }

    private static <T extends List & Serializable & Comparable<? super T>> void comp2(T t1, T t2) {
        t1.get(1);
        t1.compareTo(t2);
    }

    private static <T extends HashSet & Comparable> void hc() {

    }

    private static class A {

    }

    private static class B extends A {

    }

    private static class C extends A {

    }

    public static void main(String[] args) {

        DateInterval dateInterval = new DateInterval();
        dateInterval.setSecond(new Date());

    }
}
