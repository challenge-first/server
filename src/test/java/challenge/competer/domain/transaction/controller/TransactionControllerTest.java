package challenge.competer.domain.transaction.controller;

import challenge.competer.domain.transaction.dto.RequestTransactionDto;
import challenge.competer.domain.transaction.dto.ResponseTransactionDto;
import challenge.competer.domain.transaction.service.TransactionService;
import challenge.competer.global.response.ResponseDataDto;
import challenge.competer.global.response.ResponseMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TransactionService transactionService;

    @Test
    public void paymentTest() throws Exception {
        //given
        String requestJson = new ObjectMapper().writeValueAsString(new RequestTransactionDto(0));

        when(transactionService.payment(any(), any(), any()))
                .thenReturn(new ResponseMessageDto("결제 완료", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));

        // when, then
        mockMvc.perform(post("/payments/products/1")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("data").exists());
    }

    @Test
    public void getTransactionTest() throws Exception {
        //given
        when(transactionService.getTransaction(any(), any()))
                .thenReturn(new ResponseDataDto<>(new ResponseTransactionDto(10L, 10)));

        //when, then
        mockMvc.perform(get("/payments/products/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("data.currentPoint").value(0L))
                .andExpect(jsonPath("data").exists());
    }
}