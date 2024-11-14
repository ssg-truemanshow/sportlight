package com.tms.sportlight.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * 이메일을 전송하는 메서드로, 제목과 내용을 외부에서 지정할 수 있도록 함.
     *
     * @param to 이메일 수신자 주소
     * @param subject 이메일 제목
     * @param content 전송할 이메일 내용 (HTML 형식 가능)
     * @throws MessagingException 메일 전송 중 예외 발생 시 예외처리
     */
    public void sendEmail(String to, String subject, String content) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, "utf-8");

        messageHelper.setText(content, true);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);

        mailSender.send(message);
    }


}
