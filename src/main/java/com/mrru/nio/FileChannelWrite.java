package com.mrru.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @className: NIOFileChannel_write
 * @author: 茹某
 * @date: 2021/7/25 9:58
 **/
public class FileChannelWrite
{
    //FileChannel 往文件写数据
    public static void main(String[] args) throws IOException
    {
        //数据
        String str = "hello,file channel";

        //创建输出流，创建管道   输入输出是相对于java程序的
        FileOutputStream fos = new FileOutputStream("fileChannel.txt");
        FileChannel channel = fos.getChannel();

        //创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        //数据写入缓冲区
        buffer.put(str.getBytes());

        //缓冲区切换
        buffer.flip();

        //缓冲区写到管道   注意read和write是以channel为研究对象的 往出读，往里写
        channel.write(buffer);

        //关闭流
        fos.close();
    }


}
