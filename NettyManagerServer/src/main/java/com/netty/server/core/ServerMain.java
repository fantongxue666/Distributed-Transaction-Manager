package com.netty.server.core;

/**
 * @author FanJiangFeng
 * @createTime 2021年09月28日 13:24:00
 */
public class ServerMain {
    public static void main(String[] args) {
        NettyServer server = new NettyServer();
        server.start("localhost",8080);
        System.out.println("============= Netty服务端【全局事务管理器】已启动 ============");
    }
}
