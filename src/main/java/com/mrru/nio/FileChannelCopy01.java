package com.mrru.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @className: FileChannelCopy01
 * @author: 茹某
 * @date: 2021/7/25 10:55
 **/
public class FileChannelCopy01
{
    //使用一个 Buffer 完成文件读取、写入
    public static void main(String[] args) throws IOException
    {

        method2();


    }

    //老师的
    private static void method2() throws IOException
    {
        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true) { //循环读取

            //这里有一个重要的操作，一定不要忘了
            /*
            public final Buffer clear() {
                position = 0;
                limit = capacity;
                mark = -1;
                return this;
            }
            */

            byteBuffer.clear(); //清空 buffer   用完一次buffer后，position和Limit相等，用之前要复位一下
            int read = fileChannel01.read(byteBuffer);
            System.out.println("read = " + read);
            if (read == -1) { //表示读完
                break;
            }

            //将 buffer 中的数据写入到 fileChannel02--2.txt
            byteBuffer.flip();
            fileChannel02.write(byteBuffer);
        }

        //关闭相关的流
        fileInputStream.close();
        fileOutputStream.close();

    }

    //我的方法
    private static void method1() throws IOException
    {
        //创建输入流
        File file = new File("fileChannel.txt");
        FileInputStream fis = new FileInputStream(file);
        //创建对应的管道
        FileChannel channel = fis.getChannel();

        //创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());

        //通道往出读到 缓冲区
        channel.read(buffer);

        //--------------------------------------------------------//
        //创建输出流
        FileOutputStream fos = new FileOutputStream("fileChannel02");
        //创建对应的管道
        FileChannel channel1 = fos.getChannel();

        //缓冲区反转
        buffer.flip();
        //缓冲区往另一个通道写
        channel1.write(buffer);

        //关闭流
        fos.close();
        fis.close();

    }
}
