package com.FOOD.Service;

import com.FOOD.Models.*;
import com.FOOD.Repo.*;
import com.FOOD.Request.AddCartItemReq;
import com.FOOD.Request.OrderReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {



    @Autowired
    private addressRepo addressRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private userRepo userRepo;
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserService userService;
    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private FoodService foodService;

    @Override
    public Order createOrder(OrderReq req, User user) throws Exception {

        if (req.getDeliveryAdd() == null) {
            throw new Exception("Delivery address cannot be null");
        }

        // Save delivery address
        Addresses shippingAddress = req.getDeliveryAdd();
        Addresses savedAddress = addressRepo.save(shippingAddress);

        // Add to user's addresses if not already present
        if (!user.getAddressess().contains(savedAddress)) {
            user.getAddressess().add(savedAddress);
            userRepo.save(user);
        }

        // Fetch restaurant
        Restaurant restaurant = restaurantService.findRestaurantById(req.getRestaurantId());
        if (restaurant == null) {
            throw new Exception("Restaurant not found");
        }

        // 1️⃣ Create order WITHOUT items
        Order createdOrder = new Order();
        createdOrder.setCustomer(user);
        createdOrder.setCreatedAt(new Date());
        createdOrder.setOrderStatus("PENDING");
        createdOrder.setRestaurant(restaurant);
        createdOrder.setDeliveryAddress(savedAddress);

        // Save order first
        Order savedOrder = orderRepo.save(createdOrder);


        List<orderItem> orderItems = new ArrayList<>();

        // 3️⃣ Update order with items and total

        savedOrder.setItems(orderItems);

        savedOrder = orderRepo.save(savedOrder);

        // 4️⃣ Add order to restaurant orders
        if (restaurant.getOrders() == null) {
            restaurant.setOrders(new ArrayList<>());
        }
        restaurant.getOrders().add(savedOrder);

        return savedOrder;
    }




    @Override
    public cartItems addToCart(AddCartItemReq req, String jwt) throws Exception {
       User user= userService.findUserByJwtToken(jwt);
       Food food=foodService.findFoodById(req.getFoodId());
       Cart cart=cartRepo.findByCustomerId(user.getId());

       for(cartItems cartItem:cart.getCartItems()){
           if(cartItem.getFood().equals(food))
           {
               int newQuantity=cartItem.getQuantity()+req.getQuantity();
               return updateCartItemQuantity(cartItem.getId(),newQuantity);
           }
       }

       cartItems newCartItems=new cartItems();
       newCartItems.setFood(food);
       newCartItems.setQuantity(req.getQuantity());
       newCartItems.setCart(cart);
       newCartItems.setIngredients(req.getIngredients());
       newCartItems.setTotalPrice(req.getQuantity()* food.getPrice());
     cartItems cartItems=cartItemRepo.save(newCartItems);
     cart.getCartItems().add(cartItems);
        return cartItems;
    }

    @Override
    public cartItems updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {
        return null;
    }


    @Override
    public Cart removeCartItemFromCart(Long cartItemId, String jwt) throws Exception {
        User user= userService.findUserByJwtToken(jwt);
        Cart cart=cartRepo.findByCustomerId(user.getId());
        Optional<cartItems> optcartItems=cartItemRepo.findById(cartItemId);
        if(optcartItems.isEmpty()){
            throw new Exception("CartItem not found");
        }
        cartItems item=optcartItems.get();
        cart.getCartItems().remove(item);
        return cartRepo.save(cart);
    }

    @Override
    public Long calculateCartTotal(Cart cart) throws Exception {
        Long total=0L;
        for(cartItems cartItem:cart.getCartItems()){
            total+=cartItem.getFood().getPrice()*cartItem.getQuantity();
        }

        return total;
    }

    @Override
    public Cart findCartById(Long Id) throws Exception {
        Optional<Cart> optcart=cartRepo.findById(Id);
        if(optcart.isEmpty()){
            throw new Exception("CartItem not found");
        }
        return optcart.get();
    }

    @Override
    public Cart findCartByUserId(Long id) throws Exception {
        Cart cart=cartRepo.findByCustomerId(id);
        cart.setTotal(calculateCartTotal(cart));

        return cart;

    }

    @Override
    public Cart clearCart(Long id) throws Exception {
      Cart cart=cartRepo.findByCustomerId(id);
      cart.getCartItems().clear();
        return cartRepo.save(cart);
    }


}
