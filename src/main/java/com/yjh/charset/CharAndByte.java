package com.yjh.charset;

import com.yjh.util.PrintUtil;

import java.io.IOException;

/**
 * show different formats of encoding
 *
 * Created by yjh on 15-10-10.
 */
public class CharAndByte {

    //encode in Java
    public static void main(String[] args) throws IOException {
        String str = "I am 阎锦恒";

        //ISO-8859-1, "3f" presents "?", it is unable to present chinese character, CoderResult is overflow
        System.out.println(PrintUtil.toHex(str.getBytes("iso-8859-1")));

        //GB2312
        System.out.println(PrintUtil.toHex(str.getBytes("gb2312")));

        //GBK, GBK covers GB2312
        System.out.println(PrintUtil.toHex(str.getBytes("gbk")));

        //UTF_16, 2 bytes encode, first 2 bytes indicates byte order, "FE FF" is Big-endian, "FF FE" is Little-endian
        System.out.println(PrintUtil.toHex(str.getBytes("utf-16")));

        //UTF_8
        System.out.println(PrintUtil.toHex(str.getBytes("utf-8")));

        //My environment is BigEndian
        System.out.println((0x960e));
        System.out.println((int)'阎');


        //encode and decode
        String str1 = "淘！我喜欢！";
        String str1GBK2ISO = new String(str1.getBytes("gbk"), "iso-8859-1");
        System.out.println(str1GBK2ISO);

        String str1ISO2ISO = new String(str1.getBytes("iso-8859-1"), "iso-8859-1");
        System.out.println(str1ISO2ISO);

        String str1GBK2ISO_GBK2GBK = new String(new String(str1.getBytes("gbk"), "iso-8859-1").getBytes("gbk"), "gbk");
        System.out.println(str1GBK2ISO_GBK2GBK);
    }


}
