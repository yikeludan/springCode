package org.framework.mvc;

import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;
import org.framework.mvc.processor.RequestProcessor;
import org.framework.mvc.processor.render.DefaultResultRender;
import org.framework.mvc.processor.render.InternalErrorResultRender;
import org.framework.mvc.processor.render.ResultRender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

/**
 * 以责任链的形式 执行注册请求器
 * 委派给特定的render实例对处理后的结果进行渲染
 */
public class  RequestProcessorChain {
    //请求处理迭代器
    private Iterator<RequestProcessor> iterator;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private String requestMethod;
    //请求路径
    private String requestPath;

    private int responseCode;

    //请求结果渲染器
    private ResultRender resultRender;

    public RequestProcessorChain(Iterator<RequestProcessor> iterator,
                                 HttpServletRequest req,
                                 HttpServletResponse resp) {
        this.iterator = iterator;
        this.request = req;
        this.response = resp;
        this.requestMethod = req.getMethod();
        this.requestPath = req.getPathInfo();
        this.responseCode = HttpServletResponse.SC_OK;
    }

    public Iterator<RequestProcessor> getIterator() {
        return iterator;
    }

    public void setIterator(Iterator<RequestProcessor> iterator) {
        this.iterator = iterator;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public ResultRender getResultRender() {
        return resultRender;
    }

    public void setResultRender(ResultRender resultRender) {
        this.resultRender = resultRender;
    }

    /**
     * 已责任链的模式按序执行请求链
     */
    public void doRequsetProcessorChain() {
        //通过迭代器遍历的注册请求处理器实现列表
        try {
            while (this.iterator.hasNext()){
                //直到某个请求处理器返回false
                if(!iterator.next().process(this)){
                    break;
                }

            }
        }catch (Exception e){
            //如果期间遇到异常 交由内部的异常渲染器渲染
            this.resultRender = new InternalErrorResultRender() ;
        }

    }

    public void doRender() {
        //如果请求处理器实现类均未实现选择合适的渲染器 则使用默认的
        if(this.resultRender == null){
            this.resultRender = new DefaultResultRender();
        }
        //调用渲染器的render方法进行渲染
        try {
            this.resultRender.render(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
