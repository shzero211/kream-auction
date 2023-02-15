package site.shkrr.kreamAuction.service.certification;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.controller.dto.UserDto;
import site.shkrr.kreamAuction.domain.redis.CertificationRedisRepository;
import site.shkrr.kreamAuction.exception.user.EmailNotSignUpException;
import site.shkrr.kreamAuction.service.user.UserService;

@RequiredArgsConstructor
@Service
public class EmailCertificationService {
    private final JavaMailSender mailSender;
    private final CertificationRedisRepository certificationRedisRepository;
    @Value("${spring.mail.username}")
    private String sendFrom;
    private final UserService userService;

    /*
    * 비밀 번호 찾기시 이메일로 인증번호 전송+레디스에 저장
    * */
    @Transactional
    public void sendCertificationForPassword(String email) {
        String emailStr=String.valueOf(Utils.json.toMap(email).get("email"));
        //회원가입된 이메일인지 확인
        if(!userService.isExistEmail(emailStr)){
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
        certificationRedisRepository.save(emailStr,certificationNum);
    }


    public String makeMessage(String certificationNum){
        StringBuilder builder = new StringBuilder();
        builder.append("[Kream-Auction] 인증번호는 ");
        builder.append(certificationNum);
        builder.append("입니다. ");

        return builder.toString();
    }

    /*
    * 이메일 인증번호 검증후 삭제
    * */
    public void verifyCertificationNum(UserDto .VerifyCertificationForPasswordRequest requestDto) {
        certificationRedisRepository.verify(requestDto);
    }
}
