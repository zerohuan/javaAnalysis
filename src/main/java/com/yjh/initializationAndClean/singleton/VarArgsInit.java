package com.yjh.initializationAndClean.singleton;

/**
 *  Array's initialization
 *
 * Created by yjh on 15-10-17.
 */
public class VarArgsInit {
    //overload with var argument
    public static void f(Long...longs) {
        System.out.println("f_long_varArgs");
    }

    public static void f(Character...characters) {
        System.out.println("f_character_varArgs");
    }

    public static void f(float f, Character...characters) {
        System.out.println("f_float_character_varArgs");
    }

    public static void g(float f, Character...characters) {
        System.out.println("g_float_character_varArgs");
    }

    public static void g(char c, Character...characters) {
        System.out.println("g_char_character_varArgs");
    }

    public static void main(String[] args) {
//        f(); //Error:(19, 9) java: reference to f is ambiguous
//        f();
        f(1, 'a'); //OK
//        f('a', 'b'); //Error:(19, 9) java: reference to f is ambiguous
        g('a', 'b'); //OK
    }
}
