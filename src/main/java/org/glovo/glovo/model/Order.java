package org.glovo.glovo.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Order {

    private Long id;
    private List<Product> products;
    private LocalDateTime creationDateTime;
    private LocalDateTime modificationDateTime;
    private double totalAmount;

}



