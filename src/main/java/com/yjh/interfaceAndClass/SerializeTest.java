package com.yjh.interfaceAndClass;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 保护性和final的关系和相互影响
 *
 * Created by yjh on 15-11-16.
 */
public class SerializeTest implements Serializable {
    //一段不符合start<end的约束关系的字节码
    private static final byte[] ERROR_DATA = {
            -84, -19, 0, 5, 115, 114, 0, 46, 99, 111, 109, 46, 121, 106, 104, 46, 105, 110, 116,
            101, 114, 102, 97, 99, 101, 65, 110, 100, 67, 108, 97, 115, 115, 46, 83, 101, 114, 105, 97,
            108, 105, 122, 101, 84, 101, 115, 116, 36, 80, 101, 114, 105, 111, 100, 12, -1, -24, -118, 23,
            11, -7, 100, 2, 0, 2, 76, 0, 3, 101, 110, 100, 116, 0, 16, 76, 106, 97, 118, 97, 47, 117, 116,
            105, 108, 47, 68, 97, 116, 101, 59, 76, 0, 5, 115, 116, 97, 114, 116, 113, 0, 126, 0, 1, 120,
            112, 115, 114, 0, 14, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 68, 97, 116, 101, 104, 106,
            -127, 1, 75, 89, 116, 25, 3, 0, 0, 120, 112, 119, 8, 0, 0, 1, 81, 15, 24, -88, 14, 120, 115, 113,
            0, 126, 0, 3, 119, 8, 0, 0, 1, 81, 15, 26, 46, -82, 120
    };

    /**
     * 实现Serializable接口，必须要提供保护性的readObject方法，否则可能会不安全：
     * 1. 伪字节流攻击；
     * 2. 内部域的调用
     */
    private final static class Period implements Serializable {
        private Date start;
        private Date end;

        /**
         * 使用保护性拷贝构造器
         *
         * @param start
         * @param end
         */
        public Period(Date start, Date end) {
            this.start = new Date(start.getTime());
            this.end = new Date(end.getTime());
            if(start.compareTo(end) > 0)
                throw new IllegalArgumentException(start + " After " + end);
        }

        private void readObject(ObjectInputStream s)
                throws IOException, ClassNotFoundException {
            s.defaultReadObject();

            //保护性拷贝，防止内部域盗用
            start = new Date(start.getTime());
            end = new Date(end.getTime());

            if(start.compareTo(end) > 0) {
                throw new InvalidObjectException(start + " After " + end);
            }
        }

        @Override
        public String toString() {
            return "Period{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }

    private void writePeriod(Period period) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(period);

        System.out.println(Arrays.toString(bos.toByteArray()));
        oos.close();
        bos.close();
    }

    /**
     * 内部域的盗用
     */
    private static class MutablePeriod {
        private final Period period;
        private final Date start;
        private final Date end;

        public MutablePeriod() {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(new Period(new Date(System.currentTimeMillis() - 1000), new Date()));

                byte[] ref = {0x71, 0 , 0x7e, 0, 5};
                bos.write(ref);
                ref[4] = 4;
                bos.write(ref);

                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
                period = (Period)ois.readObject();
                start = (Date)ois.readObject();
                end = (Date)ois.readObject();

                oos.close();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new AssertionError();
            }
        }

        public static void main(String[] args) throws Exception {
            MutablePeriod mutablePeriod = new MutablePeriod();
            System.out.println(mutablePeriod.period);
            TimeUnit.SECONDS.sleep(1);
            mutablePeriod.start.setTime(System.currentTimeMillis());
            System.out.println(mutablePeriod.period);
        }
    }

    private static class A {
        public A() {
            System.out.println("A is instanced.");
        }
    }


    private static class ReadResolveWrapper implements Serializable {
        public static final ReadResolveWrapper SINGLETON = new ReadResolveWrapper();
        //所有的域都应该是transient的
        private transient A a = new A();

        private ReadResolveWrapper() {}

        private Object readResolve() {
            return SINGLETON;
        }
    }

    public static void testReadSolve() throws Exception {
        SerializeTest test = new SerializeTest();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(test);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);

        System.out.println(ReadResolveWrapper.SINGLETON);
        test = (SerializeTest)ois.readObject();
        System.out.println(ReadResolveWrapper.SINGLETON);

        oos.close();
        bos.close();
        ois.close();
        bis.close();
    }

    public static void main(String[] args) throws Exception {
        Period period = new Period(new Date(System.currentTimeMillis() - 100000), new Date());

        ByteArrayInputStream bis = new ByteArrayInputStream(ERROR_DATA);
        ObjectInputStream ois = new ObjectInputStream(bis);
//        Period p2 = (Period)ois.readObject(); //将会抛出InValidException

        testReadSolve();

        ois.close();
        bis.close();
    }
}
