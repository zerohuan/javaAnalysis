package com.exercise.greed;

import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Created by yjh on 16-2-24.
 */
public class ExceedAVG {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int r = scanner.nextInt();
        int avg = scanner.nextInt();
        int[] a = new int[n];
        int[] b = new int[n];
        int total = 0;
        int top = avg * n;
        PriorityQueue<U> queue = new PriorityQueue<>();
        for (int i = 0; i < n; ++i) {
            a[i] = scanner.nextInt();
            total += a[i];
            b[i] = scanner.nextInt();
            queue.add(new U(b[i], i));
        }
        int count = 0;
        while (total < top) {
            if (!queue.isEmpty()) {
                U u = queue.poll();
                int temp1;
                total += temp1 = (top - total > r - a[u.index] ? r - a[u.index] : top - total);
                count += temp1 * u.b;
            } else
                break;
        }
        System.out.println(count);
        scanner.close();
    }

    public static class U implements Comparable<U> {
        private int b;
        private int index;

        public U(int b, int index) {
            this.b = b;
            this.index = index;
        }

        @Override
        public int compareTo(U o) {
            if (b > o.b)
                return 1;
            else if (b < o.b)
                return -1;
            else
                return 0;
        }
    }
}
