package com.mrru.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @className: GroupChatClient
 * @author: 茹某
 * @date: 2021/7/25 21:14
 **/
public class GroupChatClient
{
    //设置属性
    private SocketChannel socketChannel;
    private Selector selector;
    private String username;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6667;

    public GroupChatClient() throws IOException
    {
        selector = Selector.open();
        //socketChannel 连接服务器
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST,PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //注册
        socketChannel.register(selector, SelectionKey.OP_READ);
        //初始化username
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + "is ok...");
    }

    //向服务器发送消息   默认已经使用通道连接了
    public void sendInfo(String info){
        info = username +" 说： "+info;
        try
        {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //读取从服务器端回复的消息
    public void readInfo(){
        try
        {
            int read = selector.select();
            if (read>0){//有事件
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    //拿到key 和 Channel
                    SelectionKey key = iterator.next();
                    if (key.isReadable()){
                        SocketChannel channel = (SocketChannel) key.channel();
                        //创建buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        //将通道中 服务端转发的消息 读出来
                        channel.read(buffer);
                        //反转
                        buffer.flip();
                        //在客户端输出
                        System.out.println("客户端消息: "+new String(buffer.array()));
                    }
                    //不要忘了这个操作！！！
                    /*
                    注：只是从selectedKeys集合中移除了，并没有从Selector中移除，
                    selector不会自己删除selectedKeys()集合中的selectionKey，
                    那么如果不人工remove()，将导致下次select()的时候selectedKeys()中
                    仍有上次轮询留下来的信息，这样必然会出现错误，假设这次轮询时该通道
                    并没有准备好，却又由于上次轮询未被remove()的原因被认为已经准备好了，这样能不出错吗？
                     */
                    iterator.remove();
                }
            }else{
                System.out.println("没有可用的通道");
            }

        }catch (IOException e ){
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws IOException
    {
        //启动客户端
        GroupChatClient chatClient = new GroupChatClient();

        //启动线程
        new Thread(){
            public void run(){
                while (true){
                    chatClient.readInfo();
                    try
                    {
                        Thread.sleep(3000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        //发送数据
        Scanner scanner = new Scanner(System.in);
        //一直等待客户端的发送
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }


    }
}
