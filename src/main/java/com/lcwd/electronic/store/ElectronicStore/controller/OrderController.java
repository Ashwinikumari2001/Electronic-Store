package com.lcwd.electronic.store.ElectronicStore.controller;

import com.lcwd.electronic.store.ElectronicStore.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.ElectronicStore.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.OrderDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
      @PostMapping
     public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request){
         OrderDto order=orderService.createOrder(request);
         return new ResponseEntity<>(order, HttpStatus.CREATED);
     }

     @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId){
          orderService.removeOrder(orderId);
          ApiResponseMessage apiResponseMessage=ApiResponseMessage.builder()
                  .message("order is removed!!")
                  .success(true)
                  .httpStatus(HttpStatus.OK)
                  .build();
          return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);
     }

     @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId){
          List<OrderDto> orderDtos=orderService.getOrdersOfUser(userId);
          return new ResponseEntity<>(orderDtos,HttpStatus.OK);
     }

     @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(@RequestParam (value = "pageNumber",required = false,defaultValue ="0") int pageNumber,
                                                                   @RequestParam(value="pageSize",defaultValue ="1",required = false) int pageSize,
                                                                   @RequestParam(value="sortBy",defaultValue = "orderDate",required = false) String sortBy,
                                                                   @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir){
          PageableResponse<OrderDto> orderDtoPageableResponse=orderService.getAllOrders(pageNumber,pageSize,sortBy,sortDir);
          return new ResponseEntity<>(orderDtoPageableResponse,HttpStatus.OK);
     }
}
