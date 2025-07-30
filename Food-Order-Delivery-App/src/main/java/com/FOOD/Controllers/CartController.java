package com.FOOD.Controllers;

import com.FOOD.Models.Cart;
import com.FOOD.Models.User;
import com.FOOD.Models.cartItems;
import com.FOOD.Request.AddCartItemReq;
import com.FOOD.Request.UpdateCartItemReq;
import com.FOOD.Service.CartService;
import com.FOOD.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;


    @PutMapping("/cart/add")
    public ResponseEntity<cartItems> addItemToCart(@RequestBody AddCartItemReq req,
                                                   @RequestHeader("Authorization")String token) throws Exception {
        cartItems cartItems=cartService.addToCart(req,token);
        return new ResponseEntity<>(cartItems,HttpStatus.OK);

    }
    @PutMapping("/cart-item/update")
    public ResponseEntity<cartItems> updateCartItemQuantity(@RequestBody UpdateCartItemReq req,
                                                   @RequestHeader("Authorization")String token) throws Exception {
        cartItems cartItems = cartService.updateCartItemQuantity(req.getCartItemId(), req.getQuantity());
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }

    @PutMapping("/cart-item/{id}/remove")
    public ResponseEntity<Cart> removeCartItem(@PathVariable Long id,
                                                    @RequestHeader("Authorization")String jwt) throws Exception {

        Cart cart=cartService.removeCartItemFromCart(id, jwt);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/cart/clear")
    public ResponseEntity<Cart> clearCart(@RequestHeader("Authorization")String token) throws Exception {
        User user=userService.findUserByJwtToken(token);
      Cart cartItems=cartService.clearCart(user.getId());
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization")String token) throws Exception {
        User user=userService.findUserByJwtToken(token);
        Cart cartItems=cartService.findCartByUserId(user.getId());
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }


}
