package com.yx.demo.nio.server.nio;

import org.springframework.beans.factory.InitializingBean;

/**
 * 简版消息中间件Broker服务端启动入口
 */
public class NioServer implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        int port = 9092;
        new Thread(new ServerReactor(port)).start();
    }
}