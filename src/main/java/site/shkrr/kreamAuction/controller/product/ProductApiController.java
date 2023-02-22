package site.shkrr.kreamAuction.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.controller.dto.ProductDto.CreateRequest;
import site.shkrr.kreamAuction.service.product.ProductService;

import javax.validation.Valid;
@RequiredArgsConstructor
@RequestMapping("/product")
@RestController
public class ProductApiController {
    private final ProductService productService;
    @PostMapping
    public ResponseEntity createProduct(@Valid @RequestPart CreateRequest requestDto, @RequestPart(required = false)MultipartFile productImg){
        productService.createProduct(requestDto,productImg);
        return Utils.response.of("상품 생성 성공");
    }
}
