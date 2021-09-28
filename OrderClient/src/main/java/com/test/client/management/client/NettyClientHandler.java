package com.test.client.management.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.client.management.MyTransAction;
import com.test.client.management.annotation.TransactionType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName NettyClientHandler.java
 * @Description TODO
 * @createTime 2021年09月28日 13:27:00
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext context;

    /**
     * 表示channel处于就绪状态，客户端连接，初始化全局变量ChannelHandlerContext
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    /**
     * 接收数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接收数据：" + msg.toString());
        JSONObject jsonObject = JSON.parseObject((String) msg);

        String groupId = jsonObject.getString("groupId");
        String command = jsonObject.getString("command");
        System.out.println("接收指令："+command);

        //对事务进行操作
        //根据接收到的groupId从事务管理器中获取事务对象
        MyTransAction transAction = MyTransactionManager.getTransActionByGroupId(groupId);
        //根据接收到的命令设置事务的状态
        if(StringUtils.equals(command,"rollback")){
            transAction.setTransactionType(TransactionType.ROLLBACK);
        }else {
            transAction.setTransactionType(TransactionType.COMMIT);
        }
        //唤醒阻塞
        transAction.getTask().signlTask();
    }

    public synchronized void call(JSONObject data){
        context.writeAndFlush(data.toJSONString());
    }
}
