package com.ruyuan.netty.chapter04.article29.v2;

/**
 * 简版消息中间件Producer客户端启动入口
 */
public class ProducerClientTest {

    public static void main(String[] args) {
        new Thread(new ReactorThread("localhost", 9092)).start();
    }

}