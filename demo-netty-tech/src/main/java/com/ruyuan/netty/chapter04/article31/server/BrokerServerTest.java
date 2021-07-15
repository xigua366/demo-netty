package com.ruyuan.netty.chapter04.article31.server;

/**
 * 简版消息中间件Broker服务端启动入口
 */
public class BrokerServerTest {

    public static void main(String[] args) {
        int port = 9092;
        new Thread(new ServerReactor(port)).start();
    }
}