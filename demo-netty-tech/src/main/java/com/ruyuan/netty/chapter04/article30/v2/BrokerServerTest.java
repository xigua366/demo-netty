package com.ruyuan.netty.chapter04.article30.v2;

/**
 * 简版消息中间件Broker服务端启动入口
 */
public class BrokerServerTest {

    public static void main(String[] args) {
        new Thread(new ReactorThread()).start();
    }
}