package site.shkrr.kreamAuction.service.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.shkrr.kreamAuction.controller.dto.BrandDto;
import site.shkrr.kreamAuction.controller.dto.ProductDto;
import site.shkrr.kreamAuction.domain.brand.Brand;
import site.shkrr.kreamAuction.domain.product.Product;
import site.shkrr.kreamAuction.domain.product.common.Color;
import site.shkrr.kreamAuction.domain.product.common.ReleasePriceType;
import site.shkrr.kreamAuction.service.brand.BrandService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductServiceTest {
    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    @Test
    public void createProductTest() throws IOException {
        //브랜드 요청생성
        BrandDto.CreateRequest brandRequest= BrandDto.CreateRequest.builder()
                .nameEng("converse")
                .nameKor("컨버스")
                .build();
        //브랜드 이미지 생성
        BufferedImage brandImage=new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB );
        ByteArrayOutputStream baoB=new ByteArrayOutputStream();
        ImageIO.write(brandImage,"PNG",baoB);
        byte[] bytesB=baoB.toByteArray();
        MockMultipartFile brandMockFile=new MockMultipartFile("converse.png","converse.png",MediaType.MULTIPART_FORM_DATA.toString(),bytesB);
        //브랜드 생성
        Brand brand=brandService.createBrand(brandRequest,brandMockFile);

        //상품 요청 생성
        ProductDto.CreateRequest productRequest= ProductDto.CreateRequest.builder()
                .brand(brand.toBrandInfo())
                .nameEng("Converse Chuck 70 Ox Black")
                .nameKor("컨버스 척 70 로우 블랙")
                .modelNum("162058C")
                .sizeGap(10)
                .maxSize(290)
                .minSize(270)
                .releasePriceType(ReleasePriceType.KRW)
                .releasePrice(69000L)
                .releaseDate(LocalDate.now())
                .color(Color.BLACK)
                .build();

        //상품 이미지 생성
        BufferedImage productImage=new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB );
        ByteArrayOutputStream baoP=new ByteArrayOutputStream();
        ImageIO.write(brandImage,"PNG",baoP);
        byte[] bytesP=baoP.toByteArray();
        MockMultipartFile productMockFile=new MockMultipartFile("converse.png","converse.png",MediaType.MULTIPART_FORM_DATA.toString(),bytesP);

        //상품 생성
        Product product=productService.createProduct(productRequest,productMockFile);

        Assertions.assertEquals("BLACK",product.getColor().toString());

    }

}