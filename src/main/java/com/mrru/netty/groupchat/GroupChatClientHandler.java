package com.mrru.netty.groupchat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @className: GroupChatClientHandler
 * @author: 茹某
 * @date: 2021/7/27 21:29
 **/
public class GroupChatClientHandler extends SimpleChannelInboundHandler<String>
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception
    {
        System.out.println(msg.trim());

    }
}
