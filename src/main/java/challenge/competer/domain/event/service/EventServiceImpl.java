package challenge.competer.domain.event.service;

import challenge.competer.domain.event.dto.ResponseEventDto;
import challenge.competer.domain.event.entity.Coupon;
import challenge.competer.domain.event.entity.Event;
import challenge.competer.domain.event.repository.CouponRepository;
import challenge.competer.domain.event.repository.EventRepository;
import challenge.competer.domain.image.entity.Image;
import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.repository.ImageRepository;
import challenge.competer.domain.product.repository.ProductRepository;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseDataDto;
import challenge.competer.global.response.ResponseMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static challenge.competer.domain.event.eventstatus.EventStatus.*;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final CouponRepository couponRepository;
    private final EventRepository eventRepository;
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public ResponseDataDto<ResponseEventDto> getOpenEvent() {
        List<Event> event = findOneEvent();

        validateEmptyEvent(event);

        ResponseEventDto responseEventDto = event.stream()
                .map(this::createResponseEventDto)
                .toList()
                .get(0);

        return new ResponseDataDto<>(responseEventDto);

    }

    @Transactional
    @Override
    public ResponseDataDto<ResponseMessageDto> createCoupon(Long eventId, MemberDetails memberDetails) {

        Long couponCount = couponRepository.countByEventId(eventId).orElseThrow();
        Event currentEvent = eventRepository.findById(eventId).orElseThrow();

        if (couponCount >= currentEvent.getMaxMemberCount() || couponCount != currentEvent.getCurrentMemberCount()) {
            throw new IllegalArgumentException("Can't create Coupon");
        }

        Coupon coupon = Coupon.builder()
                .eventId(currentEvent.getId())
                .memberId(memberDetails.getId())
                .discountRate(currentEvent.getDiscountRate())
                .build();

        couponRepository.save(coupon);
        currentEvent.updateCurrentMember();

        ResponseMessageDto responseMessageDto = new ResponseMessageDto("쿠폰 발급 성공", HttpStatus.OK.value(), HttpStatus.OK.toString());
        return new ResponseDataDto<>(responseMessageDto);
    }

    private void validateEmptyEvent(List<Event> event) {
        if (event.isEmpty()) {
            throw new IllegalArgumentException("현재 진행중인 이벤트가 없습니다.");
        }
    }

    private ResponseEventDto createResponseEventDto(Event eventItem) {
        Product product = productRepository.findById(eventItem.getProductId()).orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));
        Image image = imageRepository.findFirstByProductId(product.getId()).orElseThrow(() -> new IllegalArgumentException("해당 이미지가 없습니다."));
        Integer eventPrice = product.getPrice() / 2;

        return ResponseEventDto.builder()
                .name(product.getName())
                .price(eventPrice)
                .imageUrl(image.getImageUrl())
                .maxMemberCount(eventItem.getMaxMemberCount())
                .currentMemberCount(eventItem.getCurrentMemberCount())
                .openingTime(eventItem.getOpeningTime())
                .closingTime(eventItem.getClosingTime())
                .productStock(eventItem.getProductStock())
                .eventStatus(OPEN)
                .build();
    }

    private List<Event> findOneEvent() {
        LocalDateTime currentTime = LocalDateTime.now();
        PageRequest limit = PageRequest.of(0, 1);

        return eventRepository.findOpenEvent(currentTime, limit);
    }
}
