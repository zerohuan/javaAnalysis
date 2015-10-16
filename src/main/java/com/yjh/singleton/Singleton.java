package com.yjh.singleton;

/**
 * difference model of singleton
 *
 * Created by yjh on 15-10-16.
 */
public class Singleton {
    /**
     * hungry man
     */
    private Singleton() {
        System.out.println("Singleton create.");
    }

    //hungry man model1: create when Class loaded
    private final static Singleton singleton1 = new Singleton();

    public static Singleton getNewInstance1() { return singleton1;}

    //hungry man model2:
    private static Singleton singleton2 = new Singleton();

    public static Singleton getNewInstance2() { return singleton2;}

    //hungry man model3:
    private static Singleton singleton3 = null;

    static {
        singleton3 = new Singleton();
    }

    public static Singleton getSingleton3() { return singleton3;}

    /**
     * lazy man
     *
     */
    //lazy man model1: thread unsafe, and extra work to do (Serializable, transient, readResolve())
    private static Singleton singleton4 = null;

    public static Singleton newInstances4() {
        if(singleton4 == null) {
            singleton4 = new Singleton();
        }
        return singleton4;
    }

    //lazy man model2: synchronized, efficiency is too low. Every time invoke newInstance() need get a lock.
    private static Singleton singleton5 = null;

    public synchronized static Singleton newInstance5() {
        if(singleton5 == null)
            singleton5 = new Singleton();
        return singleton5;
    }

    //double checked lock: After JDK1.5 this method is ok. You need use volatile keyword to ensure
    // writing is before reading
    private static volatile  Singleton singleton6 = null;

    public static Singleton newInstance6() {
        if(singleton6 == null) {
            synchronized (Singleton.class) {
                if(singleton6 == null) {
                    singleton6 = new Singleton();
                }
            }
        }
        return singleton6;
    }

    //initialization on demand holder, recommend it.
    private static class SingletonHolder {
        public static final Singleton singleton = new Singleton();
    }

    public static Singleton newInstatnce7() {
        return SingletonHolder.singleton;
    }

    //A good method of singleton: enum, see SingletonEnum in this package.


    public static void main(String[] args) throws Exception {
    }
}
