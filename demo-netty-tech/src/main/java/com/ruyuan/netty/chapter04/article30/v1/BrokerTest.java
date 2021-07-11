package com.ruyuan.netty.chapter04.article30.v1;

/**
 * Broker服务端测试类
 */
public class BrokerTest {

    public static void main(String[] args) {
        // 启动Broker服务端
        new Thread(new BrokerServer()).start();
    }
}




