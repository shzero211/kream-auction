package site.shkrr.kreamAuction.domain.brand;

import lombok.*;
import site.shkrr.kreamAuction.domain.BaseTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Brand extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String nameKor;

    @Column(unique = true)
    private String nameEng;

    private String imagePath;

    public void updateBrandImgPath(String imagePath) {
        this.imagePath=imagePath;
    }
}
