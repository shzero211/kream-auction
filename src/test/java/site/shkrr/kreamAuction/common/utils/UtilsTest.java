package site.shkrr.kreamAuction.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import site.shkrr.kreamAuction.controller.dto.PaymentMethodDto;
import site.shkrr.kreamAuction.controller.dto.PaymentMethodDto.BillingResponse;

@SpringBootTest
class UtilsTest {

    @Test
    @DisplayName("Utils.json.toObj 테스트")
    public void jsonToObjUtilTest(){
        String json="{\"mId\":\"m\",\"customerKey\":\"ck\",\"authenticatedAt\":\"2023-02-27T17:04:12+09:00\",\"method\":\"카드\",\"billingKey\":\"billingKey\",\"cardCompany\":\"우리\",\"cardNumber\":\"94442034****947*\",\"card\":{\"company\":\"우리\",\"issuerCode\":\"33\",\"acquirerCode\":\"31\",\"number\":\"94442034****947*\",\"cardType\":\"체크\",\"ownerType\":\"개인\"}}";
        BillingResponse billingResponse =Utils.json.toObj(json,BillingResponse.class);
        Assertions.assertEquals("ck",billingResponse.getCustomerKey());
        Assertions.assertEquals("billingKey",billingResponse.getBillingKey());
    }

}