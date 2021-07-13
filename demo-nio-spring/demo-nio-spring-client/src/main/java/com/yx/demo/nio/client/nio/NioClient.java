package com.yx.demo.nio.client.nio;

import org.springframework.beans.factory.InitializingBean;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class NioClient implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {

        new Thread(new ClientReactor("localhost", 9092)).start();
    }
}