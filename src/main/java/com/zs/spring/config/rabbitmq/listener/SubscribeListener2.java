package com.zs.spring.config.rabbitmq.listener;

import com.zs.spring.entity.Mail;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class SubscribeListener2 {
	@RabbitListener(queues = "queue2")
	public void subscribe(Mail mail) throws IOException {
		System.out.println("订阅者2收到消息"+mail.toString());
	}
}
