package com.concurrent.examples.vehicletraceker;

/**
 * 可变的点
 * Created by yjh on 15-12-20.
 */
public class MutablePoint {
    private int x, y;

    public MutablePoint() {
        this.x = 0;
        this.y = 0;
    }

    public MutablePoint(MutablePoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
