package site.shkrr.kreamAuction.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapUtilTest {
    @Test
    @DisplayName(value = "map 생성 메서드 테스트")
    public void makeMapTest(){
         Map<String,String> map=MapUtil.of("내생일","123");
        Assertions.assertEquals(String.class.getName(),map.get("내생일").getClass().getName());
        Assertions.assertEquals(true,map.get("내생일") instanceof String);
    }
}