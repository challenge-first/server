package challenge.competer.domain.auction.repository;

import challenge.competer.domain.auction.entity.Auction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class AuctionRepositoryTest {

    @Autowired
    AuctionRepository auctionRepository;

    @BeforeEach
    void beforeEach() {
        for (int i = 0; i < 5; i++) {
            Auction auction = Auction.builder()
                    .openingTime(LocalDateTime.now().withHour(15).withMinute(0).withSecond(0).plusDays(i))
                    .closingTime(LocalDateTime.now().withHour(16).withMinute(59).withSecond(0).plusDays(i))
                    .productId(1L)
                    .memberId(1L)
                    .openingPrice(100L)
                    .winningPrice(100L)
                    .build();

            auctionRepository.save(auction);
        }
    }

    @Test
    @DisplayName("현재 시간 기준 참여가 가능한 경매 조회 테스트")
    public void findByCurrentTimeTest() throws Exception {
        //given, when
        Optional<Auction> byCurrentTime = auctionRepository.findByCurrentTime(LocalDateTime.now().withHour(15));

        //then
        assertThat(byCurrentTime.isPresent()).isTrue();
        assertThat(byCurrentTime.get().getOpeningTime()).isAfter(LocalDateTime.now().withHour(14).withMinute(59));
        assertThat(byCurrentTime.get().getClosingTime()).isBefore(LocalDateTime.now().withHour(17));
    }

    @Test
    @DisplayName("시작, 종료시간 기준 경매 조회 테스트")
    public void findByClosingTimeBetweenTest() throws Exception {

        Optional<Auction> byClosingTime = auctionRepository.findByClosingTimeBetween(LocalDateTime.now().withHour(15), LocalDateTime.now().withHour(16).withMinute(59));

        assertThat(byClosingTime.isPresent()).isTrue();
        assertThat(byClosingTime.get().getOpeningTime()).isAfter(LocalDateTime.now().withHour(14).withMinute(59));
        assertThat(byClosingTime.get().getClosingTime()).isBefore(LocalDateTime.now().withHour(17));
    }
}
