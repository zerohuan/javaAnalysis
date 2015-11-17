package com.yjh.interfaceAndClass;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 等价关系
 *
 * Created by yjh on 15-11-16.
 */
public class EqualsTest {
    private static class A { //如果使用abstract表明A不可实例化，子类就可以使用instanceof判断类型
        private int a;
        private int b;

        @Override
        public boolean equals(Object o) {
            if(this == o) return true; //自反性
            if(o == null) //非空性
                return false;
            return o instanceof A && a == ((A)o).a && b == ((A)o).b;
        }

        //覆盖了equals方法一定要覆盖hashCode方法
        @Override
        public int hashCode() {
            int result = a;
            result += 31 * result + b;
            return result;
        }
    }

    private static class B extends A {
        private int c;

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            //如果没有使用getClass检查而仅仅是使用instanceof，会有混合使用A和B时造成传递性不能保证的问题
            if(o == null || getClass() != o.getClass()) return false;
            if(!super.equals(o)) return false;

            B b = (B)o;
            return b.c == c;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + c; //31是奇素数在溢出时也不会丢失调信息，31*i会经过编译器优化为(i << 5) - i;
            return result;
        }
    }

    //测试TimeStamp和Date的equals行为
    //在jdk7以上中TimeStamp和Date的bug已经修复
    public static void timeStampTest() throws Exception {
        Date d = new Date();
        Timestamp t1 = new Timestamp(d.getTime());
        t1.setNanos(1);
        Timestamp t2 = new Timestamp(d.getTime());
        t2.setNanos(2);
        System.out.println(d.getTime());
        System.out.println(t1.getTime());
        System.out.println(t2.equals(d));
    }

    public static void main(String[] args) throws Exception{
        timeStampTest();
    }
}
