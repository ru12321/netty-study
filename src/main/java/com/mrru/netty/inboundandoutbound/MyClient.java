package com.mrru.netty.inboundandoutbound;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @className: MyClient
 * @author: 茹某
 * @date: 2021/7/28 21:03
 **/
public class MyClient
{
    public static void main(String[] args)
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        try
        {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(bossGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new MyClientInitializer());//自定义初始 channel对象

            ChannelFuture channelFuture = bootstrap.connect("localhost",7000).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } finally
        {
            bossGroup.shutdownGracefully();
        }


    }
}
