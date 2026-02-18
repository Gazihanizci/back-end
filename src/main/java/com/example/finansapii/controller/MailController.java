package com.example.finansapii.controller;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;

public class MailController {
    private final JavaMailSender mailSender;

    public MailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @GetMapping("/test")
    public String test() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("KENDI_MAILIN@gmail.com");
        msg.setSubject("FinansApp Test");
        msg.setText("Mail sistemi çalışıyor ✅");

        mailSender.send(msg);
        return "OK";
    }
}
