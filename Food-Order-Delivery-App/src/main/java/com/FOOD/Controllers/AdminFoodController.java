package com.FOOD.Controllers;

import com.FOOD.Models.Food;
import com.FOOD.Models.Restaurant;
import com.FOOD.Models.User;
import com.FOOD.Request.createFoodRequest;
import com.FOOD.Response.MessageResponse;
import com.FOOD.Service.FoodService;
import com.FOOD.Service.RestaurantService;
import com.FOOD.Service.UserService;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/food")
public class AdminFoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<Food> createFood(@RequestBody createFoodRequest req ,
                                           @RequestHeader("Authorization")String jwt
                                           ) throws Exception {
        System.out.println("=== DEBUG INFO ===");
        System.out.println("Request object: " + req);
        System.out.println("Name: " + req.getName());
        System.out.println("Description: " + req.getDescription());
        System.out.println("Price: " + req.getPrice());
        System.out.println("RestaurantId: " + req.getRestaurantId());
        System.out.println("Category: " + req.getCategory());
        System.out.println("==================");
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant= restaurantService.findRestaurantById(req.getRestaurantId());
        Food food= foodService.createFood(req,req.getCategory(),restaurant);
        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteFood(@PathVariable Long id,
                                                      @RequestHeader("Authorization")String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        foodService.deleteFood(id);
        MessageResponse m1 = new MessageResponse();
        m1.setMessage("food deleted successfully");
        return new ResponseEntity<>(m1, HttpStatus.OK);
    }
    @PostMapping("/{id}")
    public ResponseEntity<Food> updateFood(@PathVariable Long id,
                                           @RequestHeader("Authorization")String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Food food= foodService.updateAvailabiltyStatus(id);
        return new ResponseEntity<>(food, HttpStatus.OK);
    }



}
