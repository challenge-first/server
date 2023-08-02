package challenge.competer.domain.auction.controller;

import challenge.competer.domain.auction.dto.ResponseAuctionDto;
import challenge.competer.domain.auction.service.AuctionService;
import challenge.competer.global.response.ResponseDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @GetMapping("/auctions")
    public ResponseEntity<ResponseDataDto> getAuction() {

        ResponseDataDto<ResponseAuctionDto> response = new ResponseDataDto<>(auctionService.getAuction());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
