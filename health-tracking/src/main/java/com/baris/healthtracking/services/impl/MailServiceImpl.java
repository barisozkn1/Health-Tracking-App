package com.baris.healthtracking.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class MailServiceImpl {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String to, String subject, String body) {
        System.out.println("E-posta gönderiliyor: " + to); // Log ekle
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            System.out.println("E-posta gönderim hatası: " + e.getMessage()); // Detaylı hata
            throw new RuntimeException("E-posta gönderiminde hata oluştu.");
        }
    }

}
