package com.designmodel.flyweight;

/**
 * 具体的享元对象，外部状态，不可共享，往往是“易变的”，不稳定的，比如用户ID
 *
 * Created by yjh on 15-11-26.
 */
public class UnsharedConcreteFlyWeight implements FlyWeight {
    @Override
    public void doSomething() {

    }
}
