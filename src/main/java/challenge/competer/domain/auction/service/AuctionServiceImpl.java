package challenge.competer.domain.auction.service;

import challenge.competer.domain.auction.dto.RequestAuctionDto;
import challenge.competer.domain.auction.dto.ResponseAuctionDto;
import challenge.competer.domain.auction.dto.ResponseWinningPriceDto;
import challenge.competer.domain.auction.entity.Auction;
import challenge.competer.domain.auction.repository.AuctionRepository;
import challenge.competer.domain.image.entity.Image;
import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.repository.ImageRepository;
import challenge.competer.domain.product.repository.ProductRepository;
import challenge.competer.global.auth.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    @Override
    public ResponseAuctionDto getAuction() {

        Auction auction = auctionRepository.findByCurrentTime(LocalDateTime.now()).orElseThrow(
                () -> new IllegalArgumentException("현재 진행중인 경매가 없습니다."));

        Product product = productRepository.findById(auction.getProductId()).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 없습니다."));

        Image image = imageRepository.findFirstByProductId(product.getId()).orElseThrow(
                () -> new IllegalArgumentException("이미지가 없습니다."));

        ResponseAuctionDto responseDto = ResponseAuctionDto.builder()
                .id(auction.getId())
                .name(product.getName())
                .winningPrice(auction.getWinningPrice())
                .openingPrice(auction.getOpeningPrice())
                .openingTime(auction.getOpeningTime())
                .imageUrl(image.getImageUrl())
                .closingTime(auction.getClosingTime())
                .build();

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseWinningPriceDto bid(Long auctionId, RequestAuctionDto requestAuctionDto, MemberDetails memberDetails) {

        Auction findAuction = auctionRepository.findById(auctionId).orElseThrow(() -> new IllegalArgumentException("상품이 없습니다"));

        validateAuctionCondition(requestAuctionDto, findAuction);
        findAuction.update(requestAuctionDto.getPoint());

        return createResponseWinningPriceDto(findAuction);
    }

    private ResponseWinningPriceDto createResponseWinningPriceDto(Auction auction) {
        return ResponseWinningPriceDto.builder()
                .winningPrice(auction.getWinningPrice())
                .build();
    }

    private void validateAuctionCondition(RequestAuctionDto requestAuctionDto, Auction auction) {
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


}
