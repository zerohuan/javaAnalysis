package com.jvm.ClassFileFormat;

import com.jvm.classloading.MInterface;
import com.jvm.classloading.SubClass;
import com.jvm.classloading.SuperClass;

/**
 *
 * Created by yjh on 15-11-18.
 */
public class InvokeInstruction extends SuperClass {

    public static void main(String[] args) {
        //invokestatic指令
        SuperClass.superStaticMethod();
        SubClass.superStaticMethod();

        //invokespecial指令
        SuperClass superClass = new SuperClass(); //调用构造器
        SubClass subClass = new SubClass();
        InvokeInstruction invokeInstruction = new InvokeInstruction();
        invokeInstruction.privateMethodInvoke(); //调用私有方法
        MInterface mInterface = subClass;

        //invokevirtual指令：调用实例方法
        subClass.superMethodInvoke();
        invokeInstruction.protectedMethodInvoke();
        subClass.interfaceMethod(); //注意通过具体类型调用接口方法是属于实例方法调用
        int[] ints = new int[10];
        ints.clone(); //注意clone方法是invokevirtual，虽然通过反射并不能获取这个方法
        SuperClass s = subClass;
        s.superMethodInvoke();

        //invokeinterface指令：调用接口方法，通过接口类型引用调用
        mInterface.interfaceMethod();


    }

    private void privateMethodInvoke() {

    }

}
