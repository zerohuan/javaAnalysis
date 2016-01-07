package com.concurrent.lock;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by yjh on 16-1-7.
 */
public class ReadWriteLockTest {
    //多线程读，单线程写，读写互斥，读读不互斥
    private static class ReadWriteMap<K,V> {
        private final Map<K,V> map;
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        private final Lock r = lock.readLock();
        private final Lock w = lock.writeLock();

        public ReadWriteMap(Map<K, V> map) {
            this.map = map;
        }

        public V get(K k) {
            r.lock();
            try {
                return map.get(k);
            } finally {
                r.unlock();
            }
        }

        public void put(K k, V v) {
            w.lock();
            try {
                map.put(k, v);
            } finally {
                w.unlock();
            }
        }
    }
}
