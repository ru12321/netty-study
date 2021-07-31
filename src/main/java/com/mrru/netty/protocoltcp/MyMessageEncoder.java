package com.mrru.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @className: MyMessageEncoder
 * @author: 茹某
 * @date: 2021/7/29 15:31
 **/
public class MyMessageEncoder extends MessageToByteEncoder<MessageProtocol>
{
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception
    {
        System.out.println("MyMessageEncoder encode 编码 被调用");
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}
