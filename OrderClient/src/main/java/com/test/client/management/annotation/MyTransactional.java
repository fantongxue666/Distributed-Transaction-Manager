package com.test.client.management.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName MyTransactional.java
 * @Description TODO
 * @createTime 2021年09月26日 09:27:00
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyTransactional {

    //表明事务是否开始
    boolean isStart() default false;
    //表明事务是否结束
    boolean isEnd() default false;

}
