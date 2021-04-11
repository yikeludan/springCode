package org.framework.aop.aspect;

import java.lang.reflect.Method;

public abstract class DefaultAspect {
    public void before(Class<?> targetClass, Method method,Object[] args)throws Throwable{

    }

    public Object afterReturing(Class<?> targetClass, Method method,
                                Object[] args,Object returnValue)throws Throwable{
        return returnValue;

    }
    public void afterThrowIng(Class<?> targetClass, Method method,
                                Object[] args,Throwable e)throws Throwable{

    }
}
