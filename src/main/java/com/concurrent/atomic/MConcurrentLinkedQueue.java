package com.concurrent.atomic;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yjh on 16-1-11.
 */
public class MConcurrentLinkedQueue<E> {
    private static class Node<E> {
        private static final long nextOffset;
        static {
            Class<?> N = Node.class;
            try {
                nextOffset = UNSAFE.objectFieldOffset(N.getDeclaredField("next"));
            } catch (NoSuchFieldException e) {
                throw new IllegalStateException();
            }
        }

        private final E item;
        /*
        不在节点类中直接使用原子类性能更好
        实际上J.U.C中也是采用Unsafe的cas更新+volatile；
         */
        private volatile Node<E> next;

        public Node(E item) {
            this.item = item;
        }
    }

    private final Node<E> dummy = new Node<>(null);
    private volatile Node<E> head = dummy;
    private volatile Node<E> tail = dummy;

    private static final sun.misc.Unsafe UNSAFE;
    private static final long headOffset;
    private static final long tailOffset;

    static {
        Class<?> claz = sun.misc.Unsafe.class;
        try {
            Field theUnsafe = claz.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (sun.misc.Unsafe)theUnsafe.get(null);
            Class<?> k = MConcurrentLinkedQueue.class;
            headOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("head"));
            tailOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("tail"));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    /*
    对于保证两个变量同时更新的原子性必须进行更复杂的判断和考虑（可见非阻塞算法比阻塞算法要复杂）
    保证同时更新tail引用和oldTail.next引用，分为稳定状态和中间状态来考虑
    一个关键的地方需要注意的是：设置保证tail在CAS设置时不用考虑成败，因为我们知道至少有一个线程可以修改成功，这经常被非阻塞算法所利用；
     */
    public boolean put(E item) {
        Node<E> newNode = new Node<>(item);
        while (true) {
            Node<E> curTail = tail;
            Node<E> tailNext = tail.next;
            if (curTail == tail) {
                if (tailNext != null) {
                    /*
                    队列处于中间状态，说明有线程在进行插入操作
                    注意这里不用成功与否，因此不成功说明其他线程成功设置了
                     */
                    UNSAFE.compareAndSwapObject(this, tailOffset, curTail, tailNext);
                } else {
                    //链表处于稳定状态
                    if (UNSAFE.compareAndSwapObject(curTail, Node.nextOffset, null, newNode))  {
                        //成功插入新节点，CAS设置tail，这里也一样不用考虑成败
                        UNSAFE.compareAndSwapObject(this, tailOffset, curTail, newNode);
                        return true;
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        Node<E> h = head;
        StringBuilder sb = new StringBuilder("MConcurrentLinkedQueue[");
        while (h.next != null) {
            Node<E> n = h.next;
            sb.append(n.item).append(',');
            h = n;
        }
        int len;
        if ((len = sb.length()) > "MConcurrentLinkedQueue[".length())
            sb.deleteCharAt(len - 1);
        sb.append(']');
        return sb.toString();
    }

    //用一个Barrier同步打印
    private static final int THREAD_COUNT = 10;
    private static final CountDownLatch LATCH = new CountDownLatch(THREAD_COUNT);
    private static class InsertQueueTask implements Runnable {
        private final MConcurrentLinkedQueue<Integer> queue;

        public InsertQueueTask(MConcurrentLinkedQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            queue.put(1);
            queue.put(2);
            queue.put(3);
            LATCH.countDown();
        }
    }
    public static void main(String[] args) throws Exception {
        MConcurrentLinkedQueue<Integer> queue = new MConcurrentLinkedQueue<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; ++i) {
            executorService.execute(new InsertQueueTask(queue));
        }
        executorService.shutdown();
        LATCH.await();
        System.out.println(queue);
    }
}
