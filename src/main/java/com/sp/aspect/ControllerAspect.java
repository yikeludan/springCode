package com.sp.aspect;

import org.framework.aop.annotation.Aspect;
import org.framework.aop.annotation.Order;
import org.framework.aop.aspect.DefaultAspect;
import org.framework.core.annotatior.Controller;
import org.framework.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

@Aspect(value = Controller.class)
@Order(0)
public class ControllerAspect extends DefaultAspect {
    public  final  static Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    private long startTime = 0L;

    @Override
    public void before(Class<?> targetClass, Method method, Object[] args) throws Throwable {
        logger.info("执行之前");
        startTime = System.currentTimeMillis();
    }

    @Override
    public Object afterReturing(Class<?> targetClass, Method method, Object[] args, Object returnValue) throws Throwable {
        long endTime = System.currentTimeMillis();
        long dur = startTime -endTime;
        logger.info("执行之后");

        return returnValue;
    }
}
