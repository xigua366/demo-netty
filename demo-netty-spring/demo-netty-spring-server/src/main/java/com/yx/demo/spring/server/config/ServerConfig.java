package com.yx.demo.spring.server.config;

import com.yx.demo.spring.server.netty.NettyEchoServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
@Configuration
public class ServerConfig {

    @Bean
    public NettyEchoServer nettyEchoServer() {
        return new NettyEchoServer();
    }

}