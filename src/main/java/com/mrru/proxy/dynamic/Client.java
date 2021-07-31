package com.mrru.proxy.dynamic;

/**
 * @className: Client
 * @author: 茹某
 * @date: 2021/7/30 22:01
 **/
public class Client
{

    public static void main(String[] args)
    {
        TeacherDao teacherDao = new TeacherDao();

        //因为传进去的是teacherDao  实现了ITeacherDao接口 ，
        // 必须类型是ITeacherDao接口类型，不然调用不了目标对象方法
        ITeacherDao proxyInstance = (ITeacherDao) new ProxyFactory(teacherDao).getProxyInstance();

//        proxyInstance.teach();
//        System.out.println("proxyInstance= " +proxyInstance);//proxyInstance= com.mrru.proxy.dynamic.TeacherDao@1d44bcfa
//
          //返回的是$Proxy0 带$的就是代理对象，在内存中动态生成了代理对象
//        System.out.println("proxyInstance= " +proxyInstance.getClass());//proxyInstance= class com.sun.proxy.$Proxy0

        //只有代理实例调用方法时，才会触发invoke方法
        String msg = proxyInstance.sayHello("小王aaa");

        System.out.println(msg);


    }
}
