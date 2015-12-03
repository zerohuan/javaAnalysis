package com.net;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

/**
 *
 * Created by yjh on 15-12-1.
 */
public class SocketAndAddress {
    public static void testInetAddress() throws IOException {
        PrintStream out = System.out;

        //套接字连接
        Socket socket = new Socket("www.baidu.com", 80); //在构造函数中连接/阻塞
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        socket.close();
        //套接字超时时间
        Socket socket1 = new Socket();
        //设置等待超时时间，超过时间直接返回
        socket1.connect(new InetSocketAddress("www.baidu.com", 80), 1000);
        //设置等待读取的超时时间，超时返回InterruptedIOException
        socket1.setSoTimeout(5000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        try {
            System.out.println(reader.readLine());
        } catch (InterruptedIOException e) {
            e.printStackTrace();
        }
        //检查是否已经连接
        out.println(socket.isConnected());
        out.println(socket1.isConnected());
        //是否关闭
        out.println(socket1.isClosed());
        socket1.close();
        out.println(socket1.isClosed());

        /*
        基于DNS的负载均衡：
        为多个ip地址配置相同的域名，客户端请求某个域名服务时，域名服务器采用轮询的方式为客户端选择一个ip地址。为不同的客户端选择不同的服务器，从而实现负载均衡的目的。
        DNS做负载均衡简单方便，但存在不少问题。
        a.DNS无法知道解析的服务节点是否有效，若服务节点无效，DNS服务器依然会将域名解析到该节点上，造成访问服务节点无效。
        b.DNS的缓存时间较长，一旦出现问题，更新DNS信息需要等待客户数分钟甚至数十分钟，可靠性不高。
        c.DNS无法知晓服务器间的差异，也不能反映服务器当前的运行状态。其有可能将轻量级的访问发给空闲的服务器，将重量级的访问发给负载已经很重的服务器。
         */

        //根据主机名获取一个地址，依赖DNS，如果服务器是负载均衡将随机得到一个ip地址
        //通过工厂方法返回Inet4Address或Inet6Address实例
        InetAddress taobao = InetAddress.getByName("www.taobao.com");
        //得到ip地址
        byte[] ip = taobao.getAddress();
        out.println("Taobao IP:" + Arrays.toString(ip));
        //获取主机名对应的所有ip地址
        InetAddress[] baidus = InetAddress.getAllByName("www.baidu.com");
        out.println(Arrays.toString(baidus));
        //获取本机地址
        InetAddress localhost = InetAddress.getLocalHost();
        out.println(localhost);
        out.println(InetAddress.getLoopbackAddress());
        out.println(localhost.getHostAddress());
        out.println(localhost.getHostName());
        out.println(localhost.getCanonicalHostName());
    }

    public static void main(String[] args) throws Exception {
        testInetAddress();
    }

}
