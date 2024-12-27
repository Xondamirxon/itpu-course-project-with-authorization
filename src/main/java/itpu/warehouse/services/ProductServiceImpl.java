package itpu.warehouse.services;

import itpu.warehouse.dao.ProductDAO;
import itpu.warehouse.dto.ProductDto;
import itpu.warehouse.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {

    private final ProductDAO dao;

    public ProductServiceImpl(ProductDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<ProductDto> list() {
        return dao.findAll().stream().map(Product::todDto).collect(Collectors.toList());
    }

    @Override
    public ProductDto getById(Integer id) {
        return dao.findById(id).orElseThrow().todDto();
    }

    @Override
    public ProductDto add(ProductDto dto) {
        dao.save(new Product(dto));
        return dto;
    }

    @Override
    public ProductDto edit(ProductDto dto) {
        Optional<Product> product = dao.findById(dto.getId());
        if(product.isPresent()) {
            product.get().setName(dto.getName());
            product.get().setPrice(dto.getPrice());
            product.get().setQuantity(dto.getQuantity());
            dao.update(product.get());
        } else {
            throw new RuntimeException("Product not found. ID: " + dto.getId());
        }
        return dto;
    }

    @Override
    public void delete(Integer id) {
        dao.delete(id);
    }
}
