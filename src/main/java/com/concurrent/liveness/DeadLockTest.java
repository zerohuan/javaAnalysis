package com.concurrent.liveness;

/**
 * Created by yjh on 15-12-23.
 */
public class DeadLockTest {
    /*
    死锁简单形式：Dead Embrace
    如果所有的线程都按照固定的顺序获取锁，那就不会发生死锁
     */
    private final Object left = new Object();
    private final Object right = new Object();
    private void leftRight() {
        synchronized (left) {
            synchronized (right) {
                System.out.println("left-right");
            }
        }
    }

    private void rightLeft() {
        synchronized (right) {
            synchronized (left) {
                System.out.println("right-left");
            }
        }
    }

    /*
    动态的锁顺序死锁
    依赖与运行时变量引用的对象顺序
     */
    private static final class Account {
        double balance;
        void dedit(double amount) {}
        void credit(double amount) {}
    }
    //from和to依赖与参数顺序，可能会造成死锁
    private void transferMoney(Account from, Account to, double amount) {
        synchronized (from) {
            synchronized (to) {
                if (from.balance < amount)
                    throw new IllegalStateException();
                from.dedit(amount);
                from.dedit(amount);
            }
        }
    }

    /*
    避免动态的锁顺序问题造成的死锁问题：根据具体对象确定锁顺序，System.identityHashCode(o);
    当hash值相同时，使用一个“加时赛”锁来消除死锁的可能
     */
    private final Object tieLock = new Object();
    private void transferMoney1(Account from, Account to, double amount) {
        class Helper {
            void transfer() {
                if (from.balance < amount)
                    throw new IllegalStateException();
                from.dedit(amount);
                to.credit(amount);
            }
        }
        int fromHash = System.identityHashCode(from);
        int toHash = System.identityHashCode(to);
        if (fromHash < toHash) {
            synchronized (from) {
                synchronized (to) {
                    new Helper().transfer();
                }
            }
        }
        else if (fromHash > toHash) {
            synchronized (to) {
                synchronized (from) {
                    new Helper().transfer();
                }
            }
        }
        //hashCode相等，即发生冲突，使用额外的一个“加时赛”锁来消除死锁的可能性
        else {
            synchronized (tieLock) {
                synchronized (from) {
                    synchronized (to) {
                        new Helper().transfer();
                    }
                }
            }
        }
    }



    public static void main(String[] args) {

    }
}
