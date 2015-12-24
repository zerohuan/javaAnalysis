package com.concurrent.examples.vehicletraceker;

/**
 * Created by yjh on 15-12-20.
 */
public class SafePoint {
    private int x;
    private int y;

    private SafePoint(int[] xy) {
        this.x = xy[0];
        this.y = xy[1];
    }

    public SafePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public SafePoint(SafePoint p) {
        //这里如果使用this(p.x, p.y)会产生竞态条件
        this(p.get());
    }

    public synchronized int[] get() {
        return new int[]{x, y};
    }

    public synchronized void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
