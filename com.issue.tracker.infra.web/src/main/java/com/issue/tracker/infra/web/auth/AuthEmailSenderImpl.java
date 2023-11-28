package com.issue.tracker.infra.web.auth;

import com.issue.tracker.api.auth.AuthEmailSender;
import com.issue.tracker.api.logger.LogType;
import com.issue.tracker.api.logger.LoggerBuilder;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.Serializable;
import java.util.Properties;

@Stateless
public class AuthEmailSenderImpl implements AuthEmailSender, Serializable {

    @EJB
    private LoggerBuilder loggerBuilder;

    @Override
    public void sendUserRegistrationEmailConfirmation(String to, String token) {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "mail-server");
        prop.put("mail.smtp.port", "25");
        String username = "csabi@adventureseekers.go.ro";
        String password = "test123";

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(to));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Mail Subject");

            String msg = "Token: " + token;

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException ex) {
            loggerBuilder.create(getClass(), LogType.ERROR, ex.getMessage())
                    .build()
                    .print();
            throw new RuntimeException("Cannot send email");
        }

    }
}
