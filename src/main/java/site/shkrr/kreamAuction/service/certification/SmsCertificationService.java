package site.shkrr.kreamAuction.service.certification;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.shkrr.kreamAuction.common.utils.Utils;
@Slf4j
@Service
public class SmsCertificationService {
    private static final String fromNum="01039229957";
    private final DefaultMessageService messageService;

    public SmsCertificationService(@Value("${sms.api_key}") String apiKey,@Value("${sms.secret_key}") String secretKey){
        this.messageService= NurigoApp.INSTANCE.initialize(apiKey,secretKey, "https://api.coolsms.co.kr");
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
    public void sendTo(String phoneNum){
        Message message =new Message();
        String certificationNum=makeMessage(Utils.random.makeRandomNum());
        message.setFrom(fromNum);
        message.setTo(phoneNum);
        message.setText(certificationNum);
        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));//메세지 발송

        log.info(String.valueOf(response));
    }

}
