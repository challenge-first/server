package challenge.competer.domain.auction.controller;

import challenge.competer.domain.auction.dto.ResponseAuctionDto;
import challenge.competer.domain.auction.service.AuctionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuctionControllerTest {

    @Autowired
    MockMvc mockMvc;

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

}