package itpu.warehouse.controller;

import itpu.warehouse.dto.ProductDto;
import itpu.warehouse.services.ProductService;

public class WarehouseController {

    private final ProductService productService;

    public WarehouseController(ProductService productService) {
        this.productService = productService;
    }

    public void getAllPlates() {
        productService.list().forEach(System.out::println);
    }

    public void getProduct(Integer id) {
        ProductDto dto = productService.getById(id);
        if(dto != null) {
            System.out.println(dto.toString());
        } else {
            System.out.println("Product not found!!!");
        }
    }

    public void addProduct(ProductDto dto) {
        try {
            dto = productService.add(dto);
            System.out.println("Product successfully saved. ID: " + dto.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void editProduct(ProductDto dto) {
        try {
            dto = productService.edit(dto);
            System.out.println("Product successfully edited. ID: " + dto.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeProduct(Integer productId) {
        try {
            productService.delete(productId);
            System.out.println("Product successfully removed. ID: " + productId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
