package com.mrru.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * @className: MyServerHandler
 * @author: 茹某
 * @date: 2021/7/29 11:24
 **/
public class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf>
{
    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        for (int i = 0; i < 10; i++)
        {
            ByteBuf buf = Unpooled.copiedBuffer("hello server " + i, Charset.forName("utf-8"));
            ctx.writeAndFlush(buf);
        }

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception
    {
        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);

        String message = new String(buffer, Charset.forName("utf-8"));
        System.out.println("客户端接收到消息= "+message);

        System.out.format("客户端第%d次收到数据\n",count++);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        ctx.close();
    }
}
