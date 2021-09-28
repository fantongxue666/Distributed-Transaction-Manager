package com.test.client.management.client;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author FanJiangFeng
 * @createTime 2021年09月28日 13:34:00
 *
 * springboot启动就执行
 */
@Component
public class NettyClient implements InitializingBean {

    public NettyClientHandler client = null;

    @Override
    public void afterPropertiesSet() {
        //启动客户端
        start("localhost",8080);
    }

    private void start(String hostName, int port) {
        //创建自定义的客户端处理器
        client = new NettyClientHandler();
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        //向pipeline加入解码器
                        pipeline.addLast("decoder",new StringDecoder());
                        //向pipeline加入编码器
                        pipeline.addLast("encoder",new StringEncoder());
                        //加入自己的业务处理handler
                        pipeline.addLast(client);
                    }
                });
        try {
            bootstrap.connect(hostName,port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void send(JSONObject jsonObject){
        client.call(jsonObject);
    }


}
