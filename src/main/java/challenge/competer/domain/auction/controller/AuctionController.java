package challenge.competer.domain.auction.controller;

import challenge.competer.domain.auction.dto.RequestAuctionDto;
import challenge.competer.domain.auction.dto.ResponseWinningPriceDto;
import challenge.competer.domain.auction.service.AuctionService;
import challenge.competer.global.auth.LoginMember;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping("/auctions/{auctionId}")
    public ResponseEntity<ResponseDataDto> bid(@PathVariable Long auctionId,
                                               @RequestBody RequestAuctionDto requestAuctionDto,
                                               @LoginMember MemberDetails memberDetails) {

        ResponseWinningPriceDto responseWinningPriceDto = auctionService.bid(auctionId, requestAuctionDto, memberDetails);
        ResponseDataDto responseDataDto = new ResponseDataDto(responseWinningPriceDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }
}
