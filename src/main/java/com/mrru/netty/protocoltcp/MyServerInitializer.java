package com.mrru.netty.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @className: MyServerInitializer
 * @author: 茹某
 * @date: 2021/7/29 11:27
 **/
public class MyServerInitializer extends ChannelInitializer<SocketChannel>
{

    @Override
    protected void initChannel(SocketChannel ch) throws Exception
    {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new MyMessageDecoder());

        pipeline.addLast(new MyMessageEncoder());

        pipeline.addLast(new MyServerHandler());
    }



}
