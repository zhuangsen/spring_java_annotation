package com.zs.spring.service;


import com.zs.spring.entity.Mail;

public interface Producer {
	public void sendMail(String queue, Mail mail);//向队列queue发送消息
}
