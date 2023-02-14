package site.shkrr.kreamAuction.service.certification;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.controller.dto.UserDto;
import site.shkrr.kreamAuction.domain.user.UserRepository;
import site.shkrr.kreamAuction.exception.user.EmailNotSignUpException;

@RequiredArgsConstructor
@Service
public class EmailCertificationService {
    private final JavaMailSender mailSender;
    private final RedisCertificationService redisCertificationService;
    @Value("${spring.mail.username}")
    private String sendFrom;
    private final UserRepository userRepository;

    @Transactional
    public void sendCertificationForPassword(String email) {
        String emailStr=String.valueOf(Utils.json.toMap(email).get("email"));
        //회원가입된 이메일인지 확인
        if(!isExistEmail(emailStr)){
            throw new EmailNotSignUpException("회원 가입된 이메일이 아닙니다.");
        }
        String certificationNum=Utils.random.makeRandomNum();
        String messageContent=makeMessage(certificationNum);
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(emailStr);
        message.setFrom(sendFrom);
        message.setSubject("[Kream-auction] 이메일 인증");
        message.setText(messageContent);
        mailSender.send(message);//이메일 전송

        //레디스에 저장
        redisCertificationService.saveCertificationNumForPassword(emailStr,certificationNum);
    }

    private boolean isExistEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public String makeMessage(String certificationNum){
        StringBuilder builder = new StringBuilder();
        builder.append("[Kream-Auction] 인증번호는 ");
        builder.append(certificationNum);
        builder.append("입니다. ");

        return builder.toString();
    }

    public void verifyCertificationNum(UserDto .VerifyCertificationForPasswordRequest requestDto) {
        redisCertificationService.verifyCertificationNumForPassword(requestDto);
    }
}
