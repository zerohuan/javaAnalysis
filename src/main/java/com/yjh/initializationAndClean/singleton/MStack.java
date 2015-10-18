package com.yjh.initializationAndClean.singleton;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by yjh on 15-10-18.
 */
public class MStack {
    private static final int DEFAULT_SIZE = 20;
    private Object[] elements = new Object[DEFAULT_SIZE];
    private int size = 0;

    public MStack() {
        elements = new Object[DEFAULT_SIZE];
    }

    public void push(Object element) {
        ensureCapacity();
        elements[size++] = element;
    }

    public Object pop() {
        if(size == 0) {
            throw new RuntimeException("empty stack cannot pop");
        }
        return elements[--size];
    }

    public void ensureCapacity() {
        if(size == elements.length) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }

    public static void main(String[] args) throws Exception {
        MStack stack = new MStack();

        for(int j = 0; j < 50; j++) {
            for(int i = stack.size; i < stack.elements.length; i++) {
                stack.push(new Element()); //自动装箱
            }
            for(int i = 0; i < 5; i++) {
                stack.pop();
            }
            for(int i = 0; i < 6; i++) {
                stack.push(new Element()); //自动装箱
            }
            System.gc();
        }
        TimeUnit.SECONDS.sleep(100);
    }

    static class Element {

    }

}
