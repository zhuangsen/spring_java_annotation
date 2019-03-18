package com.zs.spring.config.rabbitmq.listener;


import com.zs.spring.entity.Mail;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class QueueListener1 {
	
	@RabbitListener(queues = "myqueue")
	public void displayMail(Mail mail) throws Exception {
		System.out.println("队列监听器1号收到消息"+mail.toString());
	}
}
