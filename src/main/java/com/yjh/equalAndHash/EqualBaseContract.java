package com.yjh.equalAndHash;

/**
 * four contract of equivalence relation
 *
 * Created by yjh on 15-10-22.
 */
public class EqualBaseContract {
    //equal relation in class inheritance
    static class S {
        private int x;
        private A a;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            S s = (S) o;

            if (x != s.x) return false;
            return !(a != null ? !a.equals(s.a) : s.a != null);

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + (a != null ? a.hashCode() : 0);
            return result;
        }
    }

    static class R {

    }

    static class A {
        private int x;
        private R r;

        @Override
        public boolean equals(Object o) {
            if(this == o) return true; //same reference
            if(o == null || getClass() != o.getClass()) return false;
            A a = (A)o;
            return x == a.x &&
                    (r == a.r || (r != null && r.equals(a.r)));
        }

        @Override
        public int hashCode() {
            return x;
        }
    }

    static class B extends A {
        private int y;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            B b = (B) o;

            return y == b.y;

        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + y;
            return result;
        }
    }

    public static void main(String[] args) {

    }
}
