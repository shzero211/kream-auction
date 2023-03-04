package site.shkrr.kreamAuction.service.encrypt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EncryptServiceTest {

    @Autowired
    private EncryptService encryptService;

    @Test
    @DisplayName("Utils.json.toObj 테스트")
    public void keyEncryptAndDecrypt() throws Exception {
        String text="비밀이지롱";
        String encryptText=encryptService.encryptAES256(text);

        Assertions.assertNotEquals(text,encryptText);
        System.out.println("encryptText:"+encryptText);

        String decryptText=encryptService.decryptAES256(encryptText);

        Assertions.assertEquals(text,decryptText);
        System.out.println("decryptText:"+decryptText);

    }
}