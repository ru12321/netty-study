package com.mrru.netty.heartbeat;

import com.mrru.netty.groupchat.GroupChatServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @className: Myserver
 * @author: 茹某
 * @date: 2021/7/27 21:54
 **/
public class MyServer
{
    public static void main(String[] args)
    {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))//在bossGroup添加一个日志处理器
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception
                        {
                            //获取到pipeline
                            ChannelPipeline pipeline = sc.pipeline();
                            //pipeline加入 空闲检测处理器 IdleStateHandler
                            /*
                            1.IdleStateHandler 是netty 提供的处理空闲状态的处理器
                            2.long readerIdleTime：表示多长时间没有读，就会发送一个心跳检测包检测是否连接
                            3.long writerIdleTime：表示多长时间没有写，就会发送一个心跳检测包检测是否连接
                            4.long allIdleTime：表示多长时间没有读和写，就会发送一个心跳检测包检测是否连接
                            5.文档说明
                                Triggers an IdleStateEvent when a Channel has not performed read, write,
                                or both operation for a while.
                            6.当IdleStateHandler 触发后，就会传递给管道的下一个handler去处理
                                通过调用 触发 下一个handler的 userEventTriggered,在该方法中去处理IdleStateHandler（读空闲，写空闲，读写空闲）

                             */
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                            //加入一个对空闲检测进一步处理的 handler 自定义的
                            pipeline.addLast(new MyServerHandler());

                        }
                    });
            System.out.println("netty服务器启动...");
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
