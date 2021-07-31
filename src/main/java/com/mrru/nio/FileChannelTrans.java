package com.mrru.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @className: FileChannelTrans
 * @author: 茹某
 * @date: 2021/7/25 11:23
 **/
public class FileChannelTrans
{
    //拷贝图片 使用管道的transfrom
    public static void main(String[] args) throws IOException
    {
        //创建输出输入流
        FileInputStream fis = new FileInputStream("lisa.png");
        FileOutputStream fos = new FileOutputStream("lisa2.png");
        //获取对应管道
        FileChannel source = fis.getChannel();
        FileChannel destination = fos.getChannel();
        //trans方法调用
        destination.transferFrom(source,0,source.size());
        //关闭流
        fis.close();
        fos.close();
    }

}
