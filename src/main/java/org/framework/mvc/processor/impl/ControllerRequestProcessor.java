package org.framework.mvc.processor.impl;

import org.framework.core.BeanContainer;
import org.framework.core.annotatior.Controller;
import org.framework.mvc.RequestProcessorChain;
import org.framework.mvc.annotation.RequestMapping;
import org.framework.mvc.annotation.RequestParam;
import org.framework.mvc.annotation.ResponseBody;
import org.framework.mvc.processor.RequestProcessor;
import org.framework.mvc.processor.render.JsonResultRender;
import org.framework.mvc.processor.render.ResourceNotFoundResultRender;
import org.framework.mvc.processor.render.ResultRender;
import org.framework.mvc.processor.render.ViewResultRender;
import org.framework.mvc.type.ControllerMethod;
import org.framework.mvc.type.RequestPathInfo;
import org.omg.PortableInterceptor.RequestInfo;

import javax.print.DocFlavor;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ControllerRequestProcessor implements RequestProcessor {
    private BeanContainer beanContainer;
    private Map<RequestPathInfo, ControllerMethod> pathControllerMethodMap =
            new ConcurrentHashMap<>();
    private Map<String, ControllerMethod> pathControllerMethodMap_s =
            new ConcurrentHashMap<>();

    public ControllerRequestProcessor(){
        this.beanContainer = BeanContainer.getInstance();
        Set<Class<?>> requestMappingSet =  beanContainer.getClassByAnnotation(RequestMapping.class);
        initPathControllerMethodMap(requestMappingSet);
    }

    private void initPathControllerMethodMap(Set<Class<?>> requestMappingSet) {
      if(requestMappingSet == null){
          return;
      }
      //遍历所有被requestMapping标记的类 获取类上面该注解的的属性值作为第一路径
        for(Class<?> requestMappingClass:requestMappingSet){
          RequestMapping requestMapping = requestMappingClass.getAnnotation(RequestMapping.class);
          String basePath = requestMapping.value();
          if(!basePath.startsWith("/")){
              basePath ="/"+basePath;
          }
            //遍历所有被requestMapping标记的方法 获取方法上面的注解的属性值作为二级路径
          Method[] methods = requestMappingClass.getDeclaredMethods();
          if(methods == null){
                continue;
          }
          for(Method method:methods){
              if(method.isAnnotationPresent(RequestMapping.class)){
                  RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
                  String methodPath = methodRequestMapping.value();
                  if(!methodPath.startsWith("/")){
                      methodPath ="/"+methodPath;
                  }
                  String url = basePath + methodPath;
                  //解析方法里被requestParam标记的参数
                  //获取该注解的属性值 作为参数名
                  //获取被标记的参数的数据类型 建立参数名和参数类型的映射

                  Map<String,Class<?>> methodParams = new HashMap<>();
                  String httpMethod = String.valueOf(methodRequestMapping.method());
                  RequestPathInfo requestPathInfo = new RequestPathInfo(httpMethod,url);
                  ControllerMethod controllerMethod = new ControllerMethod(requestMappingClass,method,methodParams);
                  this.pathControllerMethodMap.put(requestPathInfo,controllerMethod);
                  String key = requestPathInfo.getHttpPath()+"-"+requestPathInfo.getHttpMethod();
                  this.pathControllerMethodMap_s.put(key,controllerMethod);

                  Parameter[] parameters = method.getParameters();
                  /*if(parameters == null){
                      String httpMethod = String.valueOf(methodRequestMapping.method());
                      RequestPathInfo requestPathInfo = new RequestPathInfo(httpMethod,url);
                      ControllerMethod controllerMethod = new ControllerMethod(requestMappingClass,method,methodParams);
                      this.pathControllerMethodMap.put(requestPathInfo,controllerMethod);
                      continue;
                  }else {
                      for(Parameter parameter:parameters){
                          RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                          if(requestParam == null){
                              continue;
                              //throw new RuntimeException("requestParam ERROR");
                          }
                          methodParams.put(requestParam.value(),parameter.getType());
                          //将获取到的信息封装成RequestPathInfo实例和ControllerMethod实例 放到映射表里
                          String httpMethod = String.valueOf(methodRequestMapping.method());
                          RequestPathInfo requestPathInfo = new RequestPathInfo(httpMethod,url);
                          ControllerMethod controllerMethod = new ControllerMethod(requestMappingClass,method,methodParams);
                          this.pathControllerMethodMap.put(requestPathInfo,controllerMethod);
                      }
                  }*/

              }
          }
        }






    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        //解析httpServletRequest的请求方法 请求路径 获取对应的controllerMethod实例
        String method = requestProcessorChain.getRequestMethod();
        String path = requestProcessorChain.getRequestPath();
        String key = path+"-"+method;
        ControllerMethod controllerMethod = this.pathControllerMethodMap_s.get(key);

       // ControllerMethod controllerMethod = this.pathControllerMethodMap.get(new RequestPathInfo(method,path));
        if(controllerMethod == null){
            throw new RuntimeException("404");
            //requestProcessorChain.setResultRender(new ResourceNotFoundResultRender());
        }
        //解析请求参数并传递给获取到的ControllerMethod实例去执行
        Object result = invokeControllerMethod(controllerMethod,requestProcessorChain.getRequest());
        //根据解析结果 进行render渲染
        setResultRender(result,controllerMethod,requestProcessorChain);

        return true;
    }

    private void setResultRender(Object result, ControllerMethod controllerMethod, RequestProcessorChain requestProcessorChain) {
      if(result == null){
          return;
      }
      ResultRender resultRender;
      boolean isJson = controllerMethod.getInvokeMethod().isAnnotationPresent(ResponseBody.class);
      if(isJson){
          resultRender = new JsonResultRender(result);
      }else {
          resultRender = new ViewResultRender(result);
      }
      requestProcessorChain.setResultRender(resultRender);
    }

    private Object invokeControllerMethod(ControllerMethod controllerMethod, HttpServletRequest request) {
           //从请求里获取GET 或者post的参数名及其对应的值
           Map<String ,String> requestParamMap = new HashMap<>();
           Map<String, String[]> parameterMap = request.getParameterMap();
           for(Map.Entry<String, String[]> parameter:parameterMap.entrySet()){
               if(parameter.getValue() != null){
                   requestParamMap.put(parameter.getKey(),parameter.getValue()[0]);

               }
           }
           //根据获取的请求参数名及其对应的值 以及controllerMethod里面的参数映射关系 去实例化 方法对应的参数
           List<Object> methodParams = new ArrayList<>();
           Map<String, Class<?>> methodParamsMap = controllerMethod.getMethodParams();
           for(String parName:methodParamsMap.keySet()){
               Class<?> type = methodParamsMap.get(parName);
               String requestValue = requestParamMap.get(parName);
               Object value = null;
               if(requestValue == null){

               }else {

               }
               methodParams.add(value);

           }
           // 反射执行controller方法
         Object controllerClass = beanContainer.getBean(controllerMethod.getControllerClass());
         Method invokeMethod = controllerMethod.getInvokeMethod();
         invokeMethod.setAccessible(true);
         Object result = null;
         try {
             if(methodParams.size() == 0){
                 result = invokeMethod.invoke(controllerClass);
             }else {
                 result = invokeMethod.invoke(controllerClass,methodParams.toArray());

             }
         }catch (Exception e){
             throw new RuntimeException("INVOKE ERROR");
         }
        return result;

    }
}
