package in.indiaBridal.commands;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.sun.mail.util.MailSSLSocketFactory;

import in.indiaBridal.UtilityClasses.Env;
import in.indiaBridal.UtilityClasses.ReadProperties;

public class SendEmailCommand
{
	final static Logger logger = Logger.getLogger(SendEmailCommand.class);
	String env = "DEV";

	public static void main(String args[])
	{
		SendEmailCommand sendObj = new SendEmailCommand();
		sendObj.sendEmail("rajeshkm7958@gmail.com", "dummy", sendObj.getRegisterBodyContent());
	}
	
	public String getRegisterBodyContent()
	{
		logger.debug("getting email bodyu content for successfull registration");
		String emailBody = "Hello,<br><br>"
				+ "Welcome to IndiazBridal. <br><br>"
				+ "Thanks for registering with us. Soon you will receive a verification email to verify your email id."
				+"Please complete the registration process to help us in finding your better half. <br><br>"
				+ " Regards, <br>IndiazBridal Team";

		return emailBody;
	}

	public void sendEmail(String receiverEmailID,String subject, String bodyContent)
	{
		logger.debug("getting property values for email");
		// Sender's email ID needs to be mentioned
		String env = Env.getEnv();
		if(env==null || env.isEmpty())
			env="DEV";
		ReadProperties readPropObj = new ReadProperties();
		ArrayList keyNames = new ArrayList();
		keyNames.add("emailUserName"+env);
		keyNames.add("emailPassword"+env);
		keyNames.add("emailHost"+env);
		keyNames.add("emailPortNo"+env);
		HashMap propVals = readPropObj.readMultiProperties(keyNames);

		if(propVals!=null)
		{
			final String username = (String) propVals.get("emailUserName"+env);
			final String password = (String) propVals.get("emailPassword"+env);
			String host = (String) propVals.get("emailHost"+env); //smtp.gmail.com;
			int portNo = Integer.parseInt((String) propVals.get("emailPortNo"+env));   // 587

			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", portNo);
			
			MailSSLSocketFactory socketFactory = null;
			try 
			{
				socketFactory = new MailSSLSocketFactory();
			} 
			catch (GeneralSecurityException e)
			{
				StringWriter stack = new StringWriter();
				e.printStackTrace(new PrintWriter(stack));
				logger.debug(" stack trace = "+stack);
			}
			socketFactory.setTrustAllHosts(true);
			props.put("mail.smtp.ssl.socketFactory", socketFactory);
			

			// Get the Session object.
			Session session = Session.getInstance(props,new javax.mail.Authenticator()
			{
				protected PasswordAuthentication getPasswordauthentication()
				{
					logger.debug("email pwd = "+password);
					return new PasswordAuthentication(username, password);
				}
			});

			try
			{
				logger.debug("creating message");
				// Create a default MimeMessage object.
				MimeMessage message =  new MimeMessage(session);

				// Set From: header field of the header.
				message.setFrom(new InternetAddress(username));

				// Set To: header field of the header.
				message.setRecipients(Message.RecipientType.TO,	InternetAddress.parse(receiverEmailID));

				// Set Subject: header field
				message.setSubject(subject);

				// Now set the actual message
				message.setContent(bodyContent, "text/html");
				// Send message
				Transport.send(message);

				logger.debug("Sent message successfully....");

			} 
			catch (MessagingException e)
			{
				logger.debug("EMAIL sending failed "+e.getMessage());
				logger.debug("MessagingException occurred");
				StringWriter stack = new StringWriter();
				e.printStackTrace(new PrintWriter(stack));
				logger.debug(" stack trace = "+stack);
			}
		}
		else
		{
			logger.debug("Failed to send email. propVals is null ");
		}
	}
}