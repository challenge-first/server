package challenge.competer.domain.event.service;

import challenge.competer.domain.event.dto.ResponseEventDto;
import challenge.competer.domain.event.entity.Event;
import challenge.competer.domain.event.repository.EventRepository;
import challenge.competer.domain.image.entity.Image;
import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.repository.ImageRepository;
import challenge.competer.domain.product.repository.ProductRepository;
import challenge.competer.global.response.ResponseDataDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static challenge.competer.domain.event.eventstatus.EventStatus.*;
import static challenge.competer.domain.product.productenum.SubCategory.*;
import static org.assertj.core.api.Assertions.*;
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

}