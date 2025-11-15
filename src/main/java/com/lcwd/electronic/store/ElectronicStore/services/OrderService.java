package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.OrderDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.PageableResponse;

import java.util.List;

public interface OrderService {

    //create Order
     OrderDto createOrder(CreateOrderRequest request);

    //remove Order
      void  removeOrder(String orderId);

      //get orders of user
    List<OrderDto> getOrdersOfUser(String userId);

    //get all orders
    PageableResponse<OrderDto> getAllOrders(int pageNumber,int pageSize,String sortBy,String sortDir);
}
