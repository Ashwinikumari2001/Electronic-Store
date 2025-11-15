package com.lcwd.electronic.store.ElectronicStore.dtos;

import com.lcwd.electronic.store.ElectronicStore.entities.Order;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemDto {

    private int OrderItemId;

    private int totalPrice;

    private int quantity;

    private ProductDto product;


//    private OrderDto order;
}
