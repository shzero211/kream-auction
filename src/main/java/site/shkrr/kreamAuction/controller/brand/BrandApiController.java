package site.shkrr.kreamAuction.controller.brand;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.controller.dto.BrandDto.CreateRequest;
import site.shkrr.kreamAuction.service.brand.BrandService;

import javax.validation.Valid;
@RequiredArgsConstructor
@RequestMapping("/brand")
@RestController
public class BrandApiController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity createBrand(@Valid @RequestPart CreateRequest requestDto, @RequestPart(required = false)MultipartFile brandImg){
        brandService.createBrand(requestDto,brandImg);
        return Utils.response.of("브랜드 생성 성공");
    }
}
