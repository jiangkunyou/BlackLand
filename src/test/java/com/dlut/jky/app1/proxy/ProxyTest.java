package com.dlut.jky.app1.proxy;

import com.dlut.jky.app1.beans.Person;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by jiangkunyou on 15/11/3.
 */
public class ProxyTest {
    @Test
    public void testProxy(){
        final Person person = new Person();
        // 只能代理接口 不能是类  这里是举例其实不好使，因为person不是接口
        Person proxy = (Person) Proxy.newProxyInstance(person.getClass().getClassLoader(), new Class[]{Person.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("invoke...");
                return null;
            }
        });
        proxy.setAge(19);

    }
}
