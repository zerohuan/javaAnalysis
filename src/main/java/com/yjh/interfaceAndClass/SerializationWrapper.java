package com.yjh.interfaceAndClass;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * 序列化代理
 *
 * Created by yjh on 15-11-16.
 */
public class SerializationWrapper implements Serializable {
    private final Date start;
    private final Date end;

    public SerializationWrapper(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());

        if(this.start.compareTo(this.end) > 0)
            throw new IllegalArgumentException("error");
    }

    private static class SerializationProxy implements Serializable {
        private final Date start;
        private final Date end;

        SerializationProxy(SerializationWrapper wrapper) {
            start = wrapper.start;
            end = wrapper.end;
        }

        private Object readResolve() {
            //可以调用外围类的构造器，静态工厂和方法，依赖它们检查约束条件，不需要在序列化机制中进行保护
            return new SerializationWrapper(start, end);
        }
    }
    //用SerializationProxy代替自己进行序列化
    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    //禁止调用外围类的readObject，防止伪造字节码
    private void readObject(ObjectInputStream stream)
        throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }

    public static void main(String[] args) {

    }
}
