package org.framework.inject.annotation;

import com.sun.deploy.util.StringUtils;
import org.framework.core.BeanContainer;
import org.framework.util.ClassUtil;

import java.lang.reflect.Field;
import java.util.Set;

public class Dependency {

    private BeanContainer beanContainer;

    public Dependency(){
        beanContainer = BeanContainer.getInstance();

    }

    public void doIoc(){
        //遍历bean中所有class对象
        for(Class<?> c:beanContainer.getClassList()){
            //遍历class里所有的成员变量
            Field fields[] = c.getDeclaredFields();
            if(fields == null||fields.length == 0){
                continue;
            }
            for(Field field:fields){
                //找出被Autowired 标记的的变量
                if(field.isAnnotationPresent(Autowired.class)){
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String autoValue = autowired.value();

                    //获取这些变量类型
                    Class<?> fieldClazz = field.getType();
                    //获取这些变量类型在容器里的实例
                    Object fieldValue =  getFieldInstance(fieldClazz,autoValue);
                   if(fieldValue == null){
                       throw new RuntimeException("value error");
                   }
                    //通过反射将对应的成员变量实例注入到成员变量所在的类实例
                    Object tagretBean = beanContainer.getBean(c);
                    ClassUtil.setField(field,tagretBean,fieldValue,true);
                }
            }

        }

    }

    private Object getFieldInstance(Class<?> fieldClazz,String name) {
       Object o =  beanContainer.getBean(fieldClazz);
       if(o!= null){
           return o;
       }else {
           Class<?> impl = getImplClass(fieldClazz,name);
           if(impl != null){
               return beanContainer.getBean(impl);
           }
       }
       return null;
    }

    private Class<?> getImplClass(Class<?> fieldClazz,String name) {
        Set<Class<?>> classSet = beanContainer.getClassBySuper(fieldClazz);
        if(classSet == null){
           return null;
        }
        if(name == null||"".equals(name)){
            if(classSet.size() == 1){
                return  classSet.iterator().next();
            }else {
                throw new RuntimeException("TWO IMPL");
            }
        }else {
            for(Class<?> c:classSet){
                if(name.equals(c.getSimpleName())){
                    return c;
                }
            }
        }
        return null;
    }


}
