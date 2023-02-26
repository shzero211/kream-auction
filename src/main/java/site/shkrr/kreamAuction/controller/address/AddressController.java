package site.shkrr.kreamAuction.controller.address;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.controller.dto.AddressDto.SaveRequest;
import site.shkrr.kreamAuction.domain.user.User;
import site.shkrr.kreamAuction.service.address.AddressService;

import javax.validation.Valid;
@RequestMapping("/address")
@RequiredArgsConstructor
@RestController
public class AddressController {
    private final AddressService addressService;

    @PostMapping("")
    public ResponseEntity save(@Valid @RequestBody SaveRequest requestDto, @AuthenticationPrincipal User loginUser){
        addressService.save(requestDto,loginUser);
        return Utils.response.of("주소 등록 성공");
    }
}
