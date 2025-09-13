package com.FOOD.Controllers;

import com.FOOD.Models.Order;
import com.FOOD.Models.User;
import com.FOOD.Models.cartItems;
import com.FOOD.Request.AddCartItemReq;
import com.FOOD.Request.OrderReq;
import com.FOOD.Service.OrderService;
import com.FOOD.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/order/ap")
    public ResponseEntity<Order> createOrder(@RequestBody OrderReq req,
                                                   @RequestHeader("Authorization")String token) throws Exception {
        User user =userService.findUserByJwtToken(token);
        Order order =orderService.createOrder(req,user);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/order/user")
    public ResponseEntity<List<Order>> getOrderHistory(
                                             @RequestHeader("Authorization")String token) throws Exception {
        User user =userService.findUserByJwtToken(token);
        List<Order> order =orderService.getUsersOrder(user.getId());

        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
