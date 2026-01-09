package com.easymeeting.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RedissonConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedissonConfig.class);

    @Value("${spring.redis.host:}")
    private String redisHost;

    @Value("${spring.redis.port:}")
    private Integer redisPort;

    @Bean(name = "redissonClient", destroyMethod = "shutdown")
    public RedissonClient redissonClient(){
        try{
            if (redisHost == null || redisHost.isEmpty()) {
                logger.warn("Redis host is not configured, Redisson will not be available");
                return null;
            }
            if (redisPort == null || redisPort <= 0) {
                logger.warn("Redis port is not configured, Redisson will not be available");
                return null;
            }
            
            Config config = new Config();
            config.useSingleServer()
                    .setAddress("redis://" + redisHost + ":" + redisPort)
                    .setConnectionPoolSize(10)
                    .setConnectionMinimumIdleSize(5)
                    .setConnectTimeout(3000)
                    .setTimeout(3000)
                    .setRetryAttempts(3)
                    .setRetryInterval(1500);
            
            RedissonClient redissonClient = Redisson.create(config);
            logger.info("Redisson client initialized successfully, address: redis://{}:{}", redisHost, redisPort);
            return redissonClient;
        } catch (Exception e) {
            logger.error("Failed to initialize Redisson client, please check Redis configuration", e);
            return null;
        }
    }
}

