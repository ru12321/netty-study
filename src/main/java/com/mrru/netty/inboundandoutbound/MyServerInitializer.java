package com.mrru.netty.inboundandoutbound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @className: MyServerInitializer
 * @author: 茹某
 * @date: 2021/7/28 20:44
 **/
public class MyServerInitializer extends ChannelInitializer<SocketChannel>
{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception
    {
        ChannelPipeline pipeline = ch.pipeline();

        //自定义 入站 handler 解码
        pipeline.addLast(new MyByteToLongDecoder());

        //自定义 出栈 handler 编码
        pipeline.addLast(new MyLongToByteEncoder());

        //自定义  handler
        pipeline.addLast(new MyServerHandler());
        System.out.println("xx");

    }



}
