package com.dlut.jky.app1.reflection;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

/**
 * Created by jiangkunyou on 15/11/2.
 */
public class ReflectionTest {

    @Test
    public Class testGenericAndReflection(){

        Type type = getClass().getGenericSuperclass();
        if(type instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type [] types = parameterizedType.getActualTypeArguments();
            if(types != null && types.length > 0){
                if(!(types[0] instanceof Class)){
                    return (Class) types[0];
                }
            }
        }
        return null;
    }

    @Test
    public void testAnnotation() throws Exception{
        String className = "com.dlut.jky.app1.beans.Person";
        int value = 12;
        Class clazz = Class.forName(className);
        Method method = clazz.getDeclaredMethod("setAge", Integer.class);
        Annotation annotation = method.getAnnotation(AgeValidator.class);
        if (annotation != null) {
            if(annotation instanceof AgeValidator){
                AgeValidator ageValidator = (AgeValidator)annotation;
                if(value < ageValidator.min() || value > ageValidator.max()){
                    throw new RuntimeException("年龄范围不对！");
                }
            }
        }
        Object obj = clazz.newInstance();
        method.invoke(obj, value);
        Field field = clazz.getDeclaredField("age");
        field.setAccessible(true);
        System.out.println(field.get(obj));
    }

    /**
     * @param obj: 要执行方法的对象
     * @param methodName: 要执行的方法
     * @param args: 方法参数
     */
    public Object invoke(Object obj, String methodName, Object ... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class [] params = new Class[args.length];
        for(int i = 0; i < args.length; i++){
            params[i] = args[i].getClass();
        }
        // 获取当前类中该方法
        Method method = getMethod(obj.getClass(), methodName, params);
        // 若方法是私有方法 则要设置该属性
        testInvokePrivateMethod(method);
//        method.setAccessible(true);
        return method.invoke(obj, args);
    }

    public void testInvokePrivateMethod(Method method){
        int modifiers = method.getModifiers();
        if(Modifier.isPublic(modifiers))
            method.setAccessible(true);
        // 若方法是私有方法 则要设置该属性
    }

    /**
     * 获取方法，若当前类对象中没有该方法，则去父类继续找
     * @param clazz: 类对象
     * @param methodName: 方法名
     */
    public Method getMethod(Class clazz, String methodName, Class ... params){
        for(Class tempClazz = clazz; tempClazz != Object.class; tempClazz = tempClazz.getSuperclass()){
            Method method = null;
            try {
                method = tempClazz.getDeclaredMethod(methodName, params);
                return method;
            } catch (NoSuchMethodException e) {}
        }
        return null;
    }

    @Test
    public void testMethod() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Class clazz = Class.forName("com.dlut.jky.app1.beans.User");
//        System.out.println(clazz);
        ClassLoader classLoader = clazz.getClassLoader();
//        System.out.println(classLoader);
        // 该方法不能获取private方法
        Method [] methods = clazz.getMethods();
//        for(Method method: methods){
//            System.out.println(method.getName());
//        }
        // 获取所有方法 包括private 但是只获取当前类声明的方法
        Method [] methods2 = clazz.getDeclaredMethods();
//        for(Method method: methods2){
//            System.out.println(method.getName());
//        }

        // 获取指定方法，第一个参数是函数名，第二参数是函数的参数列表class对象
        Method method = clazz.getDeclaredMethod("setUserName", String.class);
        System.out.println(method);

        // 执行方法
        Object obj = clazz.newInstance();
//        method.invoke(obj, "abc");


    }

}
