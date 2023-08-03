package challenge.competer.domain.event.controller;

import challenge.competer.domain.event.dto.ResponseEventDto;
import challenge.competer.domain.event.service.EventService;
import challenge.competer.global.response.ResponseDataDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class EventControllerTest {

    ResponseDataDto responseDataDto;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EventService eventService;

    @BeforeEach
    void beforeEach() {
        responseDataDto = new ResponseDataDto(ResponseEventDto.builder().build());
    }

    @Test
    @DisplayName("오픈되어있는 이벤트 조회 테스트")
    void getOpenEventTest() throws Exception {
        //given
        when(eventService.getOpenEvent()).thenReturn(responseDataDto);

        //when, then
        mockMvc.perform(get("/events"))
                .andDo(print())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").exists());
    }

}