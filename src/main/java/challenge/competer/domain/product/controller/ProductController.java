package challenge.competer.domain.product.controller;

import challenge.competer.domain.product.dto.ResponseProductDto;
import challenge.competer.domain.product.productenum.MainCategory;
import challenge.competer.domain.product.productenum.SubCategory;
import challenge.competer.domain.product.service.ProductService;
import challenge.competer.global.response.ResponseDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/main")
    public ResponseEntity<ResponseDataDto> getMainPageProducts() {
        List<ResponseProductDto> responseProductDtos = productService.getMainPageProducts();
        ResponseDataDto responseDataDto = new ResponseDataDto(responseProductDtos);

        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    @GetMapping("/main/maincategory/{mainCategory}/subcategory/{subCategory}")
    public ResponseEntity<ResponseDataDto> getCategoryProducts(@PathVariable MainCategory mainCategory, @PathVariable SubCategory subCategory) {
        List<ResponseProductDto> responseProductDtos = productService.getCategoryPageProducts(mainCategory, subCategory);
        ResponseDataDto responseDataDto = new ResponseDataDto(responseProductDtos);

        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

}
