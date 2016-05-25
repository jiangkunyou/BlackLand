package com.dlut.jky.app1.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用注解 加在setAge(int age)方法上面，可以限制年龄的大小
 * @AgeValidator
 * void setAge(int age){}
 * Created by jiangkunyou on 15/11/2.
 */
@Retention(RetentionPolicy.RUNTIME) //在运行时执行
@Target(value = ElementType.METHOD) // 可以加在方法上
public @interface AgeValidator {
    public int min();
    public int max();
}
