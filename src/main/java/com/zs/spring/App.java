package com.zs.spring;

import com.zs.spring.service.GenCounterService;
import com.zs.spring.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @auther: madisonzhuang
 * @date: 2019-02-27 17:24
 * @description:
 */
public class App {

    public static void main(String[] args) {
        // 通过Java配置来实例化Spring容器
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        context.getBean(GenCounterService.class).genCounter();
        context.close();
    }
}
