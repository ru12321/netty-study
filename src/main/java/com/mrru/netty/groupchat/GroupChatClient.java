package com.mrru.netty.groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @className: GroupChatClient
 * @author: 茹某
 * @date: 2021/7/27 21:18
 **/
public class GroupChatClient
{
    //属性
    private final String host;
    private final int port;

    public GroupChatClient(String host,int port){
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException
    {
        EventLoopGroup group = new NioEventLoopGroup();

        try
        {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception
                        {
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入 相关的handler 解码器
                            pipeline.addLast("decoder",new StringDecoder());
                            pipeline.addLast("encoder",new StringEncoder());
                            //加入自己的handler   因为客户端也要收到服务器 转发的消息，不是只有下面的发送而已
                            pipeline.addLast(new GroupChatClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host,port).sync();
            //得到channel
            Channel channel = channelFuture.channel();
            System.out.println("--------"+ channel.localAddress()+"--------");
            //客户端需要输入信息，创建一个扫描器
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String msg = scanner.nextLine();
                //通过channel发送数据
                channel.writeAndFlush(msg+"--");
            }

        } finally
        {
            group.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException
    {
        new GroupChatClient("127.0.0.1",7000).run();
    }

}
