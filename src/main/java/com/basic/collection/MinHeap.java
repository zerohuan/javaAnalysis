package com.basic.collection;

import java.util.Arrays;

/**
 * 最小堆
 *
 * Created by yjh on 15-11-10.
 */
public class MinHeap {
    private int size;
    private final int[] elements;

    public MinHeap(int[] elements) {
        if(elements == null)
            throw new NullPointerException("elements 不能为空");
        this.elements = elements;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == elements.length;
    }

    /**
     * 从第i个节点上滤
     * @param i
     */
    public void fixUp(int i) {
        int j;
        int tmp = elements[i];
        for(j = (i - 1) >> 1; j >= 0 && elements[j] > tmp; j = (i - 1) >> 1) {
            elements[i] = elements[j];
            i = j;
        }
        elements[i] = tmp;
    }

    public void insert(int x) {
        if(isFull()) {
            return;
        }

        elements[size] = x;
        fixUp(size++);
    }

    /**
     * 从第i个节点下滤
     * @param i
     */
    public void fixDown(int i) {
        if(i < size) {
            int temp = elements[i];
            int j = (i << 1) + 1;

            while(j < size) {
                if(j + 1 < size && elements[j + 1] < elements[j])
                    j++;
                if(elements[j] > temp)
                    break;
                elements[i] = elements[j];
                i = j;
                j = (i << 1) + 1;
            }

            elements[i] = temp;
        }
    }

    public int deleteMin() {
        if(isEmpty())
            throw new RuntimeException("The heap is empty.");

        int top = elements[0];
        elements[0] = elements[--size];
        elements[size] = 0;
        fixDown(0);

        return top;
    }

    @Override
    public String toString() {
        return "MinHeap{" +
                "size=" + size +
                ", elements=" + Arrays.toString(elements) +
                '}';
    }

    public static void main(String[] args) {
        int[] vs = new int[10];
        MinHeap m = new MinHeap(vs);
        m.insert(7);
        m.insert(4);
        m.insert(2);
        m.insert(3);
        m.insert(1);
        m.insert(20);

        System.out.println(m);

    }
}
