package com.mrru.netty.inboundandoutbound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @className: MyClientInitializer
 * @author: 茹某
 * @date: 2021/7/28 21:07
 **/

/*
顺序
    1.MyClientHandler要发数据出去时
        2.1如果是 MyLongToByteEncoder定义的Long类型，走 MyLongToByteEncoder的 decode方法
        2.2如果不是MyLongToByteEncoder定义的Long类型，直接 writeAndFlush
            3.通过Channel传出去

 */

public class MyClientInitializer extends ChannelInitializer<SocketChannel>
{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception
    {
        ChannelPipeline pipeline = ch.pipeline();

        //自定义 入站 handler 解码
        pipeline.addLast(new MyByteToLongDecoder());

        //自定义 出站 handler  编码
        pipeline.addLast(new MyLongToByteEncoder());

        //自定义 入站 handler 接收处理数据，
        //其实客户端 这里 往外发送了数据，但类型不是Long，所以没有走 MyLongToByteEncoder 自定义的 encode方法
        pipeline.addLast(new MyClientHandler());


    }



}
