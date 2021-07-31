package com.mrru.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @className: NettyClientHandler
 * @author: 茹某
 * @date: 2021/7/26 19:13
 **/
public class NettyClientHandler extends ChannelInboundHandlerAdapter
{

    //当通道就绪时就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
       //发送Student对象 到服务器
        StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(6).setName("王二").build();

        ctx.writeAndFlush(student);

    }

    //当通道有读取事件时，会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息是："+buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址： "+ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        ctx.close();
    }


}
