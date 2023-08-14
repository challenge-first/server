package challenge.competer.domain.product.controller;

import challenge.competer.domain.product.dto.ResponseDetailProductDto;
import challenge.competer.domain.product.dto.ResponseProductDto;
import challenge.competer.domain.product.service.ProductService;
import challenge.competer.global.response.ResponseDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/main")
    public ResponseEntity<ResponseDataDto<List<ResponseProductDto>>> getMainPageProducts() {
        List<ResponseProductDto> responseProductDtos = productService.getMainPageProducts();
        ResponseDataDto<List<ResponseProductDto>> responseDataDto = new ResponseDataDto<>(responseProductDtos);

        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    @GetMapping("/maincategory/{mainCategory}")
    public ResponseEntity<ResponseDataDto<List<ResponseProductDto>>> getCategoryProducts(@PathVariable String mainCategory,
                                                                                         @RequestParam(name = "subcategory", required = false) List<String> subCategory) {
        List<ResponseProductDto> responseProductDtos = productService.getCategoryProducts(mainCategory, subCategory);
        ResponseDataDto<List<ResponseProductDto>> responseDataDto = new ResponseDataDto<>(responseProductDtos);

        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ResponseDataDto<ResponseDetailProductDto>> getDetailProduct(@PathVariable Long productId) {
        ResponseDetailProductDto responseDetailProductDto = productService.getDetailProduct(productId);
        ResponseDataDto<ResponseDetailProductDto> responseDataDto = new ResponseDataDto<>(responseDetailProductDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

}
