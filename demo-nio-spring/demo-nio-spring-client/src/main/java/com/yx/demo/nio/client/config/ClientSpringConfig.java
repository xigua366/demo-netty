package com.yx.demo.nio.client.config;

import com.yx.demo.nio.client.nio.NioClient;
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
public class ClientSpringConfig {

    @Bean
    public NioClient nioClient() {
        return new NioClient();
    }

}