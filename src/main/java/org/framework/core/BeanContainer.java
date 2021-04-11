package org.framework.core;

import com.sun.xml.internal.ws.server.sei.SEIInvokerTube;
import org.framework.aop.annotation.Aspect;
import org.framework.aop.annotation.Order;
import org.framework.core.annotatior.Commponet;
import org.framework.core.annotatior.Controller;
import org.framework.core.annotatior.Repository;
import org.framework.core.annotatior.Service;
import org.framework.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程安全 能抵御反射和序列化的单例
 *
 */



public class BeanContainer {
    private final Map<Class<?>,Object> beanMap = new ConcurrentHashMap<>();

    //加载 bean 的注解列表
    private  static final List<Class<? extends Annotation>> BEAN_ANNOTATIONS =
            Arrays.asList(Commponet.class, Controller.class,
                    Repository.class,
                    Service.class, Aspect.class);

    private BeanContainer(){}
    public static BeanContainer getInstance(){
        return ContainerHolder.HOLDER.instance;
    }
    private enum ContainerHolder{
        HOLDER;
        private BeanContainer instance;
        ContainerHolder(){
            instance = new BeanContainer();
        }
    }


    private  boolean aBooleanoad  = false;

    public boolean isLoad(){
        return aBooleanoad;
    }
    /**
     * 扫描所有bean
     * @param packageName
     */
    public synchronized void  loadBean(String packageName){
        //判断该容器是否被加载过
        if(isLoad()){
            return;
        }
        Set<Class<?>> classSet = ClassUtil.extractPackageClass(packageName);
        if (classSet == null||classSet.isEmpty()){
            throw new RuntimeException("classSet NULL");
        }
        for (Class<?> oneClass:classSet){
            for(Class<? extends Annotation> annotation:BEAN_ANNOTATIONS){
                //如果类上标记了自定义注解
                if(oneClass.isAnnotationPresent(annotation)){
                    //将目标类本身作为键 目标类的实例作为值 放入 beanMap里
                    beanMap.put(oneClass,ClassUtil.newInstance(oneClass,true));
                }
            }
        }
        aBooleanoad = true;
    }


   public Object addBean(Class<?> clazz,Object bean){
       return beanMap.put(clazz,bean);
   }
   public Object removeBean(Class<?> clazz){
        return beanMap.remove(clazz);
   }
   public Object getBean(Class<?> clazz){
        return beanMap.get(clazz);
   }

    /**
     * 获取所有的class
     * @return
     */
   public Set<Class<?>>  getClassList(){
        return beanMap.keySet();
   }

    /**
     * 获取所有的对象实例
     * @return
     */
   public Set<Object>  getBeans(){
        return new HashSet<>(beanMap.values());
   }

    /**
     * 根据注解获取class对象
     * @param annotation
     * @return
     */
   public Set<Class<?>>getClassByAnnotation(Class<? extends Annotation> annotation){
       Set<Class<?>> keys = getClassList();
       if(keys.isEmpty()){
           return null;
       }
       Set<Class<?>> classSet = new HashSet<>();
       for(Class<?> c:keys){
           if(c.isAnnotationPresent(annotation)){
               classSet.add(c);
           }
       }
       return classSet.size()>0?classSet:null;
   }


    /**
     * 根据接口或者父类获取class对象
     * @param interfaceOrClass
     * @return
     */
    public Set<Class<?>>getClassBySuper(Class<?> interfaceOrClass){
        Set<Class<?>> keys = getClassList();
        if(keys.isEmpty()){
            return null;
        }
        Set<Class<?>> classSet = new HashSet<>();
        for(Class<?> c:keys){
            if(interfaceOrClass.isAssignableFrom(c)
                    &&!c.equals(interfaceOrClass)){
                classSet.add(c);
            }
        }
        return classSet.size()>0?classSet:null;
    }
}
