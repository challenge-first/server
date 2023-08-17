package challenge.competer.domain.auction.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskHolder;
import org.springframework.transaction.annotation.Transactional;

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
