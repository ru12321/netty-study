package com.mrru.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @className: NIOServer
 * @author: 茹某
 * @date: 2021/7/25 16:23
 **/
public class NIOServer
{
    public static void main(String[] args) throws IOException
    {
        //创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到selector对象
        Selector selector = Selector.open();

        //绑定端口  在服务端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //serverSocketChannel 注册到 selector 这个serverSocketChannel只关心事件连接！ OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("注册后的selectionkey数量是："+selector.keys().size());

        //循环等待客户端的连接
        while (true){

            //等待1秒，没有事件发生，就继续 下次等待
            if (selector.select(1000)==0){//没有事件发生
                System.out.println("服务器等待了1秒 无连接");
                continue;
            }

            //如果有事件发生，就获取到这些有事件发生的selectionKey集合，
            //1 如果返回的大于0，表示已经获取到了 关注的事件
            //2 selector.selectedKeys() 返回关注事件的集合
            //通过selectedKeys()  反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("这次集合中有事件的selectionkey数量是："+selectionKeys.size());
            //遍历集合 selectionKeys，迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()){
                //获取到每一个selectionkey
                SelectionKey key = keyIterator.next();

                //根据key对应的通道发生的事件做相应处理
                //每个客户端最开始 都要走这里！！！
                // 为每个客户端创建Channel，然后将这个Channel注册到selector，并且关联一个buffer
                if (key.isAcceptable()){ //有新的客户端连接
                    //给新的客户端生成 socketChannel   这个方法会立即执行，因为已经知道是连接
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    System.out.println("客户端连接成功 生成了一个socketChannel "+socketChannel.hashCode());

                    //将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);

                    //将当前的socketChannel也注册到selector  关注事件为OP_READ  关联buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("客户端连接后，注册后的selectionkey数量是："+selector.keys().size());
                }

                //之后发生的事件，客户端们都创建好了socketChannel，
                // 并且将自己Channel的关注事件也已经注册到了selector
                if (key.isReadable()){
                    //通过key 反向获取到对应Channel 然后执行业务事件
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();

                    channel.read(buffer);
                    System.out.println("from 客户端 "+ new String(buffer.array()));
                }

                //处理完每个事件，都要手动从集合中移除当前的selectionkey，多线程 防止重复操作
                //不移除的话
                keyIterator.remove();
            }


        }

    }



}
