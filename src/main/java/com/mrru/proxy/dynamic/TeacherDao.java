package com.mrru.proxy.dynamic;

/**
 * @className: TeacherDao
 * @author: 茹某
 * @date: 2021/7/30 21:52
 **/
public class TeacherDao implements ITeacherDao
{
    @Override
    public void teach()
    {
        System.out.println("老师授课中....");
    }

    @Override
    public String sayHello(String name)
    {
        return "name= "+ name;
    }
}
