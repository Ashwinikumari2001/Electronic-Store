package com.lcwd.electronic.store.ElectronicStore.services.Impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.CartDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Cart;
import com.lcwd.electronic.store.ElectronicStore.entities.CartItem;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.exception.ResourceNotFound;
import com.lcwd.electronic.store.ElectronicStore.repositories.CartItemRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.CartRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.ProductRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
       int quantity= request.getQuantity();
       String productId= request.getProductId();
       //fetch product
       Product product= productRepository.findById(productId).orElseThrow(()->new ResourceNotFound("given id is not found!!"));
        //fetch user from db
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFound("given id is not found!!"));
        Cart cart=null;

        try
        {
            cart=cartRepository.findByUser(user).get();
        }catch (NoSuchElementException e){
          cart=new Cart();
          cart.setCartId(UUID.randomUUID().toString());
          cart.setCreatedAt(new Date());
        }
      //perform cart operation
        //if cart items already present
        AtomicReference<Boolean>updated=new AtomicReference<>(false);
      List<CartItem> cartItems=cart.getItems();
        List<CartItem> updatedItems=cartItems.stream().map(item-> {
            if(item.getProduct().getId().equals(productId)){
                item.setQuantity(quantity);
                item.setTotalPrice(quantity* product.getDiscountedPrice());
                updated.set(true);
            }
          return item;
        }).collect(Collectors.toList());
//        List<CartItem> updatedItems=new ArrayList<>();
//        for(CartItem item:cartItems){
//            if(item.getProduct().getId().equals(productId)){
//                item.setQuantity(quantity);
//                item.setTotalPrice(quantity*product.getPrice());
//                updated.set(true);
//            }
//        }
        cart.setItems(updatedItems);
        if(!updated.get()) {
            CartItem cartItem = CartItem.builder().quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();

            cart.getItems().add(cartItem);
        }
        cart.setUser(user);
        Cart updateCart=cartRepository.save(cart);
        return mapper.map(updateCart,CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {


         CartItem cartItem1=cartItemRepository.findById(cartItem).orElseThrow(()->new ResourceNotFound("not found!!"));
         cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {
        // Approach 1:
//        List<Cart> cartList = cartRepository.findAll();
//        Cart cartUser=null;
//        for(Cart cart: cartList){
//            if(cart.getUser().getUserId().equalsIgnoreCase(userId)){
//                cartUser=cart;
//                break;
//            }
//        }
//        if(cartUser==null){
//            return;
//        }
//        cartUser.getItems().clear();
//        cartRepository.save(cartUser);

        // Approach 2:
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFound("not found!!"));
        Cart cart=cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFound("user not found!!"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartOfUser(String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFound("not found!!"));
        Cart cart=cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFound("not found!!"));
        return mapper.map(cart,CartDto.class);
    }
}
