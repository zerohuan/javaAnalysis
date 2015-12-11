package com.yjh.reflect;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反射创建数组
 *
 * Created by yjh on 15-12-7.
 */
public class ReflectAndArray {
    //泛型与数组的小例子
    static List<String>[] ls = new ArrayList[10];
    static List<?>[] ls1 = new ArrayList<?>[10];
    static List<String> list = new ArrayList<>();

    /**
     * 不用泛型，自己保证类型正确，自己进行强制类型转换
     * @param a
     * @param newArrayLen
     * @return
     */
    private static Object goodCopyOf(Object a, int newArrayLen) {
        Class<?> cl = a.getClass();
        if(!cl.isArray()) return null;
        Class<?> componentType = cl.getComponentType();
        //用反射创建指定组件类型的数组对象
        int length = Array.getLength(a);
        Object newArr = Array.newInstance(componentType, newArrayLen);
        System.arraycopy(a, 0, newArr, 0, Math.min(length, newArrayLen));
        return newArr;
    }

    /**
     * 框架的意义就在于框架可以做的事就不要给框架的使用者做，比如说类型检查和转换
     * @param oldArr
     * @param newArrLen
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] copyOf(T[] oldArr, int newArrLen) {
        Class<?> cl = oldArr.getClass();
        Class<?> componentType = cl.getComponentType();
        int length = Array.getLength(oldArr);
        Object newArr = Array.newInstance(componentType, newArrLen);
        System.arraycopy(oldArr, 0, newArr, 0, Math.min(length, newArrLen));
        return (T[])newArr;
    }

    public static <T,U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
        @SuppressWarnings("unchecked")
        T[] copy = ((Object)newType == (Object)Object[].class)
                ? (T[]) new Object[newLength]
                : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    //擦除之后创建的是Object[]，因此你即使参数是ArrayList<String>也是可以放进去的
    //但是Object显然不会帮你检查类型
    private static <T> void test(T...ts) {
        int len = ts.length;
    }

    public static <T> void gMethod(T t, Object o1, Comparable<? super T> o2) {
    }

    public static void main(String[] args) throws NoSuchMethodException {
        //Object[] objs = new int[10]; //不会进行自动装箱
        String[] str = new String[10];
        str[0] = "123";
//        Integer[] integers = copyOf(str, 100, Integer[].class);

        Class<String> stringClass = String.class;
        System.out.println(Arrays.toString(stringClass.getGenericInterfaces()));

        Class<ReflectAndArray> claz = ReflectAndArray.class;
        claz.isInstance(null);
        Method method = claz.getDeclaredMethod("gMethod", Object.class, Object.class, Comparable.class);
        //jdk 7没有Parameter
//        Parameter parameter = method.getParameters()[0];
//        Parameter objParameter = method.getParameters()[1];
//        Parameter o1Parameter = method.getParameters()[2];
//        System.out.println(parameter.getType());
//        System.out.println(parameter.getParameterizedType() instanceof TypeVariable);
//        System.out.println(objParameter.getType());
//        System.out.println(objParameter.getParameterizedType() instanceof Class);
//        System.out.println(o1Parameter.getType());
//        System.out.println(o1Parameter.getParameterizedType() instanceof  ParameterizedType);
    }

}
