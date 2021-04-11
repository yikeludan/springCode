README
===========================
该简易框架已通过自定义注解已完成 springmvc ioc aop  beanMap DispatcherServlet任务分发功能
****



****
## 核心目录
* [结构](#注释)
    * org.framework.core.BeanContainer.java 完成 class 文件的收集与标记自定义注解的beanMap文件收集
    * org.framework.inject.annotation.Dependency.java 完成benMap 集合 controller 里field ioc功能的注入 与aop功能的实现
    * org.framework.mvc.DispatcherServlet.java 完成了请求 加载 benMap metod.invoke以及返回json的功能
## 代码已经加入注释   