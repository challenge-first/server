package challenge.competer.domain.product.controller;

import challenge.competer.domain.product.dto.ResponseProductDto;
import challenge.competer.domain.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    private List<ResponseProductDto> responseProductDtos = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        for (int i = 4; i > 0; i--) {
            ResponseProductDto responseProductDto = ResponseProductDto.builder().build();
            responseProductDtos.add(responseProductDto);
        }
    }

    @Test
    void getMainPageProductsTest() throws Exception {
        //given
        when(productService.getMainPageProducts()).thenReturn(responseProductDtos);

        //when, then
        mockMvc.perform(get("/products/main"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("data").exists())
                .andExpect(jsonPath("data").isArray())
                .andExpect(jsonPath("data.length()").value(4));
    }

}