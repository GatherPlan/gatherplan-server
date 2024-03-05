package com.example.gatherplan.feature.join.service.impl;

import com.example.gatherplan.common.exception.AuthenticationFailException;
import com.example.gatherplan.common.exception.ExceptionInfo;
import com.example.gatherplan.common.exception.ResourceConflictException;
import com.example.gatherplan.common.exception.ResourceNotFoundException;
import com.example.gatherplan.domain.EmailAuth;
import com.example.gatherplan.domain.Member;
import com.example.gatherplan.domain.enums.UserAuthType;
import com.example.gatherplan.domain.enums.UserType;
import com.example.gatherplan.feature.join.dto.LocalJoinEmailFormDto;
import com.example.gatherplan.feature.join.dto.LocalJoinFormDto;
import com.example.gatherplan.feature.join.repository.LocalJoinRepository;
import com.example.gatherplan.feature.join.service.LocalJoinService;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocalJoinServiceImpl implements LocalJoinService {

    private final LocalJoinRepository localJoinRepository;
    private final Random random = new Random();

    private final JavaMailSender javaMailSender;

    @Override
    public void sendAuthCodeProcess(LocalJoinEmailFormDto localJoinEmailFormDto){
        String email = localJoinEmailFormDto.getEmail();
        checkEmailDuplicate(email);
        sendAuthCodeToEmail(email);
    }
    @Transactional
    public void sendAuthCodeToEmail(String email) {
        Optional<EmailAuth> findEmailAuth = localJoinRepository.findEmailAuthByEmail(email);

        if (findEmailAuth.isPresent()){
            localJoinRepository.deleteEmailAuth(email);
        }

        String authCode = Integer.toString(random.nextInt(888888) + 111111);

        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime expiredTime = createdTime.plusMinutes(1);

        EmailAuth emailAuth = EmailAuth.builder()
                .authCode(authCode)
                .userEmail(email)
                .createdTime(createdTime)
                .expiredTime(expiredTime)
                .build();

        localJoinRepository.saveEmailAuth(emailAuth);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom("dlwogns1205@gmail.com");
        simpleMailMessage.setSubject("[Gather Plan] 일반 회원가입 이메일 인증");
        simpleMailMessage.setText("Gather Plan에 방문해주셔서 감사합니다.\n\n" + "인증번호는 " + authCode + " 입니다." + "\n\n 인증번호를 인증코드란에 입력해주세요.");

        javaMailSender.send(simpleMailMessage);

    }

    public void checkEmailDuplicate(String email){
        Optional<Member> memberByEmail = localJoinRepository.findMemberByEmail(email);

        if (memberByEmail.isPresent()) {
            throw new ResourceConflictException(ExceptionInfo.RESOURCE_CONFLICT, "이미 사용 중인 이메일입니다.");
        }
    }

    @Override
    @Transactional
    public void validateLocalJoinFormProcess(LocalJoinFormDto localJoinFormDto){
        String email = localJoinFormDto.getEmail();
        String authCode = localJoinFormDto.getAuthCode();
        String name = localJoinFormDto.getName();
        String password = localJoinFormDto.getPassword();

        checkAuthCodeCorrect(authCode,email);
        checkNameDuplicate(name);

        Member member = Member.builder()
                .email(email)
                .name(name)
                .password(password)
                .userType(UserType.MEMBER)
                .userAuthType(UserAuthType.LOCAL)
                .build();

        localJoinRepository.saveMember(member);
    }

    @Transactional
    public void checkAuthCodeCorrect(String authCode,String email) {
        Optional<EmailAuth> findEmailAuth = localJoinRepository.findEmailAuthByEmail(email);

        if (findEmailAuth.isEmpty()){
            throw new ResourceNotFoundException(ExceptionInfo.RESOURCE_NOT_FOUND, "존재하지 않는 인증번호입니다.");
        }

        EmailAuth emailAuth = findEmailAuth.get();

        if (LocalDateTime.now().isAfter(emailAuth.getExpiredTime())){
            localJoinRepository.deleteEmailAuth(email); // 쿼리는 나가는데 삭제가 안됨
            throw new AuthenticationFailException(ExceptionInfo.AUTHENTICATION_FAIL, "만료된 인증입니다.");
        }

        if (!authCode.equals(emailAuth.getAuthCode())){
            throw new AuthenticationFailException(ExceptionInfo.AUTHENTICATION_FAIL, "인증번호가 일치하지 않습니다.");
        }
    }

    private void checkNameDuplicate(String name) {
        Optional<Member> findMember = localJoinRepository.findMemberByName(name);

        if (findMember.isPresent()){
            throw new ResourceConflictException(ExceptionInfo.RESOURCE_CONFLICT, "이미 사용 중인 이름입니다.");
        }
    }

}
