package itpu.warehouse.entity;

import itpu.warehouse.dto.ProductDto;

public class Product {
    private Integer id;
    private String name;
    private String category;
    private Float price;
    private Integer quantity;

    public Product() {
    }

    public Product(Integer id, String name, String category, Float price, Integer quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(ProductDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.category = dto.getCategory();
        this.price = dto.getPrice();
        this.quantity = dto.getQuantity();
    }

    public Product(String[] parts) {
        this.id = (Integer.parseInt(parts[0]));
        this.name = parts[1];
        this.category = parts[2];
        this.price = Float.parseFloat(parts[3]);
        this.quantity = Integer.parseInt(parts[4]);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Float getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("| %-7d | %-25s | %-25s | $%-10f | %-8d |", id, name, category, price, quantity);
    }

    public ProductDto todDto() {
        return new ProductDto(getId(), getName(), getCategory(), getPrice(), getQuantity());
    }
}
