package com.stock.client.controller;

import com.stock.client.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName StockController.java
 * @Description TODO
 * @createTime 2021年09月26日 09:22:00
 */
@RestController
public class StockController {

    @Autowired
    private StockMapper stockMapper;

    @RequestMapping("/reduceStock")
    @Transactional
    public Integer reduceStock(){
        System.out.println("执行减少库存方法");
        return stockMapper.reduceStock();
    }
}
