package com.mrru.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;

/**
 * @className: NettyServer
 * @author: 茹某
 * @date: 2021/7/26 16:57
 **/
public class NettyServer
{

    public static void main(String[] args) throws InterruptedException
    {
        //创建BossGroup和  WorkerGroup
        /*
        说明
            1 创建两个线程组 bossGroup 和 workerGroup
            2 bossGroup只是处理连接请求，真正的与客户端业务处理会交给 workerGroup
            3 两个都是无限循环
            4 bossGroup 和 workerGroup含有的子线程(NioEventLoop)的个数
                默认是CPU核数 * 2 = 8
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            //创建服务器端的启动对象 配置启动参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程来设置
            bootstrap.group(bossGroup,workerGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG,128)//设置线程队列的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//设置连接是保持状态
                    .childHandler(new ChannelInitializer<SocketChannel>()//创建一个通道初始化对象
                        {
                            //给pipeline设置处理器
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception
                            {
                                System.out.println("客户socketchannel hashcode= "+ch.hashCode());//可以使用一个集合管理SocketChannel，
                                //在推送消息时，可以将业务加入到各个channel对应的NIOEventLoop的 taskqueue或者 scheduledTaskQueue中
                                ch.pipeline().addLast(new NettyServerHandler());//往管道后面增加一个处理器,可以自定义我们的
                            }
                        });//给workerGroup的 EventGroup对应的管道设置处理器

            System.out.println("...服务器 is ready...");
            //绑定一个端口并且同步，生成一个channelFuture对象
            //启动服务器，绑定端口
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //给cf注册监听器，监控我们关心得事件
            cf.addListener(new ChannelFutureListener()
            {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception
                {
                    if (cf.isSuccess()){
                        System.out.println("监听端口 6668 成功");
                    }else {
                        System.out.println("监听端口 6668 失败");
                    }
                }
            });

            //对关闭通道进行监听    netty的异步机制
            cf.channel().closeFuture().sync();
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
