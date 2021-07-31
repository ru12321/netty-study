package com.mrru.netty.inboundandoutbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @className: MyByteToLongDecoder
 * @author: 茹某
 * @date: 2021/7/28 20:49
 **/
public class MyByteToLongDecoder extends ByteToMessageDecoder
{
    /**
     * decode方法会根据接收的数据，被调用多次！ 直到 ByteBuf没有更多的可读字节为止
     * 如果list out不为空，就会将list的内容传递给下一个ChannelInboundHandler处理器
     * 该处理器的方法也会被调用多次
     *
     * @param ctx  上下文对象
     * @param in 入站的ByteBuf
     * @param out List集合 将解码后的数据传给下一个Handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        System.out.println("MyByteToLongDecoder decode 被调用");
        //因为Long是八个字节
        if (in.readableBytes() >=8){
            out.add(in.readLong());
        }

    }
}
