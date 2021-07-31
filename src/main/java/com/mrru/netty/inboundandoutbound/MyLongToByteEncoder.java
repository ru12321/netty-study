package com.mrru.netty.inboundandoutbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @className: MyLongToByteEncoder
 * @author: 茹某
 * @date: 2021/7/28 21:10
 **/
public class MyLongToByteEncoder extends MessageToByteEncoder<Long>
{
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception
    {
        System.out.println("MyLongToByteEncoder encode 被调用 ");
        System.out.println("msg= "+ msg);
        out.writeLong(msg);
    }
}
