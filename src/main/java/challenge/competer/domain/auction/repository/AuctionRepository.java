package challenge.competer.domain.auction.repository;

import challenge.competer.domain.auction.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Query("select a from Auction a where a.closingTime > :current_time and a.openingTime <= :current_time order by a.id desc limit 1")
    Optional<Auction> findByCurrentTime(@Param("current_time") LocalDateTime currentTime );
}
