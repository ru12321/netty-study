package com.mrru.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @className: ProxyFactory
 * @author: 茹某
 * @date: 2021/7/30 21:53
 **/

/*
getProxyInstance():
    1.根据传入的目标对象知道代理谁
    2.利用反射机制，返回一个代理对象
    3.然后通过代理对象，调用目标对象方法
 */

public class ProxyFactory
{
    //维护一个目标对象 Object
    private Object target;

    //构造器  传入目标对象
    public ProxyFactory(Object target){
        this.target = target;
    }

    //给目标对象 生成一个代理对象
    public Object getProxyInstance(){

        /**
         *     public static Object newProxyInstance(ClassLoader loader,
         *                                           Class<?>[] interfaces,
         *                                           InvocationHandler h)
         *    1.ClassLoader loader,指定当前目标对象所使用的类加载器，获取加载器的方法
         *    2.Class<?>[] interfaces,目标对象实现的接口类型，使用泛型方法确认类型  被代理类的所有接口信息; 便于生成的代理类可以具有代理类接口中的所有方法
         *    3.InvocationHandler h,事件处理，执行目标对象的方法时，会触发事件处理器的方法；
         *                      会把当前执行的目标对象方法作为参数传入
         *
         * InvocationHandler是由代理实例的调用处理程序实现的接口。
         * 每个代理实例都有一个关联的调用处理程序。在代理实例上调用方法时，方法调用将被编码并发送到其调用处理程序的invoke方法。
         */
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler()
        {
            //只有代理实例调用方法时，才会触发invoke方法
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
            {

                System.out.println("JDK代理开始...");
                //反射机制调用目标对象的方法
                Object returnVal = method.invoke(target, args);
                System.out.println("JDK代理结束....");

                return returnVal;
            }
        });

    }




}
