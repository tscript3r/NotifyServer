package pl.tscript3r.notify2.server.email;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import pl.tscript3r.notify2.server.constants.MainSettings;
import pl.tscript3r.notify2.server.utility.PropertiesLoader;

public class EmailSender {

	Properties properties = new Properties();
	private String username;
	private String password;

	public EmailSender() throws IOException, ParseException {
		PropertiesLoader propertiesLoader = new PropertiesLoader(MainSettings.PROPERTIES_FILE);
		username = propertiesLoader.getProperty("email.username");
		password = propertiesLoader.getProperty("email.password");
		properties.put("mail.smtp.host", propertiesLoader.getProperty("email.host"));
		properties.put("mail.smtp.port", propertiesLoader.getProperty("email.port"));
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.timeout", "5000");
		properties.put("mail.smtps.timeout", "5000");
		properties.put("mail.smtps.connectiontimeout", "5000");
		properties.put("mail.smtp.starttls.enable", "true");
	}
	
	public EmailSender(String username, String password, String host, String port) throws IOException {
		this.username = username;
		this.password = password;
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.timeout", "5000");
		properties.put("mail.smtps.timeout", "5000");
		properties.put("mail.smtps.connectiontimeout", "5000");
		properties.put("mail.smtp.starttls.enable", "true");	
	}

	public void sendHtmlEmail(String toAddress, String subject, String message)
			throws AddressException, MessagingException {
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};
		Session session = Session.getInstance(properties, auth);
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(username));
		InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		msg.setContent(message, "text/html; charset=utf-8");
		Transport.send(msg);

	}

}