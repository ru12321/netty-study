package com.mrru.netty.dubborpc.netty;


import com.mrru.netty.dubborpc.provider.ServerBootStrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @className: NettyServer
 * @author: 茹某
 * @date: 2021/7/31 10:04
 **/
public class NettyServer
{

    public static void startServer(String hostName,int port){
        startServer0(hostName,port);
    }

    private static void startServer0(String hostName,int port){

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try
        {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>()
                        {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception
                            {
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast(new StringDecoder());
                                pipeline.addLast(new StringEncoder());

                                pipeline.addLast(new NettyServerHandler());
                            }
                        });
            ChannelFuture channelFuture = serverBootstrap.bind(hostName, port).sync();
            System.out.println("NettyServer 服务提供端启动...");
            channelFuture.channel().closeFuture().sync();


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
