package challenge.competer.domain.auction.service;

import challenge.competer.domain.auction.entity.Auction;
import challenge.competer.domain.auction.repository.AuctionRepository;
import challenge.competer.domain.image.entity.Image;
import challenge.competer.domain.member.entity.Member;
import challenge.competer.domain.member.repository.MemberRepository;
import challenge.competer.domain.member.role.Role;
import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.productenum.ProductState;
import challenge.competer.domain.product.productenum.ProductType;
import challenge.competer.domain.product.productenum.SubCategory;
import challenge.competer.domain.product.repository.ImageRepository;
import challenge.competer.domain.product.repository.ProductRepository;
import challenge.competer.global.auth.MemberDetails;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
public class AuctionSchedulerTest {

    @Autowired
    private ScheduledTaskHolder scheduledTaskHolder;

    @InjectMocks
    private AuctionServiceImpl auctionServiceImpl;

    @Test
    @DisplayName("Scheduler 등록여부 테스트")
    public void schedulerTest() {

        String methodName = auctionServiceImpl.getClass().getName() + ".checkAndCloseAuctions";
        String cron = "0 0 17 * * *";

        long count = scheduledTaskHolder.getScheduledTasks().stream()
                .filter(scheduledTask -> scheduledTask.getTask() instanceof CronTask)
                .map(scheduledTask -> (CronTask) scheduledTask.getTask())
                .filter(cronTask -> cronTask.getExpression().equals(cron) && cronTask.toString().equals(methodName))
                .count();

        Assertions.assertThat(count).isEqualTo(1L);
    }
}
