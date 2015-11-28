package com.designmodel.flyweight;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 *
 * Created by yjh on 15-11-26.
 */
public class FlyweightFactory {
    private static Map<String, FlyWeight> cache = new ConcurrentHashMap<>();

    public static synchronized FlyWeight valueOf(String key) {
        FlyWeight obj = cache.get(key);
        if(obj == null) {
            cache.put(key, new ConcreteFlyWeight());
        }
        return obj;
    }
}
