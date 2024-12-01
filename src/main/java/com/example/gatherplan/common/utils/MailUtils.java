package com.example.gatherplan.common.utils;

import com.example.gatherplan.common.exception.BusinessException;
import com.example.gatherplan.common.exception.ErrorCode;
import lombok.experimental.UtilityClass;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@UtilityClass
public class MailUtils {
    public void sendEmail(String from, String to, String subjectText, String contentText, JavaMailSender mailSender) {
        String subject = "[Gather Plan] " + subjectText;
        String content = String.format("Gather Plan 에 방문해주셔서 감사합니다.%n") + contentText;

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);

        try {
            mailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new BusinessException(ErrorCode.SERVICE_UNAVAILABLE, "이메일 전송에 실패했습니다.");
        }
    }
}
