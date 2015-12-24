package com.concurrent.examples.vehicletraceker;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于内置锁的车辆监视器——实例封装
 * 这只是一个例子，还有很多问题
 * Created by yjh on 15-12-20.
 */
public class MonitorVehicleTracker {
    private final Map<String, MutablePoint> locations;

    /**
     * 复制的过程中locations是否可能被其他线程修改，这需要调用者根据需求来考虑
     * @param locations
     */
    public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
        this.locations = deepCopy(locations);
    }

    public synchronized MutablePoint getLocation(String id) {
        MutablePoint loc = locations.get(id);
        return loc == null ? null : new MutablePoint(loc);
    }

    public synchronized void setLocation(String id, int x, int y) {
        MutablePoint loc = locations.get(id);
        if(loc == null)
            throw new IllegalArgumentException();
        loc.setX(x);
        loc.setY(y);
    }


    /**
     * 由于我们不知道传入的参数m是否是线程安全的，通过get读取复制显然也无法靠m自己维护线程安全性
     * 因此deepCopy最好一层线程安全层来调用
     * @param m
     * @return
     */
    public Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> m) {
        Map<String, MutablePoint> nm = new HashMap<>();
        for(Map.Entry<String,MutablePoint> e : m.entrySet())
            nm.put(e.getKey(), e.getValue());
        return Collections.synchronizedMap(nm);
    }
}
