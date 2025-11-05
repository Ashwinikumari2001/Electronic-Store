package com.lcwd.electronic.store.ElectronicStore.services.Impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.CartDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Cart;
import com.lcwd.electronic.store.ElectronicStore.entities.CartItem;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.exception.ResourceNotFound;
import com.lcwd.electronic.store.ElectronicStore.repositories.CartRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.ProductRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

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
    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
       int quantity= request.getQuantity();
       String productId= request.getProductId();
       //fetch product
       Product product= productRepository.findById(productId).orElseThrow(()->new ResourceNotFound("given id is not found!!"));
        //fetch user from db
        User user=userRepository.findByEmail(userId).orElseThrow(()->new ResourceNotFound("given id is not found!!"));
        Cart cart=null;

        try
        {
            cart=cartRepository.findByUser(user).get();
        }catch (NoSuchElementException e){
          cart=new Cart();
          cart.setCreatedAt(new Date());
        }
      //perform cart operation
      //List<CartItem> cartItems=cart.getItems();
        CartItem cartItem=CartItem.builder().quantity(quantity)
                .totalPrice(quantity*product.getPrice())
                .cart(cart)
                .product(product)
                .build();

        cart.getItems().add(cartItem);
        cart.setUser(user);
        Cart updateCart=cartRepository.save(cart);
        return mapper.map(updateCart,CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {

    }

    @Override
    public void clearCart(String userId) {

    }
}
