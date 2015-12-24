package com.concurrent.examples.memorizer;

/**
 * Created by yjh on 15-12-24.
 */
public interface Computable<A,V> {
    V compute(A arg) throws InterruptedException;
}
