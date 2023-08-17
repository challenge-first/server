package challenge.competer.domain.auction.repository;

import challenge.competer.domain.auction.entity.Auction;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuctionRepositoryTest {

    @Autowired
    AuctionRepository auctionRepository;

    @BeforeEach
    void beforeEach() {
        for (int i = 0; i < 5; i++) {
            Auction auction = Auction.builder()
                    .id((long) i + 1)
                    .openingTime(LocalDateTime.now().withHour(15).plusDays(i))
                    .closingTime(LocalDateTime.now().withHour(16).withMinute(59).plusDays(i))
                    .productId(1L)
                    .memberId(1L)
                    .openingPrice(100L)
                    .winningPrice(100L)
                    .build();

            auctionRepository.save(auction);
        }
    }

    @AfterEach
    void afterEach() {
        auctionRepository.deleteAll();
    }

    @Test
    @DisplayName("현재 시간 기준 opening, closing 사이 경매 조회 테스트")
    public void findByCurrentTimeTest() throws Exception {
        //given, when
        Optional<Auction> byCurrentTime = auctionRepository.findByCurrentTime(LocalDateTime.now().withHour(15));

        //then
        assertThat(byCurrentTime.isPresent()).isTrue();
        assertThat(byCurrentTime.get().getId()).isEqualTo(1L);
        assertThat(byCurrentTime.get().getOpeningTime()).isAfter(LocalDateTime.now().withHour(14).withMinute(59));
        assertThat(byCurrentTime.get().getClosingTime()).isBefore(LocalDateTime.now().withHour(17));
    }

    @Test
    @DisplayName("opening, closing 사이 경매 조회 테스트")
    public void findByClosingTimeBetweenTest() throws Exception {

        Optional<Auction> byClosingTime = auctionRepository.findByClosingTimeBetween(LocalDateTime.now().withHour(15), LocalDateTime.now().withHour(16).withMinute(59));

        assertThat(byClosingTime.isPresent()).isTrue();
        assertThat(byClosingTime.get().getId()).isEqualTo(1L);
        assertThat(byClosingTime.get().getOpeningTime()).isAfter(LocalDateTime.now().withHour(14).withMinute(59));
        assertThat(byClosingTime.get().getClosingTime()).isBefore(LocalDateTime.now().withHour(17));
    }
}
