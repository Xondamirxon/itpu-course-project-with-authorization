package itpu.warehouse.dao;

import itpu.warehouse.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static itpu.warehouse.Constants.products;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public Optional<Product> findById(Integer id) {
        return products.values().stream()
                .flatMap(List::stream)
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Product> findAll() {
        List<Product> productList = new ArrayList<>();
        products.values().forEach(productList::addAll);
        return productList;
    }

    @Override
    public List<Product> findByCategory(String category) {
        return products.values().stream()
                .flatMap(List::stream)
                .filter(p -> p.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByName(String name) {
        if (name == null || name.isEmpty()) return null;
        return products.values().stream()
                .flatMap(List::stream)
                .filter(p -> p.getName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public void save(Product product) {
        products.get(product.getCategory()).add(product);
    }

    @Override
    public void update(Product product) {
        products.get(product.getCategory()).stream().filter(p->p.getId().equals(product.getId())).findFirst().ifPresent(p->{
            p.setName(product.getName());
            p.setCategory(product.getCategory());
            p.setPrice(product.getPrice());
            p.setQuantity(product.getQuantity());
        });
    }

    @Override
    public void delete(Integer id) {
        for (String key : products.keySet()) {
            List<Product> productList = products.get(key);
            productList.removeIf(product -> product.getId().equals(id));
        }
    }

}
