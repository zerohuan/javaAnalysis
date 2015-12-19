package com.yjh.Collections;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by yjh on 15-12-12.
 */
public class HashMapTest {
    static class A {
        int x;
        int y;

        public A(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            A a = (A) o;

            if (x != a.x) return false;
            return y == a.y;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    public static void main(String[] args) {
        Map<A, String> map = new HashMap<>();
        A a1 = new A(1,2);
        map.put(a1, "12");
        a1.x = 2;
        System.out.println(map.entrySet());
        System.out.println(map.get(a1));
    }
}
