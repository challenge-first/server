package challenge.competer.domain.auction.repository;

import challenge.competer.domain.auction.entity.Auction;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuctionRepositoryTest {

    @Autowired
    AuctionRepository auctionRepository;

    @BeforeEach
    void beforeAll() {
        for (int i = 0; i < 5; i++) {
            Auction auction = Auction.builder()
                    .id((long) i + 1)
                    .openingTime(LocalDateTime.now().minusMinutes(1).plusHours(i))
                    .closingTime(LocalDateTime.now().plusHours(1 + i))
                    .productId(1L)
                    .memberId(1L)
                    .openingPrice(100L)
                    .winningPrice(100L)
                    .build();

            auctionRepository.save(auction);
        }
    }

    @Test
    @DisplayName("현재 시간 기준 opening, closing 사이 값 조회 테스트")
    public void findByCurrentTimeTest() throws Exception {
        //given, when
        Optional<Auction> byCurrentTime = auctionRepository.findByCurrentTime(LocalDateTime.now());

        //then
        assertThat(byCurrentTime.isPresent()).isTrue();
        assertThat(byCurrentTime.get().getId()).isEqualTo(1L);
        assertThat(byCurrentTime.get().getClosingTime()).isAfter(LocalDateTime.now());
        assertThat(byCurrentTime.get().getOpeningTime()).isBefore(LocalDateTime.now());
    }
}
