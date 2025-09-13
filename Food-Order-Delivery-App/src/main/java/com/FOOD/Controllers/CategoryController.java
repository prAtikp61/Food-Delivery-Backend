package com.FOOD.Controllers;

import com.FOOD.Models.Category;
import com.FOOD.Models.User;
import com.FOOD.Service.CategoryService;
import com.FOOD.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;


    @PostMapping("/admin/category")
    public ResponseEntity<Category> createCategory(@RequestBody Category category,
            @RequestHeader("Authorization") String token) throws Exception {
        User user=  userService.findUserByJwtToken(token);
        Category category1=categoryService.createCategory(category.getName(),user.getId());

        return new ResponseEntity<>(category1, HttpStatus.CREATED);
    }
    @GetMapping ("/category/restaurant/{id}")
    public ResponseEntity<List<Category>> getRestaurantCategory(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) throws Exception {
        User user=  userService.findUserByJwtToken(token);
       List <Category> category1=categoryService.findCategoryByRestaurantId(id);

        return new ResponseEntity<>(category1, HttpStatus.CREATED);
    }
}

