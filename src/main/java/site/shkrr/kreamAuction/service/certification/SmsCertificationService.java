package site.shkrr.kreamAuction.service.certification;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.domain.redis.CertificationRedisRepository;
import site.shkrr.kreamAuction.exception.smsCertification.CertificationNumNotMatchException;

@Slf4j
@Service
public class SmsCertificationService {
    private static final String fromNum="01039229957";
    private final DefaultMessageService messageService;
    private final CertificationRedisRepository certificationRedisRepository;

    /*
    * 생성자로 sms 서비스를 위한 api_key 가진 객체 서비스 주입
    * */
    public SmsCertificationService(@Value("${sms.api_key}") String apiKey, @Value("${sms.secret_key}") String secretKey, CertificationRedisRepository certificationRedisRepository){
        this.messageService= NurigoApp.INSTANCE.initialize(apiKey,secretKey, "https://api.coolsms.co.kr");
        this.certificationRedisRepository = certificationRedisRepository;
    }

    /*
    *인증 메세지 생성
    *  */
    public String makeMessage(String certificationNumber){
        StringBuilder builder = new StringBuilder();
        builder.append("[Kream-Auction] 인증번호는 ");
        builder.append(certificationNumber);
        builder.append(" 입니다. ");

        return builder.toString();
    }

    /*
    * 인증 메세지 전송
    * */
    public String sendTo(String phoneNum){
        Message message =new Message();
        String certificationNum=Utils.random.makeRandomNum();
        String certificationMessage=makeMessage(certificationNum);
        message.setFrom(fromNum);
        message.setTo(phoneNum);
        message.setText(certificationMessage);
        //SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));//메세지 발송
        log.info(String.valueOf(certificationNum));// 요금 방지를 위해 info 로 인증 번호 발급
        certificationRedisRepository.save(phoneNum,certificationNum);
        return certificationNum;
    }

    /*
    * 인증 메세지 번호 검증
    * */
    public void verifyNum(String phoneNum,String certificationNum){
        if(!(certificationRedisRepository.hasKey(phoneNum)&& certificationRedisRepository.verify(phoneNum,certificationNum))){
            throw new CertificationNumNotMatchException("인증번호가 일치하지 않습니다.");
        }
    }

}
