package com.yjh.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 展示生产者和消费者模式
 *
 * Created by yjh on 15-11-8.
 */
public class ProducerAndConsumer {
    private static class Value {

    }

    private static class ValueHolder {
        private final Object mutexProduce = new Object();
        private final Object mutexConsumer = new Object();
        private final ExecutorService executor;

        private Value value;

        public ValueHolder(ExecutorService executor) {
            this.executor = executor;
        }
    }

    private static class Producer implements Runnable {
        private final ValueHolder holder;
        private int count;

        public Producer(ValueHolder holder) {
            this.holder = holder;
        }

        public void produce() {
            if(holder != null) {
                try {
                    while(!Thread.interrupted()) {
                        synchronized (holder.mutexProduce) {
                            while(holder.value != null)
                                holder.mutexProduce.wait();
                        }
                        if(count > 10)
                            holder.executor.shutdownNow();
                        System.out.println("has a value produced...");
                        synchronized (holder.mutexConsumer) {
                            TimeUnit.SECONDS.sleep(1);
                            holder.value = new Value();
                            count++;
                            holder.mutexConsumer.notifyAll();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            produce();
        }
    }

    private static class Consumer implements Runnable {
        private final ValueHolder holder;

        public Consumer(ValueHolder holder) {
            this.holder = holder;
        }

        public void consume() {
            if(holder != null) {
                try {
                    while(!Thread.interrupted()) {
                        synchronized (holder.mutexConsumer) {
                            while(holder.value == null)
                                holder.mutexConsumer.wait();
                        }
                        System.out.println("has a value from producer...");
                        synchronized (holder.mutexProduce) {
                            TimeUnit.SECONDS.sleep(1);
                            holder.value = null;
                            holder.mutexProduce.notifyAll();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            consume();
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        ValueHolder valueHolder = new ValueHolder(executor);
        Producer producer = new Producer(valueHolder);
        Consumer consumer = new Consumer(valueHolder);


        executor.execute(producer);
        executor.execute(consumer);

        executor.shutdown();
    }

}
