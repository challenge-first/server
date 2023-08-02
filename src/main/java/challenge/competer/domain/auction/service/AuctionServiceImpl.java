package challenge.competer.domain.auction.service;

import challenge.competer.domain.auction.dto.RequestAuctionDto;
import challenge.competer.domain.auction.dto.ResponseWinningPriceDto;
import challenge.competer.domain.auction.entity.Auction;
import challenge.competer.domain.auction.repository.AuctionRepository;
import challenge.competer.global.auth.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;

    @Override
    @Transactional
    public ResponseWinningPriceDto bid(Long auctionId, RequestAuctionDto requestAuctionDto, MemberDetails memberDetails) {

        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new IllegalArgumentException("상품이 없습니다"));

        validateAuctionCondition(requestAuctionDto, auction);
        auction.update(requestAuctionDto.getPoint());

        return createResponseWinningPriceDto(auction);
    }

    private static void validateAuctionCondition(RequestAuctionDto requestAuctionDto, Auction auction) {
        if (requestAuctionDto.getPoint() < auction.getOpeningPrice()) {
            throw new IllegalArgumentException("기본 입찰가보다 부족한 입찰 금액입니다");
        }

        if (requestAuctionDto.getPoint() < auction.getWinningPrice()) {
            throw new IllegalArgumentException("현재 입찰가보다 부족한 입찰 금액입니다");
        }

        if (requestAuctionDto.getTime().isAfter(auction.getClosingTime())) {
            throw new IllegalStateException("경매가 종료되었습니다");
        }
    }

    private static ResponseWinningPriceDto createResponseWinningPriceDto(Auction auction) {
        return ResponseWinningPriceDto.builder()
                .winningPrice(auction.getWinningPrice())
                .build();
    }


}
