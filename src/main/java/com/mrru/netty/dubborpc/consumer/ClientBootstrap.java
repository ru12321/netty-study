package com.mrru.netty.dubborpc.consumer;

import com.mrru.netty.dubborpc.common.HelloService;
import com.mrru.netty.dubborpc.netty.NettyClient;

/**
 * @className: ClientBootstrap
 * @author: 茹某
 * @date: 2021/7/31 11:47
 **/
public class ClientBootstrap
{
    public static final String protocolName = "HelloService#hello#";

    public static void main(String[] args)
    {
        //创建一个服务消费者
        NettyClient consumer = new NettyClient();

        //创建代理对象
        HelloService service = (HelloService) consumer.getBean(HelloService.class,protocolName);


        for(;;){
            try
            {
                Thread.sleep(2* 1000);
                //通过代理对象调用服务提供者的方法
                String res = service.hello("你好 dubbo~ ");
                System.out.println("ClientBootstrap 调用的结果 res= "+ res);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }


}
