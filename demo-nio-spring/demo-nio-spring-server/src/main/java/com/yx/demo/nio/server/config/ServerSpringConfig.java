package com.yx.demo.nio.server.config;

import com.yx.demo.nio.server.nio.NioServer;
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
public class ServerSpringConfig {

    @Bean
    public NioServer nioClient() {
        return new NioServer();
    }

}