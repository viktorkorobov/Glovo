package org.glovo.glovo.service;

import lombok.AllArgsConstructor;
import org.glovo.glovo.exception.OrderNotFoundException;
import org.glovo.glovo.exception.OrderProcessingException;
import org.glovo.glovo.exception.ProductNotFoundException;
import org.glovo.glovo.model.Order;
import org.glovo.glovo.model.Product;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final List<Order> orders;
    private final ProductService productService;

    public List<Order> getAllOrders() {
        return orders;
    }

    public Order getOrderById(Long id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
    }

    public Order createOrder(Order order) {
        try {
            order.setId(generateNextOrderId());
            order.setCreationDateTime(LocalDateTime.now());
            for (Product product : order.getProducts()) {
                productService.addProduct(product);
            }

            orders.add(order);
            recalculateOrderTotal(order);
            return order;
        } catch (Exception e) {
            throw new OrderProcessingException("Error processing order creation", e);
        }
    }

    public void updateOrder(Order updatedOrder) {
        try {
            Order existingOrder = getOrderById(updatedOrder.getId());
            for (Product updatedProduct : updatedOrder.getProducts()) {
                Product existingProduct = existingOrder.getProducts().stream()
                        .filter(p -> p.getName().equals(updatedProduct.getName()))
                        .findFirst()
                        .orElseThrow(() -> new ProductNotFoundException("Product not found in order with ID: " + updatedProduct.getId()));
                productService.updateProductQuantity(existingProduct.getId(), updatedProduct.getQuantity());
            }
            existingOrder.setModificationDateTime(LocalDateTime.now());
            recalculateOrderTotal(existingOrder);
        } catch (Exception e) {
            throw new OrderProcessingException("Error processing order update", e);
        }
    }

    public void deleteOrder(Long id) {
        try {
            Order order = getOrderById(id);
            orders.remove(order);
        } catch (Exception e) {
            throw new OrderProcessingException("Error processing order deletion", e);
        }
    }
    private synchronized Long generateNextOrderId() {
        return (long) orders.size() + 1;
    }

    public void addProductToOrder(Long id, Product product) {
        try {
            Order order = getOrderById(id);
            productService.addProduct(product);
            order.getProducts().add(product);
            recalculateOrderTotal(order);
        } catch (Exception e) {
            throw new OrderProcessingException("Error adding product to order", e);
        }
    }

    public void removeProductFromOrder(Long orderId, Long productId) {
        try {
            Order order = getOrderById(orderId);
            Product productToRemove = order.getProducts().stream()
                    .filter(product -> product.getId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new ProductNotFoundException("Product not found in order with ID: " + productId));
            order.getProducts().remove(productToRemove);
            recalculateOrderTotal(order);
        } catch (Exception e) {
            throw new OrderProcessingException("Error removing product from order", e);
        }
    }

    private void recalculateOrderTotal(Order order) {
        double totalAmount = order.getProducts().stream()
                .mapToDouble(product -> product.getPrice() * product.getQuantity())
                .sum();

        order.setTotalAmount(totalAmount);
    }

}
