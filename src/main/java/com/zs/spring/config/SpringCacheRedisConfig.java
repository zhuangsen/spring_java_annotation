package com.zs.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @auther: madisonzhuang
 * @date: 2019-02-27 17:29
 * @description:
 */
//@Configuration
//@EnableRedisHttpSession
public class SpringCacheRedisConfig {
    private Logger logger = LoggerFactory.getLogger(SpringCacheRedisConfig.class);

    @Value("${redis.port}")
    private Integer prot;

    @Value("${redis.database}")
    private Integer database;

    @Value("${redis.hostname}")
    private String hostname;

    @Value("${redis.password}")
    private String password;

    @Value("${redis.cache.path}")
    private String cachePath;

    @Bean("stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(@Qualifier("redisConnectionFactory")LettuceConnectionFactory redisConnectionFactory){
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }

    @Bean("standaloneConfiguration")
    public RedisStandaloneConfiguration standaloneConfiguration() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setPort(prot);
        configuration.setDatabase(database);
        configuration.setHostName(hostname);
        configuration.setPassword(RedisPassword.of(password));

        return configuration;
    }

    @Bean("redisConnectionFactory")
    public LettuceConnectionFactory redisConnectionFactory(@Qualifier("standaloneConfiguration") RedisStandaloneConfiguration configuration) {
        return new LettuceConnectionFactory(configuration);
    }

    @Bean("springCacheManager")
    public RedisCacheManager redisCacheManager(@Qualifier("redisConnectionFactory") RedisConnectionFactory factory) {
        RedisCacheConfiguration defaultConfiguration = RedisCacheConfiguration.defaultCacheConfig();

        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(factory);
//        builder.withInitialCacheConfigurations(load(defaultConfiguration));
        builder.cacheDefaults(defaultConfiguration);

        return builder.build();
    }

    /** 加载缓存配置 */
//    protected Map<String, RedisCacheConfiguration> load(RedisCacheConfiguration defaultConfiguration) {
//        Map<String, RedisCacheConfiguration> configurations = new HashMap<>();
//
//        try {
//            Resource resource = ResourceUtil.getResource(cachePath);
//
//            if (resource != null && resource.exists()) {
//                Properties properties = PropertiesLoaderUtils.loadProperties(resource);
//                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
//                    Long seconds = Convert.toLong(entry.getValue(), 0L);
//                    String key = Convert.toStr(entry.getKey(), null);
//                    Duration ttl = Duration.ofSeconds(seconds);
//
//                    logger.info("Load Cache configuration {} TTL is {} seconds.", key, seconds);
//
//                    configurations.put(key, defaultConfiguration.entryTtl(ttl));
//                }
//            } else {
//                logger.warn("Not find cache configuration with {}", cachePath);
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//
//        return configurations;
//    }

}