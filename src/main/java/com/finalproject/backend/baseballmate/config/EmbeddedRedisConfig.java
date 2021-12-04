package com.finalproject.backend.baseballmate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
//@Profile("local")
@Profile("3.34.46.135")
public class EmbeddedRedisConfig {
    private RedisServer redisServer;

    @PostConstruct
    public void start() {
        redisServer = RedisServer.builder()
                .port(6379)
                .setting("maxmemory 800M")
                .build();
        redisServer.start();
    }

    @PreDestroy
    public void stop() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}
