package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.AuthenticateEmailReqDto;
import com.example.gatherplan.appointment.dto.CreateMemberReqDto;
import com.example.gatherplan.appointment.dto.CreateTemporaryMemberReqDto;
import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.enums.UserType;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.MemberException;
import com.example.gatherplan.appointment.repository.MemberRepository;
import com.example.gatherplan.appointment.repository.entity.EmailAuth;
import com.example.gatherplan.appointment.repository.entity.Member;
import com.example.gatherplan.appointment.service.MemberService;
import com.example.gatherplan.common.exception.AuthenticationFailException;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService, UserDetailsService {
    private final MemberRepository memberRepository;
    private final Random random = new Random();
    private final JavaMailSender javaMailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public void authenticateEmail(AuthenticateEmailReqDto authenticateEmailReqDto) {
        String email = authenticateEmailReqDto.getEmail();
        checkEmailDuplicate(email);
        sendAuthCodeToEmail(email);
    }


    public void sendAuthCodeToEmail(String email) {
        Optional<EmailAuth> findEmailAuth = memberRepository.findEmailAuthByEmail(email);

        if (findEmailAuth.isPresent()) {
            memberRepository.deleteEmailAuth(email);
        }

        String authCode = Integer.toString(random.nextInt(888888) + 111111);

        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime expiredTime = createdTime.plusMinutes(3);

        EmailAuth emailAuth = EmailAuth.builder()
                .authCode(authCode)
                .userEmail(email)
                .createdTime(createdTime)
                .expiredTime(expiredTime)
                .build();

        memberRepository.saveEmailAuth(emailAuth);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom("dlwogns1205@gmail.com");
        simpleMailMessage.setSubject("[Gather Plan] 일반 회원가입 이메일 인증");
        simpleMailMessage.setText("Gather Plan에 방문해주셔서 감사합니다.\n\n" + "인증번호는 " + authCode + " 입니다." + "\n\n 인증번호를 인증코드란에 입력해주세요.");

        javaMailSender.send(simpleMailMessage);

    }

    public void checkEmailDuplicate(String email) {
        Optional<Member> memberByEmail = memberRepository.findMemberByEmail(email);

        if (memberByEmail.isPresent()) {
            throw new AppointmentException(ErrorCode.RESOURCE_CONFLICT, "이미 사용 중인 이메일입니다.");
        }
    }

    @Override
    public void joinMember(CreateMemberReqDto createMemberReqDto) {
        String email = createMemberReqDto.getEmail();
        String authCode = createMemberReqDto.getAuthCode();
        String name = createMemberReqDto.getName();
        String password = createMemberReqDto.getPassword();

        checkAuthCodeCorrect(authCode, email);
        checkNameDuplicate(name);

        Member member = Member.builder()
                .email(email)
                .name(name)
                .password(bCryptPasswordEncoder.encode(password))
                .userType(UserType.REGULAR)
                .userAuthType(UserAuthType.LOCAL)
                .role("ROLE_ADMIN")
                .build();

        memberRepository.saveMember(member);
    }

    public void checkAuthCodeCorrect(String authCode, String email) {
        Optional<EmailAuth> findEmailAuth = memberRepository.findEmailAuthByEmail(email);

        if (findEmailAuth.isEmpty()) {
            throw new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 인증번호입니다.");
        }

        EmailAuth emailAuth = findEmailAuth.get();

        if (LocalDateTime.now().isAfter(emailAuth.getExpiredTime())) {
            memberRepository.deleteEmailAuth(email); // 쿼리는 나가는데 삭제가 안됨
            throw new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL, "만료된 인증입니다.");
        }

        if (!authCode.equals(emailAuth.getAuthCode())) {
            throw new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL, "인증번호가 일치하지 않습니다.");
        }
    }

    private void checkNameDuplicate(String name) {
        Optional<Member> findMember = memberRepository.findMemberByName(name);

        if (findMember.isPresent()) {
            throw new AppointmentException(ErrorCode.RESOURCE_CONFLICT, "이미 사용 중인 이름입니다.");
        }
    }

    @Override
    @Transactional
    public void joinTemporaryMember(CreateTemporaryMemberReqDto createTemporaryMemberReqDto) {
        String name = createTemporaryMemberReqDto.getName();
        String password = createTemporaryMemberReqDto.getPassword();

        Member member = Member.builder()
                .name(name)
                .password(password)
                .role("ROLE_ADMIN")
                .userType(UserType.TEMPORARY)
                .build();

        memberRepository.saveMember(member);

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> findMember = memberRepository.findMemberByEmail(email);

        if (findMember.isEmpty()) {
            throw new MemberException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다.");
        }

        return new CustomUserDetails(findMember.get());
    }
}
