package org.glovo.glovo.controller;

import lombok.RequiredArgsConstructor;
import org.glovo.glovo.model.Order;
import org.glovo.glovo.model.Product;
import org.glovo.glovo.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @PutMapping("/{id}")
    public void updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        updatedOrder.setId(id);
        orderService.updateOrder(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    @PostMapping("/{id}/addProduct")
    public void addProductToOrder(@PathVariable Long id, @RequestBody Product product) {
        orderService.addProductToOrder(id, product);
    }

    @DeleteMapping("/{orderId}/removeProduct/{productId}")
    public void removeProductFromOrder(@PathVariable Long orderId, @PathVariable Long productId) {
        orderService.removeProductFromOrder(orderId, productId);
    }
}
