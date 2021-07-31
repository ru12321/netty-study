package com.mrru.netty.dubborpc.netty;

import com.mrru.netty.dubborpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @className: NettyServerHandler
 * @author: 茹某
 * @date: 2021/7/31 10:10
 **/
public class NettyServerHandler extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        System.out.println("NettyServerHandler 服务提供者 收到消息 "+ msg);
        //自定义一个协议 判断消息是不是 要接收的
        if (msg.toString().startsWith("HelloService#hello#")){

            HelloServiceImpl helloService = new HelloServiceImpl();

            String result = helloService.hello(msg.toString().substring(msg.toString().lastIndexOf("#")+1));

            ctx.writeAndFlush(result);
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        System.out.println("异常的情况是： "+cause.getMessage());
        ctx.close();
    }
}
