package com.mrru.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * @className: WebSocketServerHandler
 * @author: 茹某
 * @date: 2021/7/28 10:05
 **/
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>
{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception
    {
        //以text()方式才能获取到frame形式的消息
        System.out.println("服务器收到消息 ： "+msg.text());

        //以frame形式包装 要回复的消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间"+ LocalDateTime.now()+" "+msg.text()));

    }

    //websocket客户端连接后 触发方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("handlerAdded 被调用 "+ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用 "+ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("handlerRemoved 被调用 "+ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        System.out.println("异常发生 "+ cause.getMessage());
        ctx.close();
    }
}
