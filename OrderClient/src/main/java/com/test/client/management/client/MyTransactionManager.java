package com.test.client.management.client;

import com.alibaba.fastjson.JSONObject;
import com.test.client.management.MyTransAction;
import com.test.client.management.annotation.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author FanJiangFeng
 * @createTime 2021年09月28日 14:06:00
 */
public class MyTransactionManager {

    private static ThreadLocal<MyTransAction> current = new ThreadLocal<>();
    public static NettyClient nettyClient;

    @Autowired
    private void setNettyClient(NettyClient nettyClient){
        MyTransactionManager.nettyClient = nettyClient;
    }

    public static Map<String,MyTransAction> TRANSCTION_MAP = new HashMap<>();

    /**
     * 创建事务组
     * @return
     */
    public static String createTransactionGroup(){
        String groupId = UUID.randomUUID().toString().replace("-","");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId",groupId);
        jsonObject.put("command","create");
        nettyClient.send(jsonObject);
        System.out.println("========= 创建事务组，GroupId："+groupId+" ==========");
        return groupId;
    }

    /**
     * 创建事务
     */
    public static MyTransAction createTransction(String groupId){
        String transactionId = UUID.randomUUID().toString().replace("-","");
        MyTransAction myTransAction = new MyTransAction(groupId,transactionId);
        TRANSCTION_MAP.put(groupId,myTransAction);
        System.out.println("========= 创建事务组，TransactionId："+transactionId+" ==========");
        current.set(myTransAction);
        return myTransAction;
    }

    /**
     * 添加事务
     */
    public static MyTransAction addTransction(MyTransAction transAction,Boolean isEnd,TransactionType transactionType){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId",transAction.getGroupId());
        jsonObject.put("transactionId",transAction.getTransactionId());
        jsonObject.put("transactionType",transactionType);
        jsonObject.put("command","add");
        jsonObject.put("isEnd",isEnd);
        nettyClient.send(jsonObject);
        System.out.println("========= 添加事务 ==========");
        return transAction;
    }

    /**
     * 根据groupId得到事务对象
     */
    public static MyTransAction getTransActionByGroupId(String groupId){
        return TRANSCTION_MAP.get(groupId);
    }

    /**
     * 当前线程获取当前事务对象
     */
    public static MyTransAction getCurrentTransaction(){
        return current.get();
    }

}
