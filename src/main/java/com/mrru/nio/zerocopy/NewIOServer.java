package com.mrru.nio.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @className: NewIOServer
 * @author: 茹某
 * @date: 2021/7/26 10:43
 **/
public class NewIOServer
{
    public static void main(String[] args) throws IOException
    {
        //创建serverSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //绑定地址
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7001);
        serverSocketChannel.bind(inetSocketAddress);

        //创建buffer
        ByteBuffer buffer = ByteBuffer.allocate(4096);

        //循环监听
        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();
            //循环读取
            while (true){
                int read = 0;
                //循环读取channel到 Buffer
                while (read != -1){
                    read = socketChannel.read(buffer);
                    buffer.rewind();//倒带  position=0 mark作废  下次可以继续用buffer读取
                }
            }
        }
    }



}
