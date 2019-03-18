package com.zs.spring.config.rabbitmq.listener;

import com.zs.spring.entity.Mail;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class DirectListener2 {
	@RabbitListener(queues = "directqueue2")
	public void displayMail(Mail mail) throws Exception {
		System.out.println("directqueue2队列监听器2号收到消息"+mail.toString());
	}
}
