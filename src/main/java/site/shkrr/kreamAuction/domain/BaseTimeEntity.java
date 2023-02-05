package site.shkrr.kreamAuction.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@SuperBuilder //부모클래스 속성 빌더를 생성할수있도록 설정
@MappedSuperclass //따로 Table 을 생성하지 않고 속성만 상속
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED) //PROTECTED 범위내에서 기본생성자 사용 설정
@EntityListeners(AuditingEntityListener.class) //Auditing 설정
public class BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
