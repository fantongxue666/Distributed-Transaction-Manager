package com.test.client.management.aspect;

import com.test.client.management.MyTransAction;
import com.test.client.management.annotation.MyTransactional;
import com.test.client.management.annotation.TransactionType;
import com.test.client.management.client.MyTransactionManager;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

/**
 * @author FanJiangFeng
 * @createTime 2021年09月28日 10:13:00
 */
public class AnnotationAspect implements Ordered {

    /**
     * 切 @MyTransictional 自定义注解
     * @param point
     */
    public void invoke(ProceedingJoinPoint point){
        //获取引用该注解的方法
        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();
        //获取注解
        MyTransactional annotation = method.getAnnotation(MyTransactional.class);
        //判断事务动作
        String groupId = StringUtils.EMPTY;
        if(annotation.isStart()){
            //创建事务组
             groupId = MyTransactionManager.createTransactionGroup();
        }else{
            //得到当前事务组的groupId
            groupId = MyTransactionManager.getCurrentTransaction().getGroupId();
        }
        //创建事务
        MyTransAction transction = MyTransactionManager.createTransction(groupId);

        //走spring逻辑
        try {
            point.proceed();
            //添加事务
            MyTransactionManager.addTransction(transction,annotation.isEnd(), TransactionType.COMMIT);
        } catch (Throwable throwable) {
            MyTransactionManager.addTransction(transction,annotation.isEnd(), TransactionType.ROLLBACK);
            throwable.printStackTrace();
        }
    }

    @Override
    public int getOrder() {
        return 100000;
    }
}
