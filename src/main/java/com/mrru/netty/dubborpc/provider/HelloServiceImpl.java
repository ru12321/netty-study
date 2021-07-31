package com.mrru.netty.dubborpc.provider;

import com.mrru.netty.dubborpc.common.HelloService;

/**
 * @className: HelloServiceImpl
 * @author: 茹某
 * @date: 2021/7/31 9:46
 **/
public class HelloServiceImpl implements HelloService
{
    private  static  int count= 0;

    @Override
    public String hello(String msg)
    {
        System.out.println("HelloServiceImpl 收到了消息 "+msg);

        if (msg!=null){
            return "HelloServiceImpl，你好，服务消息者，我收到了你的消息: 【"+ msg+"】第"+(++count)+"次";
        }else {
            return "HelloServiceImpl，你好，服务消息者，我收到了你的消息";
        }
    }
}
