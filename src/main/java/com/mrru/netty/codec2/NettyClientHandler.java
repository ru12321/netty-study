package com.mrru.netty.codec2;

import com.mrru.netty.codec.StudentPOJO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

/**
 * @className: NettyClientHandler
 * @author: 茹某
 * @date: 2021/7/26 19:13
 **/
public class NettyClientHandler extends ChannelInboundHandlerAdapter
{

    //当通道就绪时就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
       //随机的发送Student对象 或者 Worker 到服务器
        int random = new Random().nextInt(3);
        MyDataInfo.MyMessage myMessage = null;

        if (random ==0){
            myMessage = MyDataInfo.MyMessage.newBuilder().
                    setDataType(MyDataInfo.MyMessage.DataType.StudentType).setStudent(MyDataInfo.Student.
                    newBuilder().setId(5).setName("李蕾").build()).build();
        }else {
            myMessage = MyDataInfo.MyMessage.newBuilder().
                    setDataType(MyDataInfo.MyMessage.DataType.WorkerType).setWorker(MyDataInfo.Worker.
                    newBuilder().setAge(60).setName("老李").build()).build();
        }

        ctx.writeAndFlush(myMessage);

    }

    //当通道有读取事件时，会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息是："+buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址： "+ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        ctx.close();
    }


}
