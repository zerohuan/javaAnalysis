package com.yjh.generic;

/**
 * Created by yjh on 15-12-6.
 */
public class GenericAndConflict {
    private static class Pair<T> {
        T first;
        T second;

        public T getFirst() {
            return first;
        }

        public void setFirst(T first) {
            this.first = first;
        }

        //不能命名该方法为equals，因为擦除后实际上是equals(Object)；
        public boolean equalss(T t) {
            //因为动态分派的存在即使擦除之后是通过Object引用调用，也会根据对象实例的类型找到具体的方法实现调用
            return first.equals(t) && second.equals(t);
        }

        public static void main(String[] args) {
            Pair<String> pair = new Pair<>();
            Pair p = pair;
            Pair<? extends A> p2 = new Pair<>();
            System.out.println(p2.getClass().getCanonicalName());
            Pair<B> p3 = new Pair<>();
            p2 = p3;
            System.out.println(p2.getClass().getCanonicalName());

            Pair<String> sp = new Pair<>();
            String s = sp.getFirst();
            s = sp.first;
        }
    }

    private static class A {}
    private static class B extends A {}

    private static class P implements Comparable<String> {
        @Override
        public int compareTo(String o) {
            return 0;
        }
    }
//    private static class PA extends P implements Comparable<Integer> {
//
//    }
}
