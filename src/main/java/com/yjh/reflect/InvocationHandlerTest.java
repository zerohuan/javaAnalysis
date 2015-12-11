package com.yjh.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by yjh on 15-12-8.
 */
public class InvocationHandlerTest {
    public interface Interface {
        void f();
    }

    private static class InvocationHandlerT implements InvocationHandler {
        private Object proxied;
        public InvocationHandlerT(Object proxied) {
            this.proxied = proxied;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(proxied, args);
        }
    }

    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Interface itf = new Interface() {
            @Override
            public void f() {
                System.out.println("Success!");
            }
        };
        Interface proxy = (Interface)Proxy.newProxyInstance(Interface.class.getClassLoader(),
                new Class<?>[]{Interface.class}, new InvocationHandlerT(itf));
        proxy.f();
    }
}
