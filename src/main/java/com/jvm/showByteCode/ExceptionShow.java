package com.jvm.showByteCode;

import javax.servlet.ServletException;
import java.io.*;
import java.sql.SQLException;
import java.util.IllformedLocaleException;
import java.util.zip.ZipException;

/**
 * Created by yjh on 15-12-4.
 */
public class ExceptionShow {
    private int ifAndFinally(int x) {
        if (x == 1) {
            try {
                x = 2;
            } finally {
                x = 3;
            }
        } else {
            try {
                x = 4;
            } finally {
                x = 5;
            }
        }
        return x;
    }

    private int exceptionInFinally() {
        try {
            System.out.println("try normal");
        } finally {
            throw new NullPointerException();
        }
    }

    private int hasException() {
        int x;
        try {
            x = 1;
            return x;
        } catch (Exception e) {
            x = 2;
            return x;
        } finally {
            x = 3;
        }
    }

    private int finallyReturn() {
        int x;
        try {
            x = 1;
            if(x == 1)
                throw new RuntimeException("xxx");
            return x;
        } catch(Exception e) {
            x = 2;
            return x;
        } finally {
            x = 3;
            return 3;
        }
    }

    private int throwsException() throws IOException, SQLException {
        return 1;
    }

    private int noException() {
        int x;
        x = 3;
        return x;
    }

    private static class SuperClass {
        protected void f() throws IOException {}
    }

    private static class SubClass1 extends SuperClass {
        @Override
        protected void f() throws ZipException {}
    }

    private static class SubClass2 extends SuperClass {
        @Override
        protected void f() {}
    }

    //捕获异常
    private void catchAndThrowNew() throws ServletException {
        try {
            throw new SQLException();
        } catch (SQLException e) {
            ServletException se = new ServletException();
            se.initCause(e);
            throw se;
        }
    }

    private int nestedTryCatch() {
        int x;
        try {
            try {
                x = 2;
            } finally {
                x = 3;
                return x;
            }
        } finally {
            x = 4;
            return x;
        }
    }

    private void tryWithResources() throws IOException {
        try(InputStream in = new ByteArrayInputStream(new byte[10])) {
            try (Reader reader = new BufferedReader(new InputStreamReader(in))) {

            }
        }
    }

    private void lostException1() {
        try {
            throw new IllegalArgumentException();
        } finally {
            throw new IllegalStateException();
        }
    }

    private void lostException2() {
        try {
            throw new IllegalArgumentException();
        } finally {
            return;
        }
    }

    private void lostException3() {
        try {
            throw new IllegalArgumentException();
        } finally {
            int x = 3;
            System.out.println(x);
        }
    }

    private void lostException4() {
        try {
            try {
                throw new IllegalArgumentException();
            } finally {
                throw new IllegalStateException();
            }
        } finally {
            throw new IllformedLocaleException();
        }
    }

    private void printStaceTrace() {
        Throwable t = new Throwable();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(bos);
        t.printStackTrace(out);

        StackTraceElement[] traceElements = t.getStackTrace();
        for(StackTraceElement e : traceElements) {
            System.out.println(e);
        }

    }

    private int simpleMethod() {
        int x = 2;
        try {
            x = 1;
            return x;
        } finally {
//            int y =3;

            return x;
        }
    }


    public static void main(String[] args) {
        ExceptionShow exceptionShow = new ExceptionShow();
        System.out.println(exceptionShow.hasException());
        System.out.println(exceptionShow.finallyReturn());
        System.out.println(exceptionShow.nestedTryCatch());
        try {
            exceptionShow.catchAndThrowNew();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getCause());
//            System.out.println(e.getCause());
        }
        try {
            exceptionShow.lostException1();
        } catch (Exception e) {
            System.out.println(e);
        }
        exceptionShow.lostException2();
        try {
            exceptionShow.lostException4();
        } catch (Exception e) {
            System.out.println(e);
        }

        exceptionShow.printStaceTrace();

        try {
            exceptionShow.lostException3();
            exceptionShow.exceptionInFinally();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
