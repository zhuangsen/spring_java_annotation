package com.zs.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @auther: madisonzhuang
 * @date: 2019-02-19 15:40
 * @description:
 */
@Configuration
@Import({RedisTemplateConfig.class, DataSourceConfig.class})
public class AppConfig {


}
