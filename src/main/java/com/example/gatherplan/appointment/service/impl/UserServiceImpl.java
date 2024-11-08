package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.CreateUserReqDto;
import com.example.gatherplan.appointment.dto.UserInfoRespDto;
import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.UserException;
import com.example.gatherplan.appointment.mapper.UserMapper;
import com.example.gatherplan.appointment.repository.AppointmentRepository;
import com.example.gatherplan.appointment.repository.EmailAuthRepository;
import com.example.gatherplan.appointment.repository.UserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.UserRepository;
import com.example.gatherplan.appointment.repository.entity.EmailAuth;
import com.example.gatherplan.appointment.repository.entity.User;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.service.UserService;
import com.example.gatherplan.appointment.validator.AppointmentValidator;
import com.example.gatherplan.common.config.jwt.RoleType;
import com.example.gatherplan.common.config.jwt.UserInfo;
import com.example.gatherplan.common.exception.AuthenticationFailException;
import com.example.gatherplan.common.exception.BusinessException;
import com.example.gatherplan.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final UserAppointmentMappingRepository userAppointmentMappingRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final UserMapper userMapper;
    private final Random random = new Random();
    private final JavaMailSender javaMailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AppointmentRepository appointmentRepository;
    @Value("${spring.mail.username}")
    private String adminEmail;

    @Override
    @Transactional
    public void authenticateEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new UserException(ErrorCode.USER_EMAIL_DUPLICATED);
        });

        emailAuthRepository.findByEmail(email)
                .ifPresent(emailAuth -> emailAuthRepository.deleteByEmail(emailAuth.getEmail()));

        String authCode = Integer.toString(random.nextInt(888888) + 111111);
        LocalDateTime expiredTime = now().plusMinutes(30);

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
            throw new BusinessException(ErrorCode.SERVICE_UNAVAILABLE, "이메일 전송에 실패했습니다.");
        }
    }

    @Override
    @Transactional
    public void joinUser(CreateUserReqDto reqDto) {
        EmailAuth emailAuth = emailAuthRepository.findByEmail(reqDto.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.SERVICE_UNAVAILABLE, "해당 이메일로 전송된 인증번호가 없습니다."));

        if (now().isAfter(emailAuth.getExpiredAt())) {
            throw new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL, "만료된 인증입니다.");
        }

        if (!StringUtils.equals(reqDto.getAuthCode(), emailAuth.getAuthCode())) {
            throw new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL, "인증번호가 일치하지 않습니다.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(reqDto.getPassword());

        User user = userMapper.to(reqDto, encodedPassword, UserAuthType.LOCAL, RoleType.USER);

        userRepository.save(user);

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
    public UserInfo loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        return new UserInfo(user);
    }


    @Override
    public boolean checkHost(String appointmentCode, Long userId) {
        if (!appointmentRepository.existsByAppointmentCode(appointmentCode))
            throw new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);

        return AppointmentValidator.isUserHost(userId, userAppointmentMappingList);
    }

    @Override
    public boolean checkJoin(String appointmentCode, Long userId) {
        if (!appointmentRepository.existsByAppointmentCode(appointmentCode))
            throw new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);

        return AppointmentValidator.isUserParticipated(userId, userAppointmentMappingList);
    }

    @Override
    public boolean checkName(String appointmentCode, String name) {
        if (!appointmentRepository.existsByAppointmentCode(appointmentCode))
            throw new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);

        return AppointmentValidator.isNotDuplicatedName(name, userAppointmentMappingList);
    }

    @Override
    public boolean checkNickname(String appointmentCode, String nickname) {
        if (!appointmentRepository.existsByAppointmentCode(appointmentCode))
            throw new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);

        return AppointmentValidator.isNotDuplicatedName(nickname, userAppointmentMappingList);
    }

    @Override
    @Transactional
    public void authenticateEmailForPasswordReset(String email) {
        userRepository.findByEmail(email).orElseThrow(() ->
                new UserException(ErrorCode.USER_NOT_FOUND));

        emailAuthRepository.findByEmail(email)
                .ifPresent(emailAuth -> emailAuthRepository.deleteByEmail(emailAuth.getEmail()));

        String authCode = Integer.toString(random.nextInt(888888) + 111111);
        LocalDateTime expiredTime = now().plusMinutes(5);

        EmailAuth emailAuth = EmailAuth.builder()
                .authCode(authCode)
                .email(email)
                .expiredAt(expiredTime)
                .build();

        emailAuthRepository.save(emailAuth);

        String subject = "[Gather Plan] 일반 회원 비밀번호 재설정을 위한 인증번호 발급";
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
            throw new BusinessException(ErrorCode.SERVICE_UNAVAILABLE, "이메일 전송에 실패했습니다.");
        }
    }

    @Override
    @Transactional
    public void resetPassword(String email, String authCode, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserException(ErrorCode.USER_NOT_FOUND));

        EmailAuth emailAuth = emailAuthRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.SERVICE_UNAVAILABLE, "해당 이메일로 전송된 인증번호가 없습니다."));

        if (now().isAfter(emailAuth.getExpiredAt())) {
            throw new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL, "만료된 인증입니다.");
        }

        if (!StringUtils.equals(authCode, emailAuth.getAuthCode())) {
            throw new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL, "인증번호가 일치하지 않습니다.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(password);

        user.updatePassword(encodedPassword);
    }
    @Override
    public UserInfoRespDto retrieveUserInfo(UserInfo userInfo) {
        return UserInfoRespDto.builder()
                .name(userInfo.getUsername())
                .email(userInfo.getEmail())
                .userAuthType(userInfo.getUserAuthType())
                .build();
    }

    @Override
    @Transactional
    public void deleteUser(UserInfo userInfo) {
        userAppointmentMappingRepository.deleteAllByUserSeqAndUserRole(userInfo.getId(), UserRole.GUEST);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByUserSeqAndUserRole(userInfo.getId(), UserRole.HOST);

        List<String> appointmentCodeList = userAppointmentMappingList.stream()
                .map(UserAppointmentMapping::getAppointmentCode)
                .toList();

        userAppointmentMappingRepository.deleteAllByAppointmentCodeIn(appointmentCodeList);

        appointmentRepository.deleteAllByAppointmentCodeIn(appointmentCodeList);

        userRepository.deleteById(userInfo.getId());
    }

    @Override
    @Transactional
    public void updateUser(String name, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        user.updateUser(name);
    }
}

