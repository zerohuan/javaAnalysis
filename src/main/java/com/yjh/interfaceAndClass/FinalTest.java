package com.yjh.interfaceAndClass;

/**
 * Created by yjh on 15-11-16.
 */
public final class FinalTest {
    private final String a;
    private Inner inner;
    //通过实例初始化赋值final域
    {
        a = "final";
    }
    //这是不安全的做法，客户端可以在外部修改inner
//    public FinalTest(Inner inner) {
//        this.inner = inner;
//    }
    public FinalTest(Inner inner) {
        //Inner是非final类，调用它的clone方法并不安全，因为可被子类覆盖
//        this.inner = inner.clone();
        //这是一个安全的做法，因为我们使用了明确的构造器
        this.inner = new Inner(inner.a);
    }

    //对于没有实现cloneable接口的类，调用clone方法会抛出异常（Object中的clone是protected和native的）
    public static class Inner implements Cloneable {
        private int a;

        public Inner(int a) {
            this.a = a;
        }

        @Override
        public Inner clone() {
            Inner inner = null;
            try {
                inner = (Inner)super.clone();
                inner.a = a;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return inner;
        }
    }
}
