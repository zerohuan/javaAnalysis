package com.jmx;

/**
 * 一个标准MBean
 * Created by yjh on 16-1-29.
 */
public interface HelloWorldMBean {
    String getName();

    void setName(String name);

    void printHello();

    void printHello(String whoName);
}
