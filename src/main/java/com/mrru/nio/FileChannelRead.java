package com.mrru.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * @className: FileChannelRead
 * @author: 茹某
 * @date: 2021/7/25 10:07
 **/
public class FileChannelRead
{
    //使用前面学习后的 ByteBuffer（缓冲）和 FileChannel（通道），
    // 将 file01.txt 中的数据读入到程序，并显示在控制台屏幕
    public static void main(String[] args) throws IOException
    {
        //创建输入流  输入输出是相对于java程序的
        File file = new File("fileChannel.txt");
        FileInputStream fis = new FileInputStream(file);

        //通过 fileInputStream 获取对应的 FileChannel -> 实际类型 FileChannelImpl
        FileChannel channel = fis.getChannel();

        //创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());

        //将通道的数据 读出到 Buffer    注意read和write是以channel为研究对象的 往出读，往里写
        channel.read(buffer);

        //缓冲区转换
        buffer.flip();

        //将 byteBuffer 的字节数据转成 String
        System.out.println(new String(buffer.array()));

        //关闭流
        fis.close();


    }

}
