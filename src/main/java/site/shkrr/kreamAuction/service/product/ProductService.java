package site.shkrr.kreamAuction.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.shkrr.kreamAuction.controller.dto.BrandDto.BrandInfo;
import site.shkrr.kreamAuction.controller.dto.ProductDto.CreateRequest;
import site.shkrr.kreamAuction.domain.product.ProductRepository;
import site.shkrr.kreamAuction.exception.brand.BrandNotFoundException;
import site.shkrr.kreamAuction.service.brand.BrandService;
import site.shkrr.kreamAuction.service.storage.AwsS3Service;
import site.shkrr.kreamAuction.service.storage.common.ImageType;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final BrandService brandService;
    private final ProductRepository productRepository;
    private final AwsS3Service awsS3Service;
    @Transactional
    public void createProduct(CreateRequest requestDto, MultipartFile productImg) {

        if(!isBrandExist(requestDto.getBrand())){//존재하는 브랜드 인지 확인
            throw new BrandNotFoundException("해당 브랜드는 존재 하지않습니다.");
        }

        if(productImg!=null){//이미지 파일이 존재하는지 확인
            String imagePath=awsS3Service.uploadImg(productImg, ImageType.PRODUCT);
            requestDto.updateImgPath(imagePath);
        }

        productRepository.save(requestDto.toEntity());
    }


    private boolean isBrandExist(BrandInfo brand) {
        return brandService.isBrandExist(brand);
    }
}
