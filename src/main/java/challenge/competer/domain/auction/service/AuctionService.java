package challenge.competer.domain.auction.service;

import challenge.competer.domain.auction.dto.RequestAuctionDto;
import challenge.competer.domain.auction.dto.ResponseWinningPriceDto;
import challenge.competer.global.auth.MemberDetails;

public interface AuctionService {

    ResponseWinningPriceDto bid(Long auctionId, RequestAuctionDto requestAuctionDto, MemberDetails memberDetails);
}
