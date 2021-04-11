package org.framework.mvc.type;

/**
 * http 请求路径和方法
 */
public class RequestPathInfo {
    private String httpMethod;
    private String httpPath;

    public RequestPathInfo(){

    }
    public RequestPathInfo(String httpMethod,String httpPath){
        this.httpMethod = httpMethod;
        this.httpPath = httpPath;
    }
    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getHttpPath() {
        return httpPath;
    }

    public void setHttpPath(String httpPath) {
        this.httpPath = httpPath;
    }
}
