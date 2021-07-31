package com.mrru.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @className: GroupChanServer
 * @author: 茹某
 * @date: 2021/7/27 20:26
 **/
public class GroupChatServer
{

    private int port;

    public GroupChatServer(int port){
        this.port = port;
    }

    public void run() throws InterruptedException
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception
                        {
                            //获取到pipeline
                            ChannelPipeline pipeline = sc.pipeline();
                            //pipeline加入 解码器
                            pipeline.addLast("decoder",new StringDecoder());
                            //pipeline加入 编码器
                            pipeline.addLast("encoder",new StringEncoder());
                            ////pipeline加入 自己的业务处理handler
                            pipeline.addLast(new GroupChatServerHandler());

                        }
                    });
            System.out.println("netty服务器启动...");
            ChannelFuture cf = bootstrap.bind(port).sync();

            cf.channel().closeFuture().sync();
        } finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }


    public static void main(String[] args) throws InterruptedException
    {
        new GroupChatServer(7000).run();
    }



}
