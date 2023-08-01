package challenge.competer.domain.product.service;

import challenge.competer.domain.product.dto.ResponseProductDto;

import java.util.List;

public interface ProductService {

    List<ResponseProductDto> getMainPageProducts();

}
