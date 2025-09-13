package com.FOOD.Service;

import com.FOOD.Models.Cart;
import com.FOOD.Models.Order;
import com.FOOD.Models.User;
import com.FOOD.Models.cartItems;
import com.FOOD.Request.AddCartItemReq;
import com.FOOD.Request.OrderReq;

public interface CartService {
    public cartItems addToCart(AddCartItemReq req, String jwt) throws Exception;

    public cartItems updateCartItemQuantity(Long cartItemId, int quantity) throws Exception;

    public Cart removeCartItemFromCart(Long cartItemId, String jwt) throws Exception;

    public Long calculateCartTotal(Cart cart) throws Exception;

    public Cart findCartById(Long Id) throws Exception;

    public Cart findCartByUserId(Long userId) throws Exception;

    public Cart clearCart(Long userId) throws Exception;
    Order createOrder(OrderReq req, User user) throws Exception;

}