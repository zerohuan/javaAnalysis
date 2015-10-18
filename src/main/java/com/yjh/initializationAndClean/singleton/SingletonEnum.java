package com.yjh.initializationAndClean.singleton;

/**
 * Able to use serialize directly, and don't worry about Class.newInstance().
 *
 * Created by yjh on 15-10-16.
 */
public enum SingletonEnum {
    INSTANCE;

    private String name;

    public void doSomething() {
        System.out.println("This is " + name + " with a single element.");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) {
        INSTANCE.setName("E");
        INSTANCE.doSomething();
    }
}
