package com.mrru.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * @className: MyServerHandler
 * @author: 茹某
 * @date: 2021/7/29 11:29
 **/
public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol>
{

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception
    {
        //接收到数据 并处理
        int len = msg.getLen();
        byte[] content = msg.getContent();
        System.out.println("服务器接收到信息如下 ");
        System.out.println("长度= "+len);
        System.out.println("内容= "+ new String(content,Charset.forName("utf-8")));

        System.out.println("服务接收到消息包数量= "+ (++this.count));

        //回复消息
        String string = UUID.randomUUID().toString();

        int length = string.getBytes("utf-8").length;

        byte[] responseContent = string.getBytes("utf-8");

        //构建一个协议包
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(length);
        messageProtocol.setContent(responseContent);
        ctx.writeAndFlush(messageProtocol);

        System.out.println();
        System.out.println();
        System.out.println();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        System.out.println("异常消息= " + cause.getMessage());
        ctx.close();
    }
}
