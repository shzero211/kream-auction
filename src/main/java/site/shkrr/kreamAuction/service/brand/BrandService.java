package site.shkrr.kreamAuction.service.brand;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.shkrr.kreamAuction.controller.dto.BrandDto.CreateRequest;
import site.shkrr.kreamAuction.domain.brand.Brand;
import site.shkrr.kreamAuction.domain.brand.BrandRepository;
import site.shkrr.kreamAuction.exception.brand.DuplicateBrandNameException;
import site.shkrr.kreamAuction.service.storage.AwsS3Service;
@Slf4j
@RequiredArgsConstructor
@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final AwsS3Service awsS3Service;
    @Transactional
    public void createBrand(CreateRequest requestDto, MultipartFile multipartFile){

        Brand brand=Brand.builder()
                .nameKor(requestDto.getNameKor())
                .nameEng(requestDto.getNameEng())
                .build();

        if(isDuplicatedName(requestDto)){//브랜드 이름 중복 검사
            throw new DuplicateBrandNameException("이미 중복되는 브랜드 이름이 존재합니다.");
        }

        if(multipartFile!=null){//이미지 파일이 있을시
            String brandImgPath=awsS3Service.uploadBrandImg(multipartFile);
            brand.updateBrandImgPath(brandImgPath);
        }

        brandRepository.save(brand);
    }

    private boolean isDuplicatedName(CreateRequest requestDto) {
        if(brandRepository.existsByNameKor(requestDto.getNameKor())){
            return true;
        }
        if(brandRepository.existsByNameEng(requestDto.getNameEng())){
            return true;
        }
        return false;
    }

}
