package com.yjh.generic;

//import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;

/**
 *
 * Created by yjh on 15-12-7.
 */
public class ReflectShowGeneric {
    public static <T extends Comparable<? super T>> T swap(T[] min) {
        return min[0];
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Class<ReflectShowGeneric> clazz = ReflectShowGeneric.class;
        Method method = clazz.getDeclaredMethod("swap", Comparable[].class);
        TypeVariable[] typeVariables =  method.getTypeParameters();
//        for(TypeVariable t : typeVariables) {
//            for(AnnotatedType annotatedType : t.getAnnotatedBounds()) {
//                System.out.println(annotatedType.getType());
//            }
//        }
    }
}
