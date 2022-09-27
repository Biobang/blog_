package com.kx.blog.common.aop;

import java.lang.annotation.*;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/8/1 21:35
 **/

//@Target({ElementType.TYPE,ElementType.METHOD})
//ElementType.TYPE代表可以放在类上面  METHOD代表可以放在方法上
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String module() default "";

    String operation() default "";

}
