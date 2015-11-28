package com.designmodel.flyweight;

/**
 * 具体的享元类，一般是内部状态，稳定的，一般是不可变状态，Integer等不可变对象，具有可共享性
 *
 * Created by yjh on 15-11-26.
 */
public class ConcreteFlyWeight implements FlyWeight {
    @Override
    public void doSomething() {
        System.out.println("");
    }
}
