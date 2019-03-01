package com.zs.spring.config;

/**
 * @auther: madisonzhuang
 * @date: 2019-02-27 17:47
 * @description:
 */
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

@Configuration
@Slf4j
@PropertySource(value = "classpath:config.properties",encoding = "UTF-8")
public class RedisTemplateConfig {

    @Autowired
    private Environment environment;

    @Value("${redis.pool.maxTotal}")
    private Integer maxTotal;
    @Value("${redis.pool.maxIdle}")
    private Integer maxIdle;
    @Value("${redis.pool.testOnBorrow}")
    private Boolean testOnBorrow;
    @Value("${redis.pool.testOnReturn}")
    private Boolean testOnReturn;
    @Value("${redis.pool.maxWait}")
    private Long maxWait;
    @Value("${redis.password}")
    private String password;
    @Value("${redis.database}")
    private Integer database;

    @Bean
    public JedisPoolConfig jedisPoolConfig(){
        log.info("初始化redisPoolConfig.................");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestOnReturn(testOnReturn);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        return jedisPoolConfig;
    }


    @Bean
    public RedisSentinelConfiguration redisSentinelConfiguration(){
        log.info("初始化redisSentinelConfiguration.................");
        RedisSentinelConfiguration configuration = new RedisSentinelConfiguration();
        configuration.setPassword(password);
        configuration.setDatabase(database);


        Set<RedisNode> redisNodeSet = Sets.newHashSet();

        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            String host = environment.getProperty("sentinel.host" + i);
            if(StringUtils.isEmpty(host)){
                break;
            }
            Integer port = environment.getProperty("sentinel.port" + i, Integer.class);
            redisNodeSet.add(new RedisNode(host,port));
        }
        log.info("一共有"+redisNodeSet.size()+"个哨兵");
        configuration.setSentinels(redisNodeSet);
        configuration.setMaster("mymaster");

        return  configuration;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig poolConfig,RedisSentinelConfiguration sentinelConfig){
        log.info("初始化redisConnectionFactory.................");
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(sentinelConfig, poolConfig);
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String,Object> redisTemplate(JedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);


        //自定义序列化方式
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        redisTemplate.setKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();


        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(JedisConnectionFactory redisConnectionFactory){
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }

}