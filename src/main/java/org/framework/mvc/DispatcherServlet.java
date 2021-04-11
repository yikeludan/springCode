package org.framework.mvc;

import org.framework.aop.AspectWeaver;
import org.framework.core.BeanContainer;
import org.framework.inject.annotation.Dependency;
import org.framework.mvc.processor.RequestProcessor;
import org.framework.mvc.processor.impl.ControllerRequestProcessor;
import org.framework.mvc.processor.impl.JspRequestProcessor;
import org.framework.mvc.processor.impl.PreRequestProcessor;
import org.framework.mvc.processor.impl.StaticRequestProcessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/*")
public class DispatcherServlet extends HttpServlet {
    List<RequestProcessor> PROCESSOR_LIST = new ArrayList<>();

    @Override
    public void init(){
        //初始化容器
        BeanContainer beanContainer =BeanContainer.getInstance();
        beanContainer.loadBean("com.sp");
       // new AspectWeaver().doAop();
        new Dependency().doIoc();
        //初始化责任链
     //   PROCESSOR_LIST.add(new PreRequestProcessor());
      //  PROCESSOR_LIST.add(new StaticRequestProcessor());
      //  PROCESSOR_LIST.add(new JspRequestProcessor());
        PROCESSOR_LIST.add(new ControllerRequestProcessor());

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)   {
        //创建责任链对象实例
        RequestProcessorChain requestProcessorChain =
                new RequestProcessorChain(PROCESSOR_LIST.iterator(),req,resp);
        //通过责任链模式来执行不同的request
        requestProcessorChain.doRequsetProcessorChain();
        //对处理结果进行渲染
        requestProcessorChain.doRender();

    }
}
