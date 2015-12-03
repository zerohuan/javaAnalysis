package com.net;

import org.apache.commons.io.IOUtils;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * Created by yjh on 15-12-1.
 */
public class URIAndURLTest {
    public static void main(String[] args) throws Exception {
        URI uri = URI.create("http://localhost:8080/example/servlets/servlet/空幻?author=空幻#success");
        System.out.println(uri.getScheme());
        System.out.println(uri.getSchemeSpecificPart());
        System.out.println(uri.getAuthority());
        System.out.println(uri.getPath());
        System.out.println(uri.getQuery());
        System.out.println(uri.getFragment() + "\n");

        System.out.println(uri.getHost());
        System.out.println(uri.getPort());

        URL url = new URL("https://www.taobao.com/");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");

//        connection.setDoOutput(true); //如果要写入一定要先设置
        connection.connect();
        //连接后可以查看响应头
        System.out.println(connection.getHeaderFields());
//        System.out.println(connection.getContent());
        System.out.println(IOUtils.toString(connection.getInputStream()));



    }
}
