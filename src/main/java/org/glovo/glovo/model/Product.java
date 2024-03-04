package org.glovo.glovo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private double price;
    private int quantity;

}
