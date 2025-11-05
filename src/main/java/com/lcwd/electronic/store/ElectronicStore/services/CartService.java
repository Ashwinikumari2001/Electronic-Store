package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.CartDto;

public interface CartService {

    //add item to the cart
    //case1: if cart is not available of the user then we available the cart first then add items to cart
    //case2: if cart is available then directly add items to cart
    CartDto addItemToCart(String UserId, AddItemToCartRequest request);


    //remove item from cart
    void  removeItemFromCart(String userId,int cartItem);


    //remove all item from cart
    void  clearCart(String userId);
}
