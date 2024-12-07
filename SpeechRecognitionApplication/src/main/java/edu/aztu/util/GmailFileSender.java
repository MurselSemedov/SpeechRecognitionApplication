package edu.aztu.util;

import java.io.File;
import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class GmailFileSender {
    private static final String EMAIL_FROM = "murselsemedov03@gmail.com";
    private static final String EMAIL_TO = "mursalsamadovv@gmail.com";
    private static final String APP_PASSWORD = "moth qnao ruuz edho";

    public static void sendFileToGmail(String obj) throws Exception {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(EMAIL_FROM));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_TO));
        message.setSubject("Email subject");
//        message.setText("This is my email sent from Gmail using Java");
        MimeMultipart mimeMultipart = new MimeMultipart();

        MimeBodyPart messagePart = new MimeBodyPart();
        messagePart.setText("This is my email sent from Gmail using Java");
//        MimeBodyPart filePart = new MimeBodyPart();
//        DataSource source = new FileDataSource(new File(obj));
//        filePart.setDataHandler(new DataHandler(source));
        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile(new File(obj));
        mimeMultipart.addBodyPart(attachmentPart);
        mimeMultipart.addBodyPart(messagePart);
        message.setContent(mimeMultipart);
        Transport.send(message);
    }

    private static Session getEmailSession() {
        return Session.getInstance(getGmailProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, APP_PASSWORD);
            }
        });
    }

    private static Properties getGmailProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        return prop;
    }
}

