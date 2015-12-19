package com.yjh.Collections;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 简单的HashMap实现
 * Created by yjh on 15-12-12.
 */
public class SimpleHashMap<K,V> extends AbstractMap<K,V> {
    static final int DEFAULT_INIT_CAPACITY = 1 << 4;
    static final int MAXIUM_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    transient Node<K,V>[] table;

    transient int size = 0;
    transient int modCount = 0;

    int threshold;
    final float loadFactor;

    public SimpleHashMap() {
        loadFactor = DEFAULT_LOAD_FACTOR;
    }



    static int hash(Object k) {
        int h;
        return (k == null) ? 0 : ((h = k.hashCode()) ^ (h >>> 16));
    }


    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        public Node(int hash, K key, Node<K, V> next, V value) {
            this.hash = hash;
            this.key = key;
            this.next = next;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if(o instanceof  Map.Entry) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
                if(Objects.equals(entry.getKey(), key) && Objects.equals(entry.getValue(), value))
                    return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }
}
