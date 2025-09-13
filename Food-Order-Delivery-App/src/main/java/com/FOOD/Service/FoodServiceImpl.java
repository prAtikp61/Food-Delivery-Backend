package com.FOOD.Service;

import com.FOOD.Models.Category;
import com.FOOD.Models.Food;
import com.FOOD.Models.Restaurant;
import com.FOOD.Repo.FoodRepo;
import com.FOOD.Request.createFoodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepo foodRepo;


    private Restaurant restaurant;



    @Override
    public Food createFood(createFoodRequest reg, Category category, Restaurant restaurant) {
       Food food = new Food();
       food.setName(reg.getName());
       food.setDescription(reg.getDescription());
       food.setFoodCategory(category);
       food.setRestaurant(restaurant);
       food.setImages(reg.getImages());
       food.setPrice(reg.getPrice());
       food.setIngredients(reg.getIngredients());
       food.setSeasonal(reg.isSeasonal());
       food.setIsVegetarian(reg.isVegetarian());
       food.setCreationDate(new Date());

       Food savedfood=foodRepo.save(food);
       restaurant.getFoods().add(savedfood);

        return savedfood;


    }

    @Override
    public void deleteFood(Long foodId) throws Exception {
Food food=findFoodById(foodId);
food.setRestaurant(null);
foodRepo.save(food);
    }

    @Override
    public List<Food> getRestaurantsFood(Long restaurantId,
                                         boolean isvegetarian,
                                         boolean isnonveg,
                                         boolean isseasonal,
                                         String foodCategory) {

        List<Food> foods=foodRepo.findByRestaurantId(restaurantId);

        if(isvegetarian)
        {
             foods=filterByVegetarian(foods,isvegetarian);
        }
        if(isnonveg)
        {
            foods=filterByNonVeg(foods,isnonveg);
        }
        if(isseasonal)
        {
            foods=filterBySeasonal(foods,isseasonal);
        }

        if(foodCategory!=null && !foodCategory.equals(""))
        {
            foods=filterByCategory(foods,foodCategory);
        }
        return foods;
    }

    private List<Food> filterByCategory(List<Food> foods, String foodCategory) {
        return foods.stream().filter(food -> {
            if (food.getFoodCategory()!=null){
                return food.getFoodCategory().getName().equals(foodCategory);
            }
            return false;
        }).collect(Collectors.toList());


    }

    private List<Food> filterBySeasonal(List<Food> foods, boolean isseasonal) {
        return foods.stream().filter(food -> food.isSeasonal()==isseasonal).collect(Collectors.toList());
    }

    private List<Food> filterByNonVeg(List<Food> foods, boolean isnonveg) {
        return foods.stream().filter(food -> food.getIsVegetarian()==false).collect(Collectors.toList());
    }

    private List<Food> filterByVegetarian(List<Food> foods, boolean isvegetarian) {
        return foods.stream().filter(food -> food.getIsVegetarian()==isvegetarian).collect(Collectors.toList());
    }



    @Override
    public List<Food> searchFood(String keyword) {
        return foodRepo.searchFood(keyword);
    }

    @Override
    public Food findFoodById(Long id) throws Exception {
      Optional<Food> food=foodRepo.findById(id);
      if(food.isEmpty()){
          throw new Exception("Food Not Found");
      }
      return food.get();
    }

    @Override
    public Food updateAvailabiltyStatus(Long id) throws Exception {
        Food food=findFoodById(id);
        food.setAvailable(!food.isAvailable());
       return foodRepo.save(food);

    }
}
