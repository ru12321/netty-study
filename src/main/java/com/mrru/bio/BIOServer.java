package com.mrru.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @className: BIOServer
 * @author: 茹某
 * @date: 2021/7/24 20:54
 **/
public class BIOServer
{
    public static void main(String[] args) throws IOException
    {
        //思路
        //1 创建一个线程池
        //2 如果有客户端连接 就创建一个线程 与之通讯
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        //创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");


        while (true){
            //一直监听 客户端连接   阻塞式方法，有连接就往下执行，没有就在这里一直等待
            System.out.println("连接中....");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");

            //有连接就创建一个线程 与之通讯
            cachedThreadPool.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    //读取客户端的发送内容
                    handler(socket);
                }
            });

        }
    }

    public static void handler(Socket socket){
        try
        {
            System.out.println("线程id= "+Thread.currentThread().getId()+"名字= "+Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            //通过socket获得输入流
            InputStream inputStream = socket.getInputStream();

            //一直输出读到的东西
            while (true){
                //这个read也是阻塞 没有就在这一直等待
                System.out.println("read中....");
                int read = inputStream.read(bytes);
                System.out.println("read 读到数据了....");
                if (read!=-1){
                    System.out.println(new String(bytes,0,read));
                }else {
                    break;
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }finally
        {
            System.out.println("关闭和client的连接");
            try
            {
                socket.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }


    }







}
