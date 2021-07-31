package com.mrru.netty.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @className: MyClientInitializer
 * @author: 茹某
 * @date: 2021/7/29 11:23
 **/
public class MyClientInitializer extends ChannelInitializer<SocketChannel>
{


    @Override
    protected void initChannel(SocketChannel ch) throws Exception
    {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new MyMessageEncoder());

        pipeline.addLast(new MyMessageDecoder());

        pipeline.addLast(new MyClientHandler());
    }



}
