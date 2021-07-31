package com.mrru.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @className: NewIOClient
 * @author: 茹某
 * @date: 2021/7/26 10:57
 **/
public class NewIOClient
{

    public static void main(String[] args) throws IOException
    {
        //获得socketChannel
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",7001));
        String filename = "lisa.png";

        //transferTo，用于两个channel之间的传输    底层使用的是零拷贝 不用buffer
        FileChannel fileChannel = new FileInputStream(filename).getChannel();

        //准备发送
        long start = System.currentTimeMillis();

        //返回的是传输的字节数
        //在linxu下，一个transferTo方法就可以完成传输了
        //在windows下，一个transferTo只能发送8m，就需要分段传输文件 ——这里没有做，做的话就fileChannel.size()/8/1024/1024得到调用次数
        //transferTo底层使用的是零拷贝
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("发送的总字节数是 = " + transferCount+" 耗时："+(System.currentTimeMillis()-start));

        //关闭
        fileChannel.close();

    }



}
