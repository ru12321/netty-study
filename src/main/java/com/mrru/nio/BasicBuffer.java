package com.mrru.nio;

import java.nio.IntBuffer;

/**
 * @className: BasicBuffer
 * @author: 茹某
 * @date: 2021/7/25 8:39
 **/
public class BasicBuffer
{
    public static void main(String[] args)
    {
        IntBuffer intBuffer = IntBuffer.allocate(5);

        for (int i = 0; i < intBuffer.capacity(); i++)
        {
            intBuffer.put(i*2);
        }

        //取数据之前 将buffer转换 要做读写切换
        intBuffer.flip();

        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());  //get一次  Buffer指针向后移动一次
        }



    }



}
