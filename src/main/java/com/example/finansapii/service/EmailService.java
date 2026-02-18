package com.example.finansapii.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String to, String code) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("FinansApp - Email Doğrulama Kodu");
        message.setText(
                "Merhaba,\n\n" +
                        "Email doğrulama kodunuz: " + code +
                        "\n\nBu kod 10 dakika geçerlidir.\n\n" +
                        "FinansApp Ekibi"
        );

        mailSender.send(message);
    }
}
