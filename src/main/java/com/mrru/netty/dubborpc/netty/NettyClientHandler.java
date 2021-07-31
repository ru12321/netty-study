package com.mrru.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @className: NettyClientHandler
 * @author: 茹某
 * @date: 2021/7/31 10:50
 **/
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable
{

    //提出这几个属性，后面多个方法要用到
    private ChannelHandlerContext context;//上下文
    private String result;//返回的结果
    private String para; //客户端调用方法时，传入的参数

    //1 与服务器的连接创建后，就会被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        context = ctx;
    }

    //2 设置了要传入的参数
    public void setPara(String para)
    {
        this.para = para;
    }

    //3 发送数据
    //5 返回结果
    @Override
    public synchronized Object call() throws Exception
    {
        System.out.println("call 第一次 被调用 ");
        context.writeAndFlush(para);
        //进行wait，
        wait();
        //5 返回结果
        //等待channelRead方法获取服务器的结果后，被channelRead唤醒
        System.out.println("call 第二次 被调用");
        return result;
    }

    //4 接收服务提供者的回复结果
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        System.out.println("channelRead 被调用");
        result = msg.toString();
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        ctx.close();
    }

}
