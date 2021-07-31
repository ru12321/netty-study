package com.mrru.netty.inboundandoutbound;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @className: MyClientHandler
 * @author: 茹某
 * @date: 2021/7/28 21:13
 **/
public class MyClientHandler extends SimpleChannelInboundHandler<Long>
{


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception
    {
        System.out.println("服务器 ip= "+ctx.channel().remoteAddress());
        System.out.println("收到服务器消息= "+ msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("MyClientHandler 发送数据");
//        ctx.writeAndFlush(123456L);

        //1.这里是16字节的话 服务端 会调用两次decode
        //2.该处理器的前一个handler 是 MyLongToByteEncoder
        //3.MyLongToByteEncoder 父类 MessageToByteEncoder
        /*
        if (acceptOutboundMessage(msg)) { //如果msg类型是不是 应该处理的类型，是就处理，不是就跳过encode
                @SuppressWarnings("unchecked")
                I cast = (I) msg;
                buf = allocateBuffer(ctx, cast, preferDirect);
                try {
                    encode(ctx, cast, buf);
                } finally {
                    ReferenceCountUtil.release(cast);
                }

                if (buf.isReadable()) {
                    ctx.write(buf, promise);
                } else {
                    buf.release();
                    ctx.write(Unpooled.EMPTY_BUFFER, promise);
                }
                buf = null;
            } else {
                ctx.write(msg, promise);
            }
         */
        //4.因此我们编写 encoder时 要注意传入的数据类型和处理的数据类型一致
//        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdefghooooiiii",CharsetUtil.UTF_8));
        ctx.writeAndFlush(123465L);
    }



}
