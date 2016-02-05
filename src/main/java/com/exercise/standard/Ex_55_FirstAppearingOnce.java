package com.exercise.standard;

import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * 字符流中第一个不重复的字符
 * 请实现一个函数用来找出字符流中第一个只出现一次的字符。例如，当从字符流中只读出前两个字符"go"时，
 * 第一个只出现一次的字符是"g"。当从该字符流中读出前六个字符“google"时，第一个只出现一次的字符是"l"。
 *
 * 解法：
 * 书中的解法仅仅使用了一个哈希表，但是每次获取的时候，时间开销是O(n)；
 *
 * 我这里除了用哈希表来实现插入时O(1)更新以外，还使用一个最小堆来实现获取时的O(lgn)；
 *
 * Created by yjh on 16-2-4.
 */
public class Ex_55_FirstAppearingOnce {
    private PriorityQueue<Unit> heap = new PriorityQueue<>();
    private HashMap<Character, Unit> map = new HashMap();
    private static int SITE = 0;

    //Insert one char from stringstream
    public void Insert(char ch) {
        Unit u = map.get(ch);
        if (u == null) {
            u = new Unit(ch);
            u.site = SITE++;
            heap.add(u);
            map.put(ch, u);
        }
        u.count++;
        Unit min = heap.peek();
        if (min.c == u.c) {
            heap.poll();
            heap.add(min);
        }
    }
    //return the first appearence once char in current stringstream
    public char FirstAppearingOnce() {
        Unit min = heap.peek();
        if (min != null && min.count == 1)
            return min.c;
        return '#';
    }

    private static class Unit implements Comparable<Unit> {
        char c;
        int count;
        int site;

        public Unit(char c) {
            this.c = c;
        }

        @Override
        public int compareTo(Unit o) {
            if (count < o.count)
                return -1;
            else if (count > o.count)
                return 1;
            else {
                if (site < o.site)
                    return -1;
                else if (site > o.site)
                    return 1;
                else
                    return 0;
            }

        }
    }

    public static void main(String[] args) {
        Ex_55_FirstAppearingOnce t = new Ex_55_FirstAppearingOnce();
        t.Insert('g');
        System.out.println(t.FirstAppearingOnce());
        t.Insert('o');
        System.out.println(t.FirstAppearingOnce());
        t.Insert('o');
        System.out.println(t.FirstAppearingOnce());
        t.Insert('g');
        System.out.println(t.FirstAppearingOnce());
        t.Insert('l');
        System.out.println(t.FirstAppearingOnce());
        t.Insert('e');
        System.out.println(t.FirstAppearingOnce());
        System.out.println(t.map.get('l').count);
    }
}
