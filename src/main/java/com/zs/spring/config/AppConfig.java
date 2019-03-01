package com.zs.spring.config;

import com.zs.spring.config.rabbitmq.RabbitConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @auther: madisonzhuang
 * @date: 2019-02-19 15:40
 * @description:
 */
@Configuration
@Import({RabbitConfig.class})
public class AppConfig {


}
