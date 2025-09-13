package com.FOOD.Controllers;

import com.FOOD.Models.Restaurant;
import com.FOOD.Models.User;
import com.FOOD.Request.createRestaurantRequest;
import com.FOOD.Response.MessageResponse;
import com.FOOD.Service.RestaurantService;
import com.FOOD.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.processing.Messager;
import javax.annotation.processing.SupportedAnnotationTypes;

@RestController
@RequestMapping("/api/admin/restaurants")
public class AdminRestaurantController {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody createRestaurantRequest req,
                                                    @RequestHeader("Authorization") String jwt
    ) throws Exception{
        System.out.println("take this->"+jwt);
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant=restaurantService.createRestaurant(req,user);
        return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@RequestBody createRestaurantRequest req,
                                                    @RequestHeader("Authorization") String jwt,
                                                       @PathVariable Long id
    ) throws Exception{

        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant=restaurantService.updateRestaurant(id,req);
        return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRestaurant(
                                                       @RequestHeader("Authorization") String jwt,
                                                       @PathVariable Long id
    ) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
       restaurantService.deleteRestaurant(id);
        MessageResponse msg= new MessageResponse();
        msg.setMessage("Restaurant deleted");
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<Restaurant> updateRestaurantStatus(
                                                       @RequestHeader("Authorization") String jwt,
                                                       @PathVariable Long id
    ) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant=restaurantService.updateRestaurantStatus(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Restaurant> findRestaurantByUserId(
                                                             @RequestHeader("Authorization") String jwt

    ) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant=restaurantService.findRestaurantByUserId(user.getId());
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
}
