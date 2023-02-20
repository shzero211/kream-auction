package site.shkrr.kreamAuction.domain.brand;

import lombok.*;
import lombok.experimental.SuperBuilder;
import site.shkrr.kreamAuction.domain.BaseTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Brand extends BaseTimeEntity {
    @Column(unique = true)
    private String nameKor;

    @Column(unique = true)
    private String nameEng;

    private String imagePath;

    public void updateBrandImgPath(String imagePath) {
        this.imagePath=imagePath;
    }
}
