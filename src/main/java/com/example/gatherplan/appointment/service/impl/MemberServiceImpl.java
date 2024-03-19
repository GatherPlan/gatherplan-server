package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.AuthenticateEmailReqDto;
import com.example.gatherplan.appointment.dto.CreateMemberReqDto;
import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.exception.MemberException;
import com.example.gatherplan.appointment.mapper.MemberMapper;
import com.example.gatherplan.appointment.repository.EmailAuthRepository;
import com.example.gatherplan.appointment.repository.MemberRepository;
import com.example.gatherplan.appointment.repository.entity.EmailAuth;
import com.example.gatherplan.appointment.repository.entity.Member;
import com.example.gatherplan.appointment.service.MemberService;
import com.example.gatherplan.common.exception.AuthenticationFailException;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService, UserDetailsService {
    private final MemberRepository memberRepository;
    private final EmailAuthRepository emailAuthRepository;

    private final MemberMapper memberMapper;

    private final Random random = new Random();
    private final JavaMailSender javaMailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public void authenticateEmail(AuthenticateEmailReqDto reqDto) {
        String email = reqDto.getEmail();

        memberRepository.findMemberByEmail(email).ifPresent(member -> {
            throw new MemberException(ErrorCode.RESOURCE_CONFLICT, "이미 사용중인 이메일입니다.");
        });

        if (!sendAuthCodeToEmail(email)) {
            throw new MemberException(ErrorCode.SERVICE_UNAVAILABLE, "이메일 전송에 실패했습니다.");
        }
    }

    @Override
    @Transactional
    public void joinMember(CreateMemberReqDto reqDto) {
        String email = reqDto.getEmail();

        EmailAuth emailAuth = emailAuthRepository.findEmailAuthByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 인증번호입니다."));

        if (now().isAfter(emailAuth.getExpiredAt())) {
            throw new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL, "만료된 인증입니다.");
        }

        if (!StringUtils.equals(reqDto.getAuthCode(), emailAuth.getAuthCode())) {
            throw new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL, "인증번호가 일치하지 않습니다.");
        }

        memberRepository.findMemberByName(reqDto.getName()).ifPresent(member -> {
            throw new MemberException(ErrorCode.RESOURCE_CONFLICT,
                    String.format("%s 은 이미 사용중인 이름입니다.", member.getName()));
        });

        String encodedPassword = bCryptPasswordEncoder.encode(reqDto.getPassword());

        Member member = memberMapper.to(reqDto, encodedPassword, UserAuthType.LOCAL, "ROLE_ADMIN");

        memberRepository.save(member);

        emailAuthRepository.deleteByEmail(emailAuth.getEmail());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        return new CustomUserDetails(member);
    }

    private boolean sendAuthCodeToEmail(String email) {
        emailAuthRepository.findEmailAuthByEmail(email)
                .ifPresent(emailAuth -> emailAuthRepository.deleteByEmail(emailAuth.getEmail()));

        String authCode = Integer.toString(random.nextInt(888888) + 111111);
        LocalDateTime expiredTime = now(ZoneId.of("Asia/Seoul")).plusMinutes(3);

        EmailAuth emailAuth = EmailAuth.builder()
                .authCode(authCode)
                .email(email)
                .expiredAt(expiredTime)
                .build();

        emailAuthRepository.save(emailAuth);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom("dlwogns1205@gmail.com");
        simpleMailMessage.setSubject("[Gather Plan] 일반 회원가입 이메일 인증");
        simpleMailMessage.setText("Gather Plan에 방문해주셔서 감사합니다.\n\n" + "인증번호는 " + authCode + " 입니다." + "\n\n 인증번호를 인증코드란에 입력해주세요.");

        try {
            javaMailSender.send(simpleMailMessage);
            return true;

        } catch (MailException e) {
            return false;
        }
    }
}

