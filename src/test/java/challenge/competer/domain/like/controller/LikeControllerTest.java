package challenge.competer.domain.like.controller;

import challenge.competer.domain.like.service.LikeService;
import challenge.competer.global.response.ResponseMessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class LikeControllerTest {

    ResponseMessageDto createLikeResponseMessageDto;
    ResponseMessageDto cencelLikeResponseMessageDto;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LikeService likeService;

    @BeforeEach
    void beforeEach() {
        createLikeResponseMessageDto = new ResponseMessageDto("좋아요 완료", 200, "OK");
        cencelLikeResponseMessageDto = new ResponseMessageDto("좋아요 취소", 200, "OK");
    }

    @Test
    @DisplayName("좋아요 등록 테스트")
    void toggleLikeTest() throws Exception {
        //given
        when(likeService.toggleLike(any(), any()))
                .thenReturn(createLikeResponseMessageDto);

        //when, then
        mockMvc.perform(post("/likes/products/2")
                        .contentType(APPLICATION_JSON)
                        .header("productId", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").exists())
                .andExpect(jsonPath("data.message").value("좋아요 완료"))
                .andExpect(jsonPath("data.statusCode").value(200))
                .andExpect(jsonPath("data.statusMessage").value("OK"));
    }

    @Test
    @DisplayName("좋아요 취소 테스트")
    void toggleLikeCancelTest() throws Exception {
        //given
        when(likeService.toggleLike(any(), any()))
                .thenReturn(cencelLikeResponseMessageDto);

        //when, then
        mockMvc.perform(post("/likes/products/2")
                        .contentType(APPLICATION_JSON)
                        .header("productId", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").exists())
                .andExpect(jsonPath("data.message").value("좋아요 취소"))
                .andExpect(jsonPath("data.statusCode").value(200))
                .andExpect(jsonPath("data.statusMessage").value("OK"));
    }

}