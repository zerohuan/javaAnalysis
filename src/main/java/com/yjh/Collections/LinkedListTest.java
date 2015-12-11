package com.yjh.Collections;

import com.yjh.util.ClassUtil;

import java.lang.reflect.Field;
import java.util.LinkedList;

/**
 * Created by yjh on 15-12-9.
 */
public class LinkedListTest {


    public static void main(String[] args)
            throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("123");
        linkedList.add("321");
        Class<?> claz = LinkedList.class;
        Field field = claz.getDeclaredField("first");
        field.setAccessible(true);
        Object node = field.get(linkedList);
        Field lastField = claz.getDeclaredField("last");
        lastField.setAccessible(true);
        Object node2 = lastField.get(linkedList);

        ClassUtil.printStatus(node, System.out);
        ClassUtil.printStatus(node2, System.out);
    }
}
