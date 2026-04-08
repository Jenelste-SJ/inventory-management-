package org.example.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.util.Properties;

public class EmailUtil {

    public static void sendReport(String toEmail, String subject, String body, String attachmentPath) {
        final String fromEmail = System.getenv("MAIL_USER");
        final String password = System.getenv("MAIL_PASS");

        if (fromEmail == null || password == null) {
            throw new RuntimeException("Email credentials not set in environment variables!");
        }

        if (toEmail == null || !toEmail.contains("@")) {
            System.out.println("Invalid email!");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject != null ? subject : "Report");

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body != null ? body : "");

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(attachmentPath));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Report sent successfully to " + toEmail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void sendOTPEmail(String toEmail, String otp) {
        final String fromEmail = System.getenv("MAIL_USER");
        final String password = System.getenv("MAIL_PASS");

        if (fromEmail == null || password == null) {
            throw new RuntimeException("Email credentials not set in environment variables!");
        }

        if (toEmail == null || !toEmail.contains("@")) {
            System.out.println("Invalid email!");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Email Verification OTP");
            message.setText(
                    "Dear User,\n\n" +
                            "Your OTP for verifying your email is: " + otp + "\n\n" +
                            "This OTP is valid for one-time use only.\n\n" +
                            "Best regards,\nInventory Management System"
            );

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void sendAlertEmail(String toEmail, String subject, String body) {
        final String fromEmail = System.getenv("MAIL_USER");
        final String password = System.getenv("MAIL_PASS");

        if (fromEmail == null || password == null) {
            throw new RuntimeException("Email credentials not set in environment variables!");
        }

        if (toEmail == null || !toEmail.contains("@")) {
            System.out.println("Invalid or missing recipient email!");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject != null ? subject : "Alert");
            message.setText(body != null ? body : "");

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}