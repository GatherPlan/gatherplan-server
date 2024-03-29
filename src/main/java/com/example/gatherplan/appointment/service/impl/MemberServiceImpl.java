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
import com.example.gatherplan.common.jwt.RoleType;
import com.example.gatherplan.common.jwt.UserInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${spring.mail.username}")
    private String adminEmail;

    @Override
    @Transactional
    public void authenticateEmail(AuthenticateEmailReqDto authenticateEmailReqDto) {

        String email = authenticateEmailReqDto.getEmail();

        memberRepository.findByEmail(email).ifPresent(member -> {
            throw new MemberException(ErrorCode.RESOURCE_CONFLICT, "이미 사용중인 이메일입니다.");
        });

        emailAuthRepository.findByEmail(email)
                .ifPresent(emailAuth -> emailAuthRepository.deleteByEmail(emailAuth.getEmail()));

        String authCode = Integer.toString(random.nextInt(888888) + 111111);
        LocalDateTime expiredTime = now().plusMinutes(3);

        EmailAuth emailAuth = EmailAuth.builder()
                .authCode(authCode)
                .email(email)
                .expiredAt(expiredTime)
                .build();

        emailAuthRepository.save(emailAuth);

        String subject = "[Gather Plan] 일반 회원가입 이메일 인증";
        String content = String.format(
                "Gather Plan 에 방문해주셔서 감사합니다. 인증번호는 %s 입니다. 인증번호를 인증코드란에 입력해주세요.", authCode);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(adminEmail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new MemberException(ErrorCode.SERVICE_UNAVAILABLE, "이메일 전송에 실패했습니다.");
        }
    }

    @Override
    @Transactional
    public void joinMember(CreateMemberReqDto reqDto) {
        String email = reqDto.getEmail();

        EmailAuth emailAuth = emailAuthRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 인증번호입니다."));

        if (now().isAfter(emailAuth.getExpiredAt())) {
            throw new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL, "만료된 인증입니다.");
        }

        if (!StringUtils.equals(reqDto.getAuthCode(), emailAuth.getAuthCode())) {
            throw new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL, "인증번호가 일치하지 않습니다.");
        }

        memberRepository.findByNickname(reqDto.getNickname()).ifPresent(member -> {
            throw new MemberException(ErrorCode.RESOURCE_CONFLICT, "이미 사용중인 이름입니다.");
        });

        String encodedPassword = bCryptPasswordEncoder.encode(reqDto.getPassword());

        Member member = memberMapper.to(reqDto, encodedPassword, UserAuthType.LOCAL, RoleType.USER);

        memberRepository.save(member);

        emailAuthRepository.deleteByEmail(emailAuth.getEmail());
    }

    /**
     * <h3>Spring Security 용 구현체 (interface : UserDetailsService)</h3>
     * 토큰에서 email 정도를 가져와 DB 에서 회원 정보를 가져옴
     *
     * @param email 토큰 내 회원 이메일 정보
     * @return UserInfo : UserDetails 구현체
     * @throws UsernameNotFoundException Spring Security 제공 에러
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        return new UserInfo(member);
    }

}

