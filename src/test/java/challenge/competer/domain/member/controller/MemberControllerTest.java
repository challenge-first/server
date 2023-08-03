package challenge.competer.domain.member.controller;

import challenge.competer.domain.member.dto.RequestMemberLoginDto;
import challenge.competer.domain.member.dto.RequestMemberPointDto;
import challenge.competer.domain.member.dto.ResponseTokenDto;
import challenge.competer.domain.member.service.MemberService;
import challenge.competer.global.jwt.JwtProvider;
import challenge.competer.global.response.ResponseMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtProvider jwtProvider;

    @MockBean
    MemberService memberService;

    @DisplayName("로그인에 성공하여 AccessToken이 성공적으로 발급된다.")
    @Test
    public void login_success_test() throws Exception {
        //given
        RequestMemberLoginDto request = new RequestMemberLoginDto("username", "password");

        String requestBody = objectMapper.writeValueAsString(request);

        when(memberService.login(any()))
                .thenReturn(new ResponseTokenDto(jwtProvider.getHEADER_NAME(),"AccessToken"));

        //when
        mockMvc.perform(post("/members/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isOk())
                .andExpect(header().string(jwtProvider.getHEADER_NAME(),"AccessToken"))
                .andExpect(jsonPath("data").exists())
                .andExpect(jsonPath("$.data.message").value("로그인 완료"))
                .andExpect(jsonPath("$.data.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.statusMessage").value(HttpStatus.OK.toString()))
                .andDo(print());
    }

    @DisplayName("포인트 충전에 성공한다.")
    @Test
    public void addPoint_success_test() throws Exception {
        //given
        RequestMemberPointDto request = new RequestMemberPointDto(5000L);

        when(memberService.addPoint(any(), any()))
                .thenReturn(new ResponseMessageDto("충전 완료", HttpStatus.OK.value(), HttpStatus.OK.toString()));

        String requestBody = new ObjectMapper().writeValueAsString(request);

        //when
        mockMvc.perform(post("/members/point")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").exists())
                .andExpect(jsonPath("$.data.message").value("충전 완료"))
                .andExpect(jsonPath("$.data.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.statusMessage").value(HttpStatus.OK.toString()))
                .andDo(print());
    }
}