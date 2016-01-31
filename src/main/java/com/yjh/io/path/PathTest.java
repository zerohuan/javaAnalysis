package com.yjh.io.path;

import java.io.File;
import java.io.IOException;

/**
 * Created by yjh on 16-1-30.
 */
public class PathTest {
    public static void main(String[] args) throws IOException {

        /*
        Class.getResource基于class文件所在目录；
        ClassLoader.getResource基于classes根目录；
         */
        //Class.getResource
//        System.out.println(IOUtils.toString(PathTest.class.getResource("/log4j2.xml")));
        //ClassLoader.getResource
//        System.out.println(IOUtils.toString(PathTest.class.getClassLoader().getResource("log4j2.xml")));
        //ClassLoader.getResourceAsStream
//        System.out.println(IOUtils.toString(PathTest.class.getClassLoader().getResourceAsStream("log4j2.xml")));

        /*
        File的Path
         */
        File f = new File(PathTest.class.getResource("/log4j2.xml").getFile());
        System.out.println(f.getPath());
    }
}
