package com.ruyuan.netty.chapter04.article34.client;

/**
 * 简版消息中间件Producer客户端启动入口
 */
public class ProducerClientTest {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 9092;
        Runnable runnable = new ClientReactor(host, port);
        // 用一个独立的线程来运行
        new Thread(runnable).start();
    }

}