package com.mrru.netty.inboundandoutbound;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @className: MyServer
 * @author: 茹某
 * @date: 2021/7/28 20:38
 **/
public class MyServer
{
    public static void main(String[] args)
    {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyServerInitializer());//自定义 初始化channel

            ChannelFuture cf = bootstrap.bind(7000).sync();
            cf.channel().closeFuture().sync();

        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }



}
