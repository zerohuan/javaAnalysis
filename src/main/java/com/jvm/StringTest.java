package com.jvm;

import java.util.Random;

/**
 * 老的HotSpot之所以在前面的一些实验里会返回false是因为它的string pool实现是要求要将被string pool
 * 引用的String实例放在PermGen里的，而随便造个普通的String实例不在PermGen里，所以这里会在PermGen内
 * 创建一个原String的拷贝。
 * 这样的话，每组内容相同的String实例首个被intern的那个实例不一定是最终放到string pool里的那个实例，
 * 自然就使得前面实验的str.intern() != str了。
 *
 * Hotspot JDK1.7, JRockit, Oracle JDK7都可以达到首次.intern()有s==s.intern();
 * 为什么JRockit与新的JDK7里的HotSpot会返回true其实很简单：它们的string pool并不拷贝输进来intern()的
 * java.lang.String实例，只是在池里记录了每组内容相同的String实例首个被intern的那个实例的引用。
 *
 * 唯一保证的事情是：当且仅当s.equals(t)时，有s.intern()==t.intern();
 *
 * Created by yjh on 15-10-28.
 */
public class StringTest {
    public static void test() {
        int trueCount = 0;
        int falseCount = 0;
        Random rand = new Random(System.currentTimeMillis());
        for(int n=0;n<10000;n++)
        {
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<300;i++)
                sb.append((char)(rand.nextInt(96)+32));
            String s = sb.toString().substring(100,200);
            if(s.intern() == s)
                trueCount++;
            else
                falseCount++;
        }
        System.out.println("trueCount=" + trueCount + "\tfalseCount=" + falseCount);
    }

    public static void main(String[] args) {
        String s = new StringBuilder("xxx").append("123").toString();
        System.out.println(s == s.intern());
    }
}
