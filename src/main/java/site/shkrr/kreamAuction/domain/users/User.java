package site.shkrr.kreamAuction.domain.users;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;
import site.shkrr.kreamAuction.domain.BaseTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Table(name = "Users")
@Entity
public class User extends BaseTimeEntity {
    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String phoneNum;

    @Enumerated
    private Role role;
}
