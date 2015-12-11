package com.yjh.reflect;

import com.yjh.util.PrintUtil;

import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * Created by yjh on 15-12-7.
 */
public class BaseUsage {
    private static class A {
        public A() {
        }

        public A(int publicSuperField) {
            this.publicSuperField = publicSuperField;
        }

        public int publicSuperField;
        private String privateStringField;
        public void superMethod() {}
    }
    private static class B extends A implements Serializable, Cloneable {
        public int publicSubClassField;
        private int privateSubClassField;
        public void subClass() {}
        private void subPrivate() {}
    }

    public static void main(String[] args)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        PrintStream out = System.out;

        double[] doubles = new double[10];
        //三种获取Class对象的方法
        Class<? extends double[]> doubleC = doubles.getClass();
        Class<BaseUsage> baseUsageClass = BaseUsage.class;
        Class<?> StringClass = Class.forName("java.lang.String");

        PrintUtil.printAllClassName(doubleC, baseUsageClass, StringClass);

        Class<B> bClass = B.class;
        //Field, Method, Constructor
        //Field
        Field[] fields = bClass.getFields(); //包括超类的公有方法
        PrintUtil.printAllMethod(fields);

        //创建对象
        Class<A> aClass = A.class;
//        the class object represents an abstract class, an interface,
//        an array class, a primitive type, or {@code void} 会抛出InstantiationException
        aClass.newInstance();

        try {
            Constructor<A> constructor = aClass.getConstructor(int.class);
            constructor.newInstance(1);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        try {
            A a = new A();
            Field intField = aClass.getField("publicSuperField");
            Field stringField = aClass.getDeclaredField("privateStringField");
            stringField.setAccessible(true); //读写私有域
            Integer intaa = (Integer)intField.get(a);//返回值自动装箱成Integer
            int inta = intField.getInt(a);
            intField.set(a, 1);
            String intStr = (String)stringField.get(a);
            stringField.set(a, "123");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }
}
