package edu.aztu.util;

import java.io.File;
import java.util.Properties;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;


public class GmailFileSender {
    private static final String EMAIL_FROM = "murselsemedov03@gmail.com";
    private static final String APP_PASSWORD = "moth qnao ruuz edho";
    public static void sendFileToGmail(String obj,String emailTo){
        System.out.println(emailTo);
        System.out.println(obj);
//        requireGmail();
        try {
            Message message = new MimeMessage(getEmailSession());
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject("Email subject");
            MimeMultipart mimeMultipart = new MimeMultipart();
            MimeBodyPart messagePart = new MimeBodyPart();
            messagePart.setText("This is my email sent from Gmail using Java");
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(obj));
            mimeMultipart.addBodyPart(attachmentPart);
            mimeMultipart.addBodyPart(messagePart);
            message.setContent(mimeMultipart);
            Transport.send(message);
            System.out.println("Gmail ugurla gonderildi");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

//    private static void requireGmail() throws IOException {
//        System.out.println("Gmail bildirin");
//        Configuration config1 = new Configuration();
//        String s = null;
//        config1.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
//        config1.setDictionaryPath("src\\main\\resources\\gmails.dic");
//        config1.setLanguageModelPath("src\\main\\resources\\gmails.lm");
//        LiveSpeechRecognizer speech = new LiveSpeechRecognizer(config1);
//        speech.startRecognition(true);
//        SpeechResult speechResult;
//        if ((speechResult = speech.getResult()) != null) {
//            String voiceCommand = speechResult.getHypothesis();
//            if(voiceCommand.contains("@gmail.com")){
//                emailTo = voiceCommand;
//            }
//        }
//    }
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

