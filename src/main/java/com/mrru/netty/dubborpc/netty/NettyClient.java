package com.mrru.netty.dubborpc.netty;

import com.mrru.proxy.dynamic.ITeacherDao;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @className: NettyClient
 * @author: 茹某
 * @date: 2021/7/31 11:19
 **/
public class NettyClient
{
    //创建线程池
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    //组合 NettyClientHandler
    private static NettyClientHandler clientHandler;


    //初始化服务器消费者
    private static void initClient(){

        clientHandler = new NettyClientHandler();

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception
                    {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());

                        pipeline.addLast(clientHandler);
                    }
                });
        try
        {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1",7000).sync();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }


    //使用代理模式，创建代理对象，接管代理方法
    /**
     *
     * @param serviceClass  接口的类对象 HelloService.class
     * @param protocolName  遵守的协议
     * @return
     */
    public Object getBean(final Class<?> serviceClass, final String protocolName){


        //new Class<?>[] {serviceClass}  被代理类的所有接口信息; 便于生成的代理类可以具有代理类接口中的所有方法
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader()
                , new Class<?>[] {serviceClass},(proxy,method,args) -> {
                    if (clientHandler == null){
                        initClient();
                    }
                    //设置要发给服务器端的信息
                    clientHandler.setPara(protocolName + args[0]);

                    //返回的是调用的方法的返回值
                    //把clientHandler提交到 线程池去执行！
                    return executorService.submit(clientHandler).get();
                });
    }




}
