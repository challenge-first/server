package challenge.competer.domain.product.service;

import challenge.competer.domain.product.dto.ResponseProductDto;
import challenge.competer.domain.product.productenum.MainCategory;
import challenge.competer.domain.product.productenum.SubCategory;

import java.util.List;

public interface ProductService {

    List<ResponseProductDto> getMainPageProducts();

    List<ResponseProductDto> getCategoryPageProducts(MainCategory mainCategory, SubCategory subCategory);

}
