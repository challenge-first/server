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

    @Transactional
    @Override
    public ResponseWinningPriceDto bid(Long auctionId, RequestAuctionDto requestAuctionDto, MemberDetails memberDetails) {

        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new IllegalArgumentException("상품이 없습니다"));
        if (requestAuctionDto.getPoint() >= auction.getOpeningPrice() && requestAuctionDto.getPoint() >= auction.getWinningPrice()) {
            auction.update(requestAuctionDto.getPoint());
            return createResponseWinningPriceDto(auction);
        }

    }

    private static ResponseWinningPriceDto createResponseWinningPriceDto(Auction auction) {
        return ResponseWinningPriceDto.builder()
                .winningPrice(auction.getWinningPrice())
                .build();
    }


}
