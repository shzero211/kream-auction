package site.shkrr.kreamAuction.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass //따로 Table 을 생성하지 않고 속성만 상속
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED) //PROTECTED 범위내에서 기본생성자 사용 설정
@EntityListeners(AuditingEntityListener.class) //Auditing 설정
public class BaseTimeEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
