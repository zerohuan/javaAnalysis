package com.jvm.executionengine;

/**
 * Created by yjh on 15-11-26.
 */
public class MethodInvoke {

    //final方法通过invokevirtual指令调用，但它仍然是非虚方法，在类解析阶段可以确定调用版本
    public final void finalMethod(){}

    //invokevirtual实例方法，动态分派，动态绑定

    //invokestatic
    public static void staticMethod(){}

    //invokespecial
    private void privateMethod(){}



    public static void main(String[] args) {
        MethodInvoke methodInvoke = new MethodInvoke();

        methodInvoke.finalMethod();
    }


}
