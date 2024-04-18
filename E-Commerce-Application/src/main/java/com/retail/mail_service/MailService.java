package com.retail.mail_service;

import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class MailService {
	private JavaMailSender javaMailSender;

	public MailService(JavaMailSender javaMailSender) {
		super();
		this.javaMailSender = javaMailSender;
	}
	
	
	public void sendMailMessage(MessageModel model) throws MessagingException {
		MimeMessage message=javaMailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message, true);
		helper.setTo(model.getTo());
		helper.setSubject(model.getSubject());
		helper.setSentDate(new Date());
		helper.setText(model.getText(),true);
		javaMailSender.send(message);
		
	}

	
}
