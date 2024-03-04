package org.glovo.glovo.service;

import lombok.AllArgsConstructor;
import org.glovo.glovo.exception.ProductNotFoundException;
import org.glovo.glovo.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final List<Product> products;

    public Product getProductById(Long id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
    }

    public void addProduct(Product product) {
        product.setId(generateNextProductId());
        products.add(product);
    }

    public void updateProductQuantity(Long id, int quantity) {
        Product product = getProductById(id);
        product.setQuantity(quantity);
    }

    private synchronized Long generateNextProductId() {
        return (long) products.size() + 1;
    }
}
