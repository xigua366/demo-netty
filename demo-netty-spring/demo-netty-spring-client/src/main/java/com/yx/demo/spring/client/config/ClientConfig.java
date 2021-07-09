package com.yx.demo.spring.client.config;

import com.yx.demo.spring.client.netty.NettyEchoClient;
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
public class ClientConfig {

    @Bean
    public NettyEchoClient nettyEchoClient() {
        return new NettyEchoClient();
    }
}