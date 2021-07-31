package com.mrru.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @className: MyServerHandler
 * @author: 茹某
 * @date: 2021/7/28 9:04
 **/
public class MyServerHandler extends ChannelInboundHandlerAdapter
{


    /***
     *
     * @param ctx 上下文
     * @param evt 空闲检测事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
        if (evt instanceof IdleStateEvent){
            //将evt向下转型
            IdleStateEvent event = (IdleStateEvent) evt;

            String eventType = null;
            switch (event.state()){
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + " 超时事件 "+eventType);
            //服务器做相应处理
            System.out.println("服务器做相应处理....");
        }
    }



}
