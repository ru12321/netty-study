package com.mrru.netty.dubborpc.provider;

import com.mrru.netty.dubborpc.netty.NettyServer;

/**
 * @className: ServerBootStrap
 * @author: 茹某
 * @date: 2021/7/31 9:47
 **/
//启动一个服务提供者
public class ServerBootStrap
{
    public static void main(String[] args)
    {
        NettyServer.startServer("127.0.0.1",7000);
    }
}
