package challenge.competer.domain.auction.controller;

import challenge.competer.domain.auction.dto.RequestAuctionDto;
import challenge.competer.domain.auction.dto.ResponseAuctionDto;
import challenge.competer.domain.auction.dto.ResponseWinningPriceDto;
import challenge.competer.domain.auction.service.AuctionService;
import challenge.competer.global.auth.LoginMember;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @GetMapping("/auctions")
    public ResponseEntity<ResponseDataDto> getAuction() {

        ResponseDataDto<ResponseAuctionDto> response = new ResponseDataDto<>(auctionService.getAuction());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/auctions/{auctionId}")
    public ResponseEntity<ResponseDataDto> bid(@PathVariable Long auctionId,
                                               @RequestBody RequestAuctionDto requestAuctionDto,
                                               @LoginMember MemberDetails memberDetails) {

        ResponseWinningPriceDto responseWinningPriceDto = auctionService.bid(auctionId, requestAuctionDto, memberDetails);
        ResponseDataDto responseDataDto = new ResponseDataDto(responseWinningPriceDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }
}
