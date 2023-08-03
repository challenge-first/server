package challenge.competer.domain.auction.controller;

import challenge.competer.domain.auction.dto.RequestAuctionDto;
import challenge.competer.domain.auction.dto.ResponseAuctionDto;
import challenge.competer.domain.auction.dto.ResponseWinningPriceDto;
import challenge.competer.domain.auction.service.AuctionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuctionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuctionService auctionService;

    private ResponseAuctionDto responseDto;

    @BeforeEach
    public void beforeEach() {
        responseDto = ResponseAuctionDto.builder()
                .openingPrice(100L)
                .openingTime(LocalDateTime.now().minusMinutes(1))
                .closingTime(LocalDateTime.now().plusHours(1))
                .imageUrl("url")
                .name("product")
                .winningPrice(100L)
                .build();
    }

    @Test
    public void getAuction() throws Exception {
        //given
        when(auctionService.getAuction())
                .thenReturn(responseDto);

        //when, then
        mockMvc.perform(get("/auctions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("data").exists())
                .andExpect(jsonPath("data.openingPrice").value(100L));
    }

    @Test
    @DisplayName("Auction bid Test")
    void bid() throws Exception {

        LocalDateTime time = LocalDateTime.now();
        RequestAuctionDto request = new RequestAuctionDto(10000L, time);

        ResponseWinningPriceDto responseWinningPriceDto = ResponseWinningPriceDto.builder()
                .winningPrice(10000L)
                .build();

        when(auctionService.bid(any(), any(), any()))
                .thenReturn(responseWinningPriceDto);

        mockMvc.perform(post("/auctions/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("data.winningPrice").value(10000L))
                .andExpect(jsonPath("data").exists());
    }
}