package com.zs.spring.task;

import com.zs.spring.config.RedisTemplateConfig;
import com.zs.spring.entity.Mail;
import com.zs.spring.service.Producer;
import com.zs.spring.service.Publisher;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @auther: madisonzhuang
 * @date: 2019-02-28 16:03
 * @description:
 */
@Slf4j
@Component
@EnableScheduling
@Lazy(value = false)
public class MyTask {

//    @Autowired
//    private RedissonClient redissonClient;

    @Autowired
    private Producer producer;
    @Autowired
    private Publisher publisher;


//    @Scheduled(cron = "*/5 * * * * ?")//每隔5秒执行一次
//    public void test() throws Exception {
//        logger.info("Test is working......");
//
//        RBucket<Integer> result = redissonClient.getBucket("hub:cache:creditLimit:counter:GRP20180927164605");
//        Integer s = result.get();
//        logger.info("redisson result : {}", s);
//    }

    @Scheduled(cron = "*/5 * * * * ?")
    public void send(){
        log.info("rabbitmq is sending......");
        producer.sendMail("myqueue",new Mail("1","2",2));
        log.info("rabbitmq is end......");
    }

    @Scheduled(cron = "*/10 * * * * ?")
    public void publisher(){
        log.info("publisher is sending......");
        publisher.publishMail(new Mail("publishMail","4",6));
        log.info("publisher is end......");
    }


    //@Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点整
    //@Scheduled(cron = "0 30 0 * * ?")//每天凌晨0点30分
    //@Scheduled(cron = "0 */60 * * * ?")//1小时处理一次

}
