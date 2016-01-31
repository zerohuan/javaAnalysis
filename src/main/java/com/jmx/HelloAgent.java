package com.jmx;

import com.sun.jdmk.comm.HtmlAdaptorServer;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * Created by yjh on 16-1-29.
 */
public class HelloAgent {
    public static void main(String[] args) throws Exception  {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName helloName = new ObjectName("yjh:name=HelloWorld");
        HelloWorld helloWorld = new HelloWorld();
        server.registerMBean(helloWorld, helloName);
        ObjectName adapterName = new ObjectName("HelloAgent:name=htmladapter,port=8082");
        HtmlAdaptorServer adaptorServer = new HtmlAdaptorServer();
        server.registerMBean(adaptorServer, adapterName);

        Jack sender = new Jack();
        server.registerMBean(sender, new ObjectName("HelloAgent:name=jack"));

        sender.addNotificationListener(new HelloListener(), null, helloWorld);
        adaptorServer.start();

        System.out.println("start...");
    }
}
