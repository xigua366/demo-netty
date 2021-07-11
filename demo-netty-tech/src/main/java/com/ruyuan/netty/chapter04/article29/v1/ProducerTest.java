package com.ruyuan.netty.chapter04.article29.v1;

/**
 * Producer客户端测试类
 */
public class ProducerTest {

    public static void main(String[] args) {
        // 启动Producer客户端
        new Thread(new ProducerClient()).start();
    }
}