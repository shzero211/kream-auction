package site.shkrr.kreamAuction.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

@SpringBootTest
@ActiveProfiles("test")
class CertificationRedisRepositoryTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void redisInsertTest(){
        redisTemplate.opsForValue().set("id","1234", Duration.ofSeconds(180));

    }
}