package com.mrru.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @className: ScatterignAndGatheringTest
 * @author: 茹某
 * @date: 2021/7/25 15:09
 **/
public class ScatterignAndGatheringTest
{
    public static void main(String[] args) throws IOException
    {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        serverSocketChannel.socket().bind(inetSocketAddress);

        ByteBuffer[] buffers = new ByteBuffer[2];
        buffers[0] = ByteBuffer.allocate(5);
        buffers[1] = ByteBuffer.allocate(3);

        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLength = 8;

        //----------------------------buffer数组 往里写-----------------------
        while (true){
            int byteRead = 0;

            while (byteRead<messageLength){
                long read = socketChannel.read(buffers);
                byteRead += read;

                System.out.println("buffers数组从 socketChannel  读到的字节数byteRead是： "+byteRead);
                //流打印
                Arrays.asList(buffers).stream().map(byteBuffer -> "position= "+byteBuffer.position()+
                        "  limit= "+byteBuffer.limit()).forEach(System.out::println);
            }

            //----------------------------buffer数组 往出读-----------------------
            //反转所有buffer
            Arrays.asList(buffers).forEach(buffer -> buffer.flip());
            //将数据显示到客户端
            long byteWrite = 0;
            while (byteWrite <messageLength){
                long write = socketChannel.write(buffers);
                byteWrite += write;
            }
            //将所有的buffer  clear
            Arrays.asList(buffers).forEach(buffer -> buffer.clear());
            System.out.println("buffers数组向 socketChannel 写出去的字节数byteWrite是：  "+byteWrite);
        }




    }
}
