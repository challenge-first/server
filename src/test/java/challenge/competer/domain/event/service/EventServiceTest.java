package challenge.competer.domain.event.service;

import challenge.competer.domain.event.dto.ResponseEventDto;
import challenge.competer.domain.event.entity.Event;
import challenge.competer.domain.event.eventstatus.EventStatus;
import challenge.competer.domain.event.repository.CouponRepository;
import challenge.competer.domain.event.repository.EventRepository;
import challenge.competer.domain.image.entity.Image;
import challenge.competer.domain.member.entity.Member;
import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.repository.ImageRepository;
import challenge.competer.domain.product.repository.ProductRepository;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseDataDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static challenge.competer.domain.event.eventstatus.EventStatus.CLOSE;
import static challenge.competer.domain.event.eventstatus.EventStatus.OPEN;
import static challenge.competer.domain.product.productenum.SubCategory.LG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    List<Event> event = new ArrayList<>();
    Product product;
    Image image;

    @Mock
    EventRepository eventRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    ImageRepository imageRepository;

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    EventServiceImpl eventService;

    @BeforeEach
    void beforeEach() {
        product = Product.builder()
                .subCategory(LG)
                .price(100).build();
        image = Image.builder().build();
    }

    @Test
    @DisplayName("오픈되어있는 이벤트 조회 테스트")
    void getOpenEventTest() throws Exception {
        //given
        event.add(Event.builder().build());

        when(eventRepository.findOpenEvent(any(), any())).thenReturn(event);
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(imageRepository.findFirstByProductId(any())).thenReturn(Optional.of(image));

        //when
        ResponseDataDto<ResponseEventDto> findResult = eventService.getOpenEvent();

        //then
        assertThat(findResult.getData().getEventStatus()).isEqualTo(OPEN);
        assertThat(findResult.getData().getPrice()).isEqualTo(50);
    }

    @Test
    @DisplayName("이벤트 조회 테스트 - 오픈된 이벤트 없음")
    void getEventFailTest() throws Exception {
        //given
        when(eventRepository.findOpenEvent(any(), any())).thenReturn(event);

        //when, then
        assertThatThrownBy(() -> eventService.getOpenEvent())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("현재 진행중인 이벤트가 없습니다.");
    }

    @DisplayName("쿠폰 생성에 성공한다.")
    @Test
    public void createCoupon_O() throws Exception {
        //given
        Event event = Event.builder()
                .id(1L)
                .discountRate(30.0)
                .currentMemberCount(1L)
                .maxMemberCount(10L)
                .build();

        when(eventRepository.findById(event.getId()))
                .thenReturn(Optional.of(event));

        Member member = Member.builder()
                .id(1L)
                .build();
        MemberDetails memberDetails = new MemberDetails(member);

        when(couponRepository.countByEventId(event.getId()))
                .thenReturn(Optional.of(1L));

        //when
        eventService.createCoupon(1L, memberDetails);

        //then
        Optional<Long> savedCount = couponRepository.countByEventId(event.getId());
        verify(couponRepository,times(1)).save(any());
        Assertions.assertThat(savedCount.get()).isEqualTo(1L);
    }

    @DisplayName("최대 발급 가능한 쿠폰 개수보다 많은 쿠폰이 생성될 때 예외가 발생한다.")
    @Test
    public void couponCreate_X1() {
        //given
        Event event = Event.builder()
                .id(1L)
                .eventStatus(EventStatus.OPEN)
                .maxMemberCount(2L)
                .build();
        Member member = Member
                .builder()
                .id(1L)
                .build();
        MemberDetails memberDetails = new MemberDetails(member);
        when(eventRepository.findById(any()))
                .thenReturn(Optional.of(event));
        when(couponRepository.countByEventId(any()))
                .thenReturn(Optional.of(2L));

        //when
        //then
        assertThatThrownBy(() -> eventService.createCoupon(event.getId(), memberDetails))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("발급된 쿠폰의 개수가 현재 참여 인원수와 같지 않다면 예외가 발생한다.")
    @Test
    public void couponCreate_X2() {
        //given
        Event event = Event.builder()
                .id(1L)
                .eventStatus(EventStatus.OPEN)
                .currentMemberCount(3L)
                .maxMemberCount(5L)
                .build();
        Member member = Member
                .builder()
                .id(1L)
                .build();
        MemberDetails memberDetails = new MemberDetails(member);
        when(eventRepository.findById(any()))
                .thenReturn(Optional.of(event));
        when(couponRepository.countByEventId(any()))
                .thenReturn(Optional.of(2L));

        //when
        //then
        assertThatThrownBy(() -> eventService.createCoupon(event.getId(), memberDetails))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("현재 인원과 최대 인원이 같아지면 이벤트 종료 / 종료시간 변경")
    public void equalToCurrentMember() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        Event event = Event.builder()
                .id(1L)
                .eventStatus(EventStatus.OPEN)
                .currentMemberCount(1L)
                .maxMemberCount(2L)
                .closingTime(now.plusHours(1))
                .build();

        Member member = Member
                .builder()
                .id(1L)
                .build();

        MemberDetails memberDetails = new MemberDetails(member);

        when(eventRepository.findById(any()))
                .thenReturn(Optional.of(event));
        when(couponRepository.countByEventId(any()))
                .thenReturn(Optional.of(1L));

        //when
        eventService.createCoupon(event.getId(), memberDetails);

        //then
        assertThat(event.getEventStatus()).isEqualTo(CLOSE);
    }
}