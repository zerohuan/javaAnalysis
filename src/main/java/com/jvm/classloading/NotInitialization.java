package com.jvm.classloading;

import com.basic.util.testUtil.AbstractTestable;
import com.basic.util.testUtil.MTestable;
import com.basic.util.testUtil.MTester;

/**
 *
 * 展示不会引起初始化的例子
 *
 * Created by yjh on 15-11-17.
 */
public class NotInitialization {

    //不会导致SubClass的初始化
    private static MTestable sub1 = new AbstractTestable("子类引用父类的静态字段") {
        @Override
        public void test() throws Exception {
            //通过子类来引用父类的字段，
            int i = SubClass.value;
        }
    };

    //不能引起SuperClass的初始化
    private static MTestable arrDefine = new AbstractTestable("通过定义数组来引用类") {
        @Override
        public void test() throws Exception {
            SuperClass[] sca = new SuperClass[10];
        }
    };

    //不能引起ConstClass的初始化
    private static MTestable finalRef = new AbstractTestable("通过访问常量引用类") {
        @Override
        public void test() throws Exception {
            System.out.println(ConstClass.HELLOWORLD);
        }
    };

    //结果：会引起类及其父类的加载，但是不会导致初始化
    private static MTestable arrayRef = new AbstractTestable("查看数组类的加载") {
        @Override
        public void test() throws Exception {
            SubClass[] sca = new SubClass[10];
        }
    };

    private static MTestable subInstantiation = new AbstractTestable("查看数组类的加载") {
        @Override
        public void test() throws Exception {
            SubClass subClass = new SubClass();
        }
    };

    public static void main(String[] args) {
//        MTester.test(sub1);
        MTester.test(subInstantiation);
    }
}
