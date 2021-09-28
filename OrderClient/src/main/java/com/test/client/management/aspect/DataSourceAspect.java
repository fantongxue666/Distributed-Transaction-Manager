package com.test.client.management.aspect;

import com.test.client.management.MyConnection;
import com.test.client.management.MyTransAction;
import com.test.client.management.client.MyTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * @author FanJiangFeng
 * @createTime 2021年09月28日 10:10:00
 */
@Aspect
@Component
public class DataSourceAspect {

    /**
     * 切getConnection方法
     * 执行getConnection方法，并创建MyConnection，通过构造器注入进去，返回MyConnection
     *
     * @return
     */
    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection around(ProceedingJoinPoint point) throws Throwable {
        Connection connection = (Connection)point.proceed();
        //获取当前线程的当前事务对象
        MyTransAction currentTransaction = MyTransactionManager.getCurrentTransaction();
        MyConnection myConnection = new MyConnection(connection,currentTransaction);
        return myConnection;
    }
}
