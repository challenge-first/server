package challenge.competer.domain.auction.service;

import challenge.competer.domain.auction.dto.ResponseAuctionDto;
import challenge.competer.domain.auction.entity.Auction;
import challenge.competer.domain.auction.repository.AuctionRepository;
import challenge.competer.domain.image.entity.Image;
import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.repository.ImageRepository;
import challenge.competer.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
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
                .name(product.getName())
                .winningPrice(auction.getWinningPrice())
                .openingPrice(auction.getOpeningPrice())
                .openingTime(auction.getOpeningTime())
                .imageUrl(image.getImageUrl())
                .closingTime(auction.getClosingTime())
                .build();

        return responseDto;
    }
}
