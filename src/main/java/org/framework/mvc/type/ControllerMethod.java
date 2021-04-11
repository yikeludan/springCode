package org.framework.mvc.type;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 待执行的controller 及其方法实例和参数映射
 */
public class ControllerMethod {
    private Class<?> controllerClass;
    private Method invokeMethod;
    private Map<String,Class<?>> methodParams;

    public ControllerMethod(){

    }

    public ControllerMethod(Class<?> controllerClass,Method invokeMethod,
                            Map<String,Class<?>> methodParams){
        this.controllerClass = controllerClass;
        this.invokeMethod = invokeMethod;
        this.methodParams = methodParams;

    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public Method getInvokeMethod() {
        return invokeMethod;
    }

    public void setInvokeMethod(Method invokeMethod) {
        this.invokeMethod = invokeMethod;
    }

    public Map<String, Class<?>> getMethodParams() {
        return methodParams;
    }

    public void setMethodParams(Map<String, Class<?>> methodParams) {
        this.methodParams = methodParams;
    }
}
