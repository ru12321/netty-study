package com.mrru.netty.codec2;

import com.mrru.netty.codec.StudentPOJO;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @className: NettyServerHandler
 * @author: 茹某
 * @date: 2021/7/26 17:24
 **/
/*
说明
1 自定义一个Handler 需要继承netty规定好的某个HandlerAdapter
2 自定义一个Handler 才能称为一个handler
 */
//public class NettyServerHandler extends ChannelInboundHandlerAdapter
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage>

{
    //读取数据的事件 可以读取客户端发送的消息
    /*
    特别重要的几点
    1 ChannelHandlerContext ctx:上下文对象，含有很多东西，管道pipeline，通道channel，连接的地址
    2 msg 就是客户端发送的数据，默认是以Object对象传输的

    当有数据读取时，channelRead就会触发
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception
    {
        //根据datatype来显示不同的信息
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType){
            //枚举类的用处！  判断类型是枚举类型中的StudentType，就按Student处理，传Student类型的数据
            MyDataInfo.Student student = msg.getStudent();
            System.out.println("学生名字 "+ student.getName() + "学生ID " + student.getId());
        }else if(dataType == MyDataInfo.MyMessage.DataType.WorkerType){
            MyDataInfo.Worker worker = msg.getWorker();
            System.out.println("工人名字 "+ worker.getName() + "工人年龄 " + worker.getAge());
        }else {
            System.out.println("传输类型不正确 ");
        }
    }

    //数据读取完毕，回消息
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        //writeAndFlush  是 write + flush
        //数据写到缓冲区，并且刷新（打到通道中）
        //一般讲，要对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~瞄1",CharsetUtil.UTF_8));
    }

    //处理异常的  一般需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        ctx.close();
    }
}
