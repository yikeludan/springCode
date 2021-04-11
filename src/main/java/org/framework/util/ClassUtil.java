package org.framework.util;

import com.sp.controller.UserController;
import com.sp.service.userService;
import org.framework.aop.AspectWeaver;
import org.framework.core.BeanContainer;
import org.framework.inject.annotation.Dependency;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.attribute.standard.RequestingUserName;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ClassUtil {
    public  final  static Logger logger = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 获取包下类集合
     * @param packageName
     * @return
     */
   public static Set<Class<?>> extractPackageClass(String packageName){
       ClassLoader classLoader = getClassLoader();

       URL url = classLoader.getResource(packageName.replace(".","/"));
       if(url == null){
          // logger.error("url error");
           return null;
       }
       Set<Class<?>>  classSet = null;
       if(url.getProtocol().equalsIgnoreCase("file")){
           classSet = new HashSet<Class<?>>();
           File packageDir = new File(url.getPath());
           extractClassFile(classSet,packageDir,packageName);
       }
       return classSet;
   }

    /**
     * 获取目录下所有的class文件
     * @param emptyClassSet
     * @param packageDir
     * @param packageName
     */
   private static void extractClassFile(Set<Class<?>> emptyClassSet, File packageDir, String packageName) {
       if(!packageDir.isDirectory()){
           return;
       }
       //获取下面的文件和文件夹
       File fileList[] =  packageDir.listFiles(new FileFilter() {
           @Override
           public boolean accept(File file) {
               if(file.isDirectory()){
                   return true;
               }else {
                   //获取文件绝对值路径
                   String absoluteFilePath = file.getAbsolutePath();
                   if(absoluteFilePath.endsWith(".class")){
                       //是class文件 加载到set 容器里
                       addToSetClass(absoluteFilePath);
                   }
               }
               return false;
           }

           //根据class 绝对值路径生成class 对象放入set 容器
           private void addToSetClass(String absoluteFilePath) {
               //从绝对值路径里提取出包名
               absoluteFilePath =  absoluteFilePath.replace(File.separator,".");
               String className = absoluteFilePath.substring(absoluteFilePath.indexOf(packageName));
               className = className.substring(0,className.lastIndexOf("."));
               //反射机制获取类对象放入容器
               Class targetClass = loadClass(className);
               emptyClassSet.add(targetClass);
               return;
           }
       });

       if(fileList == null){
           return;
       }
       System.out.println(fileList.length);
       for (File file:fileList){
           //递归调用
           extractClassFile(emptyClassSet,file,packageName);
       }

       return;


   }

    /**
     * 根据包名加类名获取class对象
     * @param className
     * @return
     */
   public static Class<?> loadClass(String className){
       try {
           return Class.forName(className);
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
   }

   public static ClassLoader getClassLoader(){

       return Thread.currentThread().getContextClassLoader();
   }

   public static <T> T newInstance(Class<?> clazz,boolean access){
       try {
           Constructor constructor = clazz.getDeclaredConstructor();
           constructor.setAccessible(access);
           return (T)constructor.newInstance();
       } catch (Exception e) {
           throw new RuntimeException(e.getMessage());
       }
   }

   public static void setField(Field field,Object target,
                               Object value,boolean access){
       field.setAccessible(access);
       try {
           field.set(target,value);
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
   }

    public static void main(String[] args) {
      /*  BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBean("com.sp");
        new AspectWeaver().doAop();
        new Dependency().doIoc();
        UserController userController1 = (UserController) beanContainer.getBean(UserController.class);
        userController1.req(3);*/
       // System.out.println(userController1.userService);
       // Set<Class<?>> classSet = beanContainer.getClassBySuper(userService.class);
       // System.out.println(classSet);
    }
}
