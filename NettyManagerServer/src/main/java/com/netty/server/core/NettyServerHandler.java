package com.netty.server.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @createTime 2021年09月28日 11:44:00
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    //事务组中的事务状态列表
    private static Map<String, List<String>> transactionTypeMap = new HashMap<>();
    //事务组是否已经接受到了结束的标记
    private static Map<String,Boolean> isEndMap = new HashMap<>();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.add(channel);
    }

    /**
     * 创建事务组，并且添加保存事务
     * 并且需要判断，如果所有事务都已经执行了（有结果了，要么回滚，要么提交），且其中有一个事务需要回滚，那么通知所有客户端进行回滚，否则，通知所有客户端进行提交
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务端接收数据："+ msg.toString());
        JSONObject jsonObject = JSON.parseObject((String) msg);

        //create 创建事务组 add 添加事务
        String command = jsonObject.getString("command");
        //事务组id
        String groupId = jsonObject.getString("groupId");
        //子事务类型 commit：待提交 rollback：待回滚
        String transactionType = jsonObject.getString("transactionType");
        //是否是结束事务
        Boolean isEnd = jsonObject.getBoolean("isEnd");

        if(StringUtils.equals(command,"create")){
            //创建事务组
            transactionTypeMap.put(groupId,new ArrayList<String>());
        }else if(StringUtils.equals(command,"add")){
            //加入事务组
            transactionTypeMap.get(groupId).add(transactionType);
            //是否是最后一个事务节点
            if(isEnd){
                isEndMap.put(groupId,true);
            }

            JSONObject result = new JSONObject();
            result.put("groupId",groupId);
            //如果已经接收到结束事务的标记 则看是否需要回滚
            if(isEndMap.get(groupId)){
                if(transactionTypeMap.get(groupId).contains("rollback")){
                    result.put("command","rollback");
                    sendResult(result);
                }else {
                    result.put("command","commit");
                    sendResult(result);
                }
            }


        }
    }

    /**
     * 服务端向客户端广播消息
     * @param result
     */
    private void sendResult(JSONObject result) {
        for(Channel channel:channelGroup){
            System.out.println("服务端===（消息）==>客户端："+result.toJSONString());
            channel.writeAndFlush(result.toJSONString());
        }
    }
}
