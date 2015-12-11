package com.yjh.reflect;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * Created by yjh on 15-12-7.
 */
public class ReflectClass {
    public static void main(String[] args) throws Exception {
        String name = "";
        try (Scanner in = new Scanner(System.in)) {
            while(true) {
                System.out.println("Enter className (e.g. java.util.Date), Enter q/quit to exit.");
                StringBuilder sb = new StringBuilder();
                name = in.next();
                //退出
                if(name.equals("quit") || name.equals("q"))
                    break;
                //获取Class对象和超类class对象
                Class cl = Class.forName(name);
                Type superClass = cl.getGenericSuperclass();
                Type[] interfaces =  cl.getGenericInterfaces();

                String modifiers = Modifier.toString(cl.getModifiers());
                if(!StringUtils.isEmpty(modifiers)) sb.append(modifiers).append(" ");
                //是否为接口
                if(!cl.isInterface())
                    sb.append("class ");
                sb.append(name);
                printTypes(cl.getTypeParameters(), "<", ",", ">", true, sb);
                if(superClass != null) {
                    sb.append(" extends ");
                    printType(superClass, false, sb);
                }
                printTypes(interfaces, "implements ", ",", "", true, sb);
                sb.append("{\n");

                //获取Fields
                Field[] fields = cl.getDeclaredFields();
                printFields(fields, sb);

                //获取构造器
                Constructor[] constructors = cl.getDeclaredConstructors();
                printConstructors(constructors, sb);

                //获取方法
                Method[] methods = cl.getDeclaredMethods();
                printMethods(methods, sb);

                sb.append("}");

                System.out.println(sb.toString());
                sb.setLength(0);
            }
        }
    }

    private static void printFields(Field[] fields, StringBuilder sb) {
        int len;
        for(int i = 0; i < (len = fields.length); ++i) {
            Field field = fields[i];
            sb.append("\t");
            printModifiers(field, sb);
            printType(field.getGenericType(), false, sb);
            sb.append(" ").append(field.getName());
            sb.append(";\n");
            if(i == len - 1)
                sb.append("\n");
        }
    }

    private static void printConstructors(Constructor[] constructors, StringBuilder sb) {
        int len;
        Constructor constructor;
        for(int i = 0; i < (len = constructors.length); ++i) {
            constructor = constructors[i];
            sb.append("\t");
            printModifiers(constructor, sb);
            sb.append(constructor.getName());
            printTypes(constructor.getParameterTypes(), "(", ",", ")", false, sb);
            sb.append(";\n");
            if(i == len - 1)
                sb.append("\n");
        }
    }

    private static void printMethods(Method[] methods, StringBuilder sb) {
        for(int i = 0; i < methods.length; ++i) {
            Method method = methods[i];
            sb.append("\t");
            printModifiers(method, sb);
            //打印类型变量定义
            printTypes(method.getTypeParameters(), "<", ",", ">", true, sb);
            //返回类型
            printType(method.getGenericReturnType(), false, sb);
            sb.append(" ");
            sb.append(method.getName());
            printTypes(method.getGenericParameterTypes(), "(", ",", ")", false, sb);
            //获取异常
            Class<?>[] exceptionTypes = method.getExceptionTypes();
            printTypes(exceptionTypes, "throws ", ",", "", false, sb);
//            sb.append(Arrays.toString(method.getGenericExceptionTypes()));
            sb.append(";\n");
        }
    }

//    private static void printParameters(Parameter[] parameters, StringBuilder sb) {
//        int len;
//        sb.append("(");
//        for(int i = 0; i < (len = parameters.length); ++i) {
//            Parameter parameter = parameters[i];
//            printType(parameter.getType(), false, sb);
//            sb.append(" ").append(parameter.getName());
//            if(i < len - 1)
//                sb.append(",");
//        }
//        sb.append(")");
//    }

    private static void printModifiers(Member member, StringBuilder sb) {
        sb.append(Modifier.toString(member.getModifiers())).append(" ");
    }

    //增加泛型信息的输出
    private static void printTypes(Type[] types, String pre,
                                   String seq, String suf, boolean isDefinition, StringBuilder sb) {
        if(pre.equals("extends") && Arrays.equals(types, new Type[]{Object.class})) return;
        int len = types.length;
        if(len > 0) sb.append(pre);
        for(int i = 0; i < len; i++) {
            printType(types[i], isDefinition, sb);
            if(i < len - 1) {
                sb.append(seq);
            }
        }
        if(len > 0) sb.append(suf);
    }

    private static void printType(Type type, boolean isDefinition, StringBuilder sb) {
        if(type instanceof Class) {
            Class<?> claz = (Class<?>)type;
            sb.append(claz.getName());
        } else if(type instanceof TypeVariable) {
            //类型变量
            TypeVariable t = (TypeVariable)type;
            sb.append(t.getName());
            if(isDefinition)
                printTypes(t.getBounds(), " extends ", "&", "", false, sb);
        } else if(type instanceof WildcardType) {
            //通配符
            WildcardType wildcardType = (WildcardType)type;
            sb.append("?");
            printTypes(wildcardType.getLowerBounds(), " super ", "&", "", false, sb);
            printTypes(wildcardType.getUpperBounds(), " extends ", "&", "", false, sb);
        } else if(type instanceof GenericArrayType) {
            //泛型数组
            GenericArrayType arrayType = (GenericArrayType)type;
            sb.append(arrayType.getGenericComponentType()).append("[]");
        } else if(type instanceof ParameterizedType) {
            //带类型参数的类型
            ParameterizedType parameterizedType = (ParameterizedType)type;
            Type ownerType = parameterizedType.getOwnerType();
            if(ownerType != null) {
                printType(ownerType, false, sb);
                sb.append(".");
            }
            printType(parameterizedType.getRawType(), false, sb);
            printTypes(parameterizedType.getActualTypeArguments(), "<", ",", ">", false, sb);
        }
    }

    private static class Pair<T extends Comparable<? super T>> {
        T first;
        T second;

        private static <U extends Comparable<? super U>> U min(U[] us) {
            return us[0];
        }

        public T getFirst() {
            return first;
        }

        public void setFirst(T first) {
            this.first = first;
        }

        public T getSecond() {
            return second;
        }

        public void setSecond(T second) {
            this.second = second;
        }
    }
    //com.yjh.reflect.ReflectClass$Pair
}
