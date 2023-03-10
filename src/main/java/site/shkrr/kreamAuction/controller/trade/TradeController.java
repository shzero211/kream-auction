package site.shkrr.kreamAuction.controller.trade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.controller.dto.TradeDto.BidRequest;
import site.shkrr.kreamAuction.domain.user.User;
import site.shkrr.kreamAuction.service.trade.TradeService;

import javax.validation.Valid;
@RequiredArgsConstructor
@RestController
public class TradeController {

    private final TradeService tradeService;

    //구매입찰
    public ResponseEntity purchaseBid(@AuthenticationPrincipal User loginUser, @Valid @RequestBody BidRequest requestDto){
        tradeService.purchaseBid(loginUser,requestDto);
        return Utils.response.of("구매 입찰 등록 성공");
    }

    //판매입찰
    public ResponseEntity salesBid(@AuthenticationPrincipal User loginUser, @Valid @RequestBody BidRequest requestDto){
        tradeService.salesBid(loginUser,requestDto);
        return Utils.response.of("판매 입찰 등록 성공");
    }
}
