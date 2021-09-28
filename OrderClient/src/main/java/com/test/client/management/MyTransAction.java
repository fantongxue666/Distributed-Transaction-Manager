package com.test.client.management;

import com.test.client.management.annotation.TransactionType;

/**
 * @author FanJiangFeng
 * @createTime 2021年09月28日 10:19:00
 *
 * 事务实体类
 */
public class MyTransAction {

    private String groupId;//事务组id
    private String transactionId;//事务id
    private TransactionType transactionType; //commit 待提交 rollback 待回滚
    private Task task;


    public MyTransAction(String groupId, String transactionId) {
        this.groupId = groupId;
        this.transactionId = transactionId;
        this.task = new Task();
    }



    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
