package site.shkrr.kreamAuction.service.encrypt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
/*
*  개인정보 보호를 위한 암호화 서비스
* */
@Service
public class EncryptService {

    @Value("${encrypt.secret_key}")
    private String secretKey;

    private static final String alg = "AES/CBC/PKCS5Padding";

    //plain text  ->  pain bytes  ->  encrypt  ->  encrypted bytes  ->  encrypted base64 text
    public String encryptAES256(String text){
        String key = secretKey.substring(0,32);//32byte
        String iv = key.substring(0, 16);//16byte
        byte[] encrypted=null;

        try {
            Cipher cipher = Cipher.getInstance(alg);

            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

            encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return Base64.getEncoder().encodeToString(encrypted);
    }

    //encrypted base64 text  ->  encrypted bytes  ->  decrypt  ->  plain bytes  ->  plain text
    public String decryptAES256(String cipherText){
        String key = secretKey.substring(0,32);//32byte
        String iv = key.substring(0, 16);//16byte
        String decryptedText=null;

        try {
            Cipher cipher = Cipher.getInstance(alg);

            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            decryptedText= new String(decrypted, "UTF-8");

        } catch (Exception e) {
            throw new RuntimeException("키 복호화 실패");
        }

        return decryptedText;
    }
}
