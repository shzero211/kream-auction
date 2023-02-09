package site.shkrr.kreamAuction.domain.token.refreshToken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Getter
@Builder
@AllArgsConstructor
public class RefreshToken {
    @Id
    private String tokenKey;
    private String refreshToken;
}
