package org.framework.mvc.annotation;

import jdk.nashorn.internal.ir.RuntimeNode;
import org.framework.mvc.type.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value() default "";
    RequestMethod method() default RequestMethod.GET;
}
