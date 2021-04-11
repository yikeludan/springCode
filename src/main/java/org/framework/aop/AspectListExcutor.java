package org.framework.aop;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.framework.aop.aspect.AspectInfo;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AspectListExcutor implements MethodInterceptor {
    //被代理的类
    private Class<?> targetClass;

    private List<AspectInfo> aspectInfoList;

    public AspectListExcutor(){

    }

    public AspectListExcutor(Class<?> targetClass,List<AspectInfo> aspectInfoList){
        this.targetClass = targetClass;
        this.aspectInfoList = sortList(aspectInfoList);
    }

    /**
     * 根据order 值升序 确保order值小的aspect 先被织入
     * @param aspectInfoList
     * @return
     */
    private List<AspectInfo> sortList(List<AspectInfo> aspectInfoList) {
        Collections.sort(aspectInfoList, new Comparator<AspectInfo>() {
            @Override
            public int compare(AspectInfo o1, AspectInfo o2) {
                return o1.getOrderIndex() - o2.getOrderIndex();
            }
        });
        return aspectInfoList;
    }


    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object retrunValue = null;
        if(this.aspectInfoList == null){
            return retrunValue;
        }
        //按照order的顺序升序执行完 aspect里before的逻辑
        invokeBeforeAdvices(method,args);
        try{
            //执行被代理类方法
            retrunValue = methodProxy.invokeSuper(o,args);
            // 如果被代理类正常返回 按照order的顺序降序执行完 aspect里afterReturing的逻辑
            retrunValue = invokeAfterReturingAdvices(method,args,retrunValue);

        }catch (Exception e){
            // 如果被代理类抛出异常 按照order的顺序降序执行完 aspect里afterThrowing的逻辑
            invokeAfterThrowingAdvices(method,args,e);

        }


        return retrunValue;
    }
    private void invokeBeforeAdvices(Method method, Object[] args) throws Throwable {
          for(AspectInfo aspectInfo:this.aspectInfoList){
              aspectInfo.getDefaultAspect().before(this.targetClass,method,args);
          }
    }


    private Object invokeAfterReturingAdvices(Method method, Object[] args, Object retrunValue) throws Throwable {
        Object res = null;
        for(int i = this.aspectInfoList.size()-1;i>=0;i--){
            res =  this.aspectInfoList.get(i).getDefaultAspect().afterReturing
                    (this.targetClass,method,args,retrunValue);
        }
        return res;
    }

    private void invokeAfterThrowingAdvices(Method method, Object[] args, Exception e) throws Throwable {
        for(int i = this.aspectInfoList.size()-1;i>=0;i--){
            this.aspectInfoList.get(i).getDefaultAspect().afterThrowIng
                    (this.targetClass,method,args,e);
        }
    }
}
