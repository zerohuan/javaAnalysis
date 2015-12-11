package com.yjh.generic;

/**
 * Created by yjh on 15-12-6.
 */
public class GenericAndWildcard {
    private static class SuperClass {}
    private static class SubClass extends com.jvm.classloading.SuperClass {}

    private static class Pair<T> {
        T first;
        T second;

        public T getFirst() {
            return first;
        }

        public void setFirst(T first) {
            this.first = first;
        }

        public T getSecond() {
            return second;
        }

        public void setSecond(T second) {
            this.second = second;
        }
    }

    private static class MinUtil {
        static <T extends Comparable<? super T>> T min(T[] ts) {
            if(ts == null || ts.length == 0)
                return null;
            T min = ts[0];
            for(int i = 1; i < ts.length; ++i)
                if(min.compareTo(ts[i]) > 0)
                    min = ts[i];
            return min;
        }
    }

    private static void swap(Pair<?> pair) {
        swapHelper(pair);
    }

    private static <T> void swapHelper(Pair<T> pair) {
        T temp = pair.getFirst();
        pair.setFirst(pair.getSecond());
        pair.setSecond(temp);
    }

    public static void main(String[] args) {
        Pair<? extends SuperClass> pair1 = new Pair<>();
        Pair<? super SubClass> pair2 = new Pair<>();
        Pair<SubClass> pair3 = new Pair<>();
        Pair<SuperClass> pair4 = new Pair<>();
        Pair<?> pair5;
        pair1 = pair4;
        pair2 = pair3;

        //<?>的范围
        pair5 = pair1;
        pair5 = pair2;
        pair5 = pair3;
        pair5 = pair4;
    }
}
