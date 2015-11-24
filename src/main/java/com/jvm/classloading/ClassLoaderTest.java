package com.jvm.classloading;

import java.io.IOException;
import java.io.InputStream;

/**
 * 不同类加载器对instanceof关键字运算的影响
 *
 * Created by yjh on 15-11-19.
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws Exception {
        ClassLoader myLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                try {
                    String filename = name.substring(name.lastIndexOf(".") + 1) + ".class";

                    InputStream is = getClass().getResourceAsStream(filename);
                    if(is == null)
                        return super.loadClass(name);
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    is.close();
                    return defineClass(name, b, 0, b.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return super.loadClass(name);
            }
        };

        //执行main函数前，com.jvm.classloading.ClassLoaderTest已经由SystemClassLoader加载过了，
        //因此，在main函数中“com.jvm.classloading.ConstClass”类型是由SystemClassLoader加载的，
        //与Object的类型不一样
        Object obj = myLoader.loadClass("com.jvm.classloading.ClassLoaderTest").newInstance();
        System.out.println(obj.getClass());
        System.out.println(obj instanceof com.jvm.classloading.ConstClass);
    }
}
