package com.mrru.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @className: GroupChatServer
 * @author: 茹某
 * @date: 2021/7/25 20:23
 **/
public class GroupChatServer
{

    //设置属性
    private ServerSocketChannel listenChannel;
    private Selector selector;
    private static final int PORT =  6667;

    //构造器
    public  GroupChatServer(){
        try
        {
            //获得serverSocketChannel
            listenChannel = ServerSocketChannel.open();
            //获得选择器
            selector = Selector.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //服务端监听方法
    public void listen(){
        try
        {
            while (true){
                int count = selector.select();
                if (count ==0 ){
                    System.out.println("客户端未连接，请等待");;
                }else {
                    //取出selectorKeys集合
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();

                    while (iterator.hasNext()){
                        //遍历取出每个key
                        SelectionKey key = iterator.next();

                        //如果是连接事件
                        if (key.isAcceptable()){
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            System.out.println("客户端: "+socketChannel.getRemoteAddress()+" 上线");
                        }
                        //如果是读事件
                        if (key.isReadable()){
                            readData(key);
                        }
                        //移除key
                        iterator.remove();
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //服务端读取客户端发送的消息
    private void readData(SelectionKey key){
        SocketChannel channel =null;
        try
        {
            //获得Channel
            channel = (SocketChannel) key.channel();
            //创建Buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //读消息
            int read = channel.read(buffer);
            if (read>0){
                //缓冲区消息转换为字符串
                String msg = new String(buffer.array());
                //输出该消息
                System.out.println("收到客户端"+channel.getRemoteAddress()+"的消息："+msg);
                //向其他客户端转发消息
                sendInfoToOtherClients(msg,channel);
            }
        }catch (IOException e){
            try
            {
                System.out.println(channel.getRemoteAddress()+"离线了");
                //取消注册 关闭通道
                key.cancel();
                channel.close();

            } catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
    }

    //转发消息给其他客户(通道)
    private void  sendInfoToOtherClients(String msg,SocketChannel selfChannel) throws IOException{
        System.out.println("服务器转发消息中-------------------------------------");
        //遍历 所有注册到selector上的SocketChannel 并排除自己
        for (SelectionKey key : selector.keys()){
            //拿到所有socketChannel
            Channel targetChannel = key.channel();

            if (targetChannel != selfChannel && targetChannel instanceof SocketChannel){
                SocketChannel dest = (SocketChannel) targetChannel;

                //将msg存入buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //往目标通道转发消息
                dest.write(buffer);
            }
        }
        System.out.println("服务器转发消息完成-------------------------------------");

    }



    public static void main(String[] args)
    {
        //创建服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();

    }
}
