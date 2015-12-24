package com.concurrent.examples.memorizer;

import java.util.concurrent.*;

/**
 * 使用FutureTask避免并发环境下的重复计算
 * Created by yjh on 15-12-24.
 */
public class Memorizer<A,V> implements Computable<A,V> {
    private final ConcurrentHashMap<A, FutureTask<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A,V> c;

    public Memorizer(Computable<A, V> c) {
        this.c = c;
    }

    public V compute(final A arg) throws InterruptedException {
        while (true) { //发生异常
            Future<V> f = cache.get(arg);
            if (f == null) {
                Callable<V> callable = new Callable<V>() {
                    @Override
                    public V call() throws Exception {
                        return c.compute(arg);
                    }
                };
                FutureTask<V> ft = new FutureTask<>(callable);
                f = cache.putIfAbsent(arg, ft);
                if (f == null) {
                    f = ft; ft.run();
                }
            }
            try {
                return f.get(); //阻塞
            } catch (CancellationException e) {
                cache.remove(arg, f);
            } catch (ExecutionException e) {
            /*
            抛出ExecutionException有三种情况：
            （1）受检查的异常；
            （2）RuntimeException；
            （3）Error；
             */
                throw launcherThrowable(e.getCause());
            }
        }
    }

    public RuntimeException launcherThrowable(Throwable t) {
        if (t instanceof RuntimeException) {
            return (RuntimeException)t;
        }
        else if (t instanceof Error) {
            throw (Error)t;
        }
        else {
            throw new RuntimeException("Not Checked" ,t);
        }
    }
}
