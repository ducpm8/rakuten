package com.web.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.web.util.config.CommonProperties;

public class SendAttachmentInEmail {
	public static void sendMail(String title, String emailBody, List<String> filePath) throws IOException, URISyntaxException {
		
		CommonProperties.loadProperties();

		final String username = CommonProperties.getEmailSender();
		final String password = CommonProperties.getEmailSenderPass();

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from-email@gmail.com"));
			
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(CommonProperties.getEmailRecipient()));
			message.setSubject(new Formatter().format(CommonProperties.getEmailTitle(), title).toString());

			// Create a multipar message
	        Multipart multipart = new MimeMultipart();
			// Create the message part
	        BodyPart messageBodyPart = new MimeBodyPart();

	        messageBodyPart.setText(emailBody);
	        multipart.addBodyPart(messageBodyPart);
	         
	        for (int i=0; i < filePath.size(); i++) {
				 Path p = Paths.get(filePath.get(i));
				 String fileName = p.getFileName().toString();
				 
				 messageBodyPart = new MimeBodyPart();
				 DataSource source = new FileDataSource(filePath.get(i));
				 messageBodyPart.setDataHandler(new DataHandler(source));
				 messageBodyPart.setFileName(fileName);
				 multipart.addBodyPart(messageBodyPart);
	        }

	         // Send the complete message parts
	         message.setContent(multipart);
			
			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}