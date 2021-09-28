package com.test.client.management;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author FanJiangFeng
 * @createTime 2021年09月28日 11:11:00
 */
public class Task {
    public Lock lock = new ReentrantLock();
    Condition con = lock.newCondition();

    public void waitTask(){
        try {
            lock.lock();
            con.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void signlTask(){
        lock.lock();
        con.signal();
        lock.unlock();
    }
}
