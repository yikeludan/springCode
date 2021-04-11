package org.framework.aop;

import org.framework.aop.annotation.Aspect;
import org.framework.aop.annotation.Order;
import org.framework.aop.aspect.AspectInfo;
import org.framework.aop.aspect.DefaultAspect;
import org.framework.core.BeanContainer;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AspectWeaver {
    private BeanContainer beanContainer;
    public AspectWeaver(){
        beanContainer = BeanContainer.getInstance();
    }
    public void doAop(){
        //获取所有的被aspect标记的切面类
        Set<Class<?>> classSet =  beanContainer.getClassByAnnotation(Aspect.class);
        //将切面类按照不同的织入目标进行切分
        Map<Class<? extends Annotation>, List<AspectInfo>> categoryMap =  new HashMap<>();
        if(classSet == null){
            return;
        }
        for(Class<?> aspectClass:classSet){
            if(verifyAspectClass(aspectClass)){
                categoryAspect(categoryMap,aspectClass);
            }else {
                throw new RuntimeException("aspect error");
            }
        }

        //按照不同的织入目标分别去按序织入Aspect的逻辑
        if(categoryMap == null){
            return;
        }
        for(Class<? extends Annotation> category:categoryMap.keySet()){
            weaveByCategory(category,categoryMap.get(category));
        }

    }

    private void weaveByCategory(Class<? extends Annotation> category, List<AspectInfo> aspectInfos) {
        //获取被代理类的集合
        Set<Class<?>> classSet = beanContainer.getClassByAnnotation(category);
        if(classSet == null){
            return;
        }

        // 遍历被代理类 分别为每个代理类生成被代理对象
        for(Class<?> targetClass:classSet){
            AspectListExcutor aspectListExcutor = new AspectListExcutor(targetClass,aspectInfos);
            Object proxyBean =  ProxyCreator.createProxy(targetClass,aspectListExcutor);
            //将动态代理对象实例添加到容器里取代未代理的对象
            beanContainer.addBean(targetClass,proxyBean);
        }
    }

    private void categoryAspect(Map<Class<? extends Annotation>, List<AspectInfo>> categoryMap, Class<?> aspectClass) {
          Order order = aspectClass.getAnnotation(Order.class);
          Aspect aspect = aspectClass.getAnnotation(Aspect.class);
          DefaultAspect defaultAspect = (DefaultAspect) beanContainer.getBean(aspectClass);
          AspectInfo aspectInfo = new AspectInfo();
          aspectInfo.setOrderIndex(order.value());
          aspectInfo.setDefaultAspect(defaultAspect);
          if(!categoryMap.containsKey(aspect.value())){
              List<AspectInfo> list = new ArrayList<>();
              list.add(aspectInfo);
              categoryMap.put(aspect.value(),list);
          }else {
              List<AspectInfo> list = categoryMap.get(aspect.value());
              list.add(aspectInfo);
              categoryMap.put(aspect.value(),list);
          }
    }


    //验证一定要遵守Aspcet类添加@Aspect 和@Order 标签的规范 同时必须继承DefaultAspect.class
    //此外aspect 的属性值不能是aspect 本身
    private boolean verifyAspectClass(Class<?> aspectClass) {
        return aspectClass.isAnnotationPresent(Aspect.class)&&
                aspectClass.isAnnotationPresent(Order.class)&&
                DefaultAspect.class.isAssignableFrom(aspectClass)&&
                aspectClass.getAnnotation(Aspect.class).value()!= Aspect.class;

    }
}
