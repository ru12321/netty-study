package com.mrru.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @className: WebSocketServer
 * @author: 茹某
 * @date: 2021/7/28 9:44
 **/
public class WebSocketServer
{
    public static void main(String[] args)
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try
        {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception
                        {
                            ChannelPipeline pipeline = ch.pipeline();
                            //http编解码 处理器
                            pipeline.addLast(new HttpServerCodec());

                            //http块方式传输 处理器
                            pipeline.addLast(new ChunkedWriteHandler());

                            //http分段聚合 处理器
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            //支持ws协议；识别请求资源url
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            //自定义处理器
                            pipeline.addLast(new WebSocketServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(7002).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }






}
