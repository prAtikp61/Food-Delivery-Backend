package com.FOOD.Request;

import com.FOOD.Models.Category;
import com.FOOD.Models.IngredientsList;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class createFoodRequest {

    private String Name;
    private String Description;
    private Long price;
    private Category category;
    private List<String> images;
    @JsonProperty("RestaurantId")
    private Long RestaurantId;
    private boolean vegetarian;
    public boolean seasonal;
    private List<IngredientsList> ingredients;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Long getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        RestaurantId = restaurantId;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public boolean isSeasonal() {
        return seasonal;
    }

    public void setSeasonal(boolean seasonal) {
        this.seasonal = seasonal;
    }

    public List<IngredientsList> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientsList> ingredients) {
        this.ingredients = ingredients;
    }
}
