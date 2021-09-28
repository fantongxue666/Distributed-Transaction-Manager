package com.stock.client.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName StockMapper.java
 * @Description TODO
 * @createTime 2021年09月26日 09:19:00
 */
@Mapper
@Repository
public interface StockMapper {

    //减少库存
    @Update(value = "update stu set age = age - 1 where id = '1'")
    int reduceStock();
}
