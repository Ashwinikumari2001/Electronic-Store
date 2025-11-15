package com.lcwd.electronic.store.ElectronicStore.services.Impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.OrderDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.entities.*;
import com.lcwd.electronic.store.ElectronicStore.exception.BadApiRequest;
import com.lcwd.electronic.store.ElectronicStore.exception.ResourceNotFound;
import com.lcwd.electronic.store.ElectronicStore.helper.Helper;
import com.lcwd.electronic.store.ElectronicStore.repositories.CartRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.OrderRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
  @Autowired
  private ModelMapper mapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private CartRepository cartRepository;

    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        //fetch user
        String userId=orderDto.getUserID();
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFound("user not found with given id!!"));
        //fetch cart
        String cartId=orderDto.getCartId();
        Cart cart=cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFound("not found!!"));

        List<CartItem> cartItems=cart.getItems();

        if(cartItems.size()<=0){
            throw new BadApiRequest("cart item not exits!!");
        }
        Order order=Order.builder()
                .billingName(orderDto.getBillingName())
                .billingAddress(orderDto.getBillingAddress())
                .billingPhone(orderDto.getBillingPhone())
                .orderDate(new Date())
                .deliveredDate(null)
                .orderStatus(orderDto.getOrderStatus())
                .paymentStatus(orderDto.getPaymentStatus())
                .orderId(UUID.randomUUID().toString())//orderitem,orderamount notset
                .user(user)
                .build();


        //here we are using atomic reference because When using Java Streams (map()), variables inside lamdas must be effectively final.
        //Because lambdas cannot modify local non-final variables.
        //So developers use:
        //AtomicReference<Integer>
        //or AtomicInteger
        //or arrays (int[] sum = {0})
        //or reduce() / sum() functions
        //ou want to mutate a variable (orderAmount) inside a lambda.
        //👉 Lambdas require the captured variable to be effectively final.
        //AtomicReference works because it allows mutating the value even though the reference is final.


//        AtomicReference<Integer> orderAmount=new AtomicReference<>(0);
//        List<OrderItem> orderItems=cartItems.stream().map(cartItem->{
//            OrderItem orderItem=OrderItem.builder()
//                    .quantity(cartItem.getQuantity())
//                    .product(cartItem.getProduct())
//                    .totalPrice(cartItem.getQuantity()*cartItem.getProduct().getDiscountedPrice())
//                    .order(order)
//                    .build();
//
//            orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());
//             return orderItem;
//        }).collect(Collectors.toList());
        int orderAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();

            orderAmount += orderItem.getTotalPrice();
            orderItems.add(orderItem);
        }
        order.setOrderItem(orderItems);
        order.setOrderAmount(orderAmount);
        List<CartItem> items=cart.getItems();
        for(CartItem item:items){
            int productQuantity=item.getProduct().getQuantity();
            int cartItemQuantity= item.getQuantity();
            item.getProduct().setQuantity(productQuantity-cartItemQuantity);
        }

        cart.getItems().clear();
        cartRepository.save(cart);
        Order savedOrder=orderRepository.save(order);


        return mapper.map(savedOrder,OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order=orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFound("not found!!"));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFound("not found!!"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtos=orders.stream().map(order->mapper.map(order,OrderDto.class)).collect(Collectors.toList());
        return orderDtos;

    }

    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page=orderRepository.findAll(pageable);
        return Helper.getPageableResponse(page,OrderDto.class);
    }
}
