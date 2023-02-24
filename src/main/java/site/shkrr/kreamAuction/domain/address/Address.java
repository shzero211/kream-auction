package site.shkrr.kreamAuction.domain.address;

import lombok.*;
import site.shkrr.kreamAuction.domain.BaseTimeEntity;
import site.shkrr.kreamAuction.domain.user.User;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Address extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private String phoneNum;

    private String zipCode;

    private String roadNameAddress;

    private String detailAddress;

}
