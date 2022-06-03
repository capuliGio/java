package ca.sheridancollege.mail;

import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import ca.sheridancollege.beans.Toy;

@Component
public class EmailServiceImpl {

	@Autowired
	private JavaMailSender emailSender;
	
	
	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
		
	}
	
	@Autowired
	private TemplateEngine templateEngine;
	
	public void sendMailWithThymeleaf(String to, String subject, String name, String messageBody, String footer, ArrayList<Toy> toy) 
			throws MessagingException{
		//Prepare evaluation context
		final Context ctx = new Context();
		ctx.setVariable("name", name);
		ctx.setVariable("message", messageBody);
		ctx.setVariable("footer", footer);
		ctx.setVariable("toys", toy);
		
		//Prepare message using a Spring helper
		final MimeMessage mimeMessage = this.emailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		message.setSubject(subject);
		message.setFrom("capuli.hernaldgior@gmail.com");
		message.setTo(to);
		
		//Create the HTML body using Thymeleaf
		final String htmlContent = this.templateEngine.process("emailTemplate.html", ctx);
		message.setText(htmlContent, true); // true = isHTML
		
		//Sent email
		this.emailSender.send(mimeMessage);
	}
		
}
