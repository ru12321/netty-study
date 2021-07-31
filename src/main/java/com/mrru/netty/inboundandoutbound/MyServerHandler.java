package com.mrru.netty.inboundandoutbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @className: MyServerHandler
 * @author: 茹某
 * @date: 2021/7/28 20:54
 **/
public class MyServerHandler extends SimpleChannelInboundHandler<Long>
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception
    {
        System.out.println("从客户端 "+ctx.channel().remoteAddress()+" 读取到 "+msg);

        //给客户端发送一个long
        ctx.writeAndFlush(98675L);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        ctx.close();
    }
}
