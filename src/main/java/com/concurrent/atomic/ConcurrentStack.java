package com.concurrent.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 基于CAS自旋的并发栈
 * Created by yjh on 16-1-11.
 */
public class ConcurrentStack<E> {
    private final AtomicReference<Node<E>> top = new AtomicReference<>(); //栈顶

    public Node<E> pop() {
        Node<E> oldHead;
        Node<E> newHead;
        do {
            oldHead = top.get();
            if (oldHead == null)
                return null;
            newHead = oldHead.next;
        } while(!top.compareAndSet(oldHead, newHead));
        return oldHead;
    }

    public void push(E e) {
        Node<E> oldHead;
        Node<E> newHead = new Node<>(e);
        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));
    }

    private static class Node<E> {
        private final E item;
        private Node<E> next;

        public Node(E item) {
            this.item = item;
        }
    }
}
