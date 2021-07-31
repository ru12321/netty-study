package com.mrru.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @className: GroupChatServerHandler
 * @author: 茹某
 * @date: 2021/7/27 20:34
 **/
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String>
{
    //定义一个channle组， 管理所有的channel
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    //表示连接建立，一旦连接，第一个被执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
        Channel channel = ctx.channel();
        /*
            将该客户加入聊天的信息推送给其它在线的客户端

            该方法会将channelGroup中所有的channel遍历，并发送消息，不需要自己遍历
         */
        channelGroup.writeAndFlush("[客户端]--"+channel.remoteAddress()+"--加入聊天--"+sdf.format(new Date())+"\n");
        channelGroup.add(channel);

    }

    //断开连接 将客户离开的消息推送给当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
    {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]--"+channel.remoteAddress()+"--离开聊天--"+sdf.format(new Date())+"\n");
        System.out.println("channelGroup size = "+channelGroup.size());

    }

    //表示channel处于活动状态 提示上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println(ctx.channel().remoteAddress()+" 上线了~"+sdf.format(new Date()));

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println(ctx.channel().remoteAddress()+" 下线了~"+sdf.format(new Date()));
    }


    //真正的读数据业务
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception
    {
        Channel selfChannel = ctx.channel();
        channelGroup.forEach(channel -> {//将本客户的消息 转发给其它客户
            if (channel != selfChannel){
                //把读到的数据 转发给其它客户---》也就是其它的channel，不能是整个channel组哦！
                channel.writeAndFlush("[客户端]--"+channel.remoteAddress()+"--发送消息--"+msg+sdf.format(new Date())+"\n");
            }else{//本客户端 也显示一下
                channel.writeAndFlush("[自己]--"+channel.remoteAddress()+"--发送消息--"+msg+sdf.format(new Date())+"\n");
            }
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        ctx.close();
    }


}
