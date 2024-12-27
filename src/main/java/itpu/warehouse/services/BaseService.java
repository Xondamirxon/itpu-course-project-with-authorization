package itpu.warehouse.services;

import itpu.warehouse.dto.ProductDto;

import java.util.List;

public interface BaseService {

    List<ProductDto> list();
    ProductDto getById(Integer id);
    ProductDto add(ProductDto dto);
    ProductDto edit(ProductDto dto);
    void delete(Integer id);
}
