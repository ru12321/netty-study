package com.mrru.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @className: NettyServerHandler
 * @author: 茹某
 * @date: 2021/7/26 17:24
 **/
/*
说明
1 自定义一个Handler 需要继承netty规定好的某个HandlerAdapter
2 自定义一个Handler 才能称为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter
{
    //读取数据的事件 可以读取客户端发送的消息
    /*
    特别重要的几点
    1 ChannelHandlerContext ctx:上下文对象，含有很多东西，管道pipeline，通道channel，连接的地址
    2 msg 就是客户端发送的数据，默认是以Object对象传输的

    当有数据读取时，channelRead就会触发
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        //比如这里有一个非常耗时业务，采用 eventLoop().execute 异步执行
        //即提交到channel对应的NIOEventloop的taskQueue中
        ctx.channel().eventLoop().execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(5*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~瞄2",CharsetUtil.UTF_8));
                }catch (Exception e){
                    System.out.println("发生了异常： "+e.getMessage());
                }
            }
        });

        //多个任务也是在一个NIOeventloop中，所以是一个线程，提交给同一个taskQueue
        ctx.channel().eventLoop().execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(5*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~瞄3",CharsetUtil.UTF_8));
                }catch (Exception e){
                    System.out.println("发生了异常： "+e.getMessage());
                }
            }
        });

        //用户自定义定时任务  ,提交到的是 scheduledTaskQueue
        ctx.channel().eventLoop().schedule(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(5*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~瞄4",CharsetUtil.UTF_8));
                }catch (Exception e){
                    System.out.println("发生了异常： "+e.getMessage());
                }
            }
        },5, TimeUnit.SECONDS);

        System.out.println("go on...");

//        System.out.println("服务器读取线程 "+Thread.currentThread().getName());
//        System.out.println("server ctx = " + ctx);
//        System.out.println("看看channel和pipeline的关系：");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline();//本质是一个双向链表，出栈入栈
//
//
//        //msg要转到buffer中 好处理一些
//        //将msg转成一个ByteBuf    ByteBuf是Netty提供的（性能更高），不是NIO的ByteBuf
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送消息是： "+buf.toString(CharsetUtil.UTF_8));//以UTF_8转成字符串
//        System.out.println("客户端地址："+ctx.channel().remoteAddress());
    }

    //数据读取完毕，回消息
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        //writeAndFlush  是 write + flush
        //数据写到缓冲区，并且刷新（打到通道中）
        //一般讲，要对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~瞄1",CharsetUtil.UTF_8));
    }

    //处理异常的  一般需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        ctx.close();
    }
}
