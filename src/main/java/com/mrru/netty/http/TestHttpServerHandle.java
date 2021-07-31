package com.mrru.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @className: TestHttpServerHandle
 * @author: 茹某
 * @date: 2021/7/27 10:59
 **/

/*
说明
1 SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter 子类 ，方法更多
2 HttpObject 客户端和服务器相互通讯的数据被封装成 HttpObject
 */
public class TestHttpServerHandle extends SimpleChannelInboundHandler<HttpObject>
{

    //有读取事件时会触发这个方法  读取 客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception
    {
        //判断msg是不是 httpquest请求
        if (msg instanceof HttpRequest){

            HttpRequest httpRequest = (HttpRequest) msg;
            //获取url,然后可以过滤指定的资源
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 favicon.ico，不做响应");
                return;
            }

            System.out.println("pipeline hashcode "+ctx.pipeline().hashCode());
            System.out.println("TestHandler hashcode "+this.hashCode());

            System.out.println("msg类型= "+msg.getClass());
            System.out.println("客户端地址 "+ctx.channel().remoteAddress());

            //回复信息给浏览器
            ByteBuf content = Unpooled.copiedBuffer("hello 我是服务器", CharsetUtil.UTF_8);

            //构造一个http的响应 即httpResponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
            //将构建好的response返回

            ctx.writeAndFlush(response);//把response写到管道中去
        }
    }
}
