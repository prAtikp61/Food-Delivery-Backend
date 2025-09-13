package com.FOOD.Service;

import com.FOOD.Models.*;
import com.FOOD.Repo.OrderItemRepo;
import com.FOOD.Repo.OrderRepo;
import com.FOOD.Repo.addressRepo;
import com.FOOD.Repo.userRepo;
import com.FOOD.Request.OrderReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private addressRepo repo1;

    @Autowired
    private userRepo userRepo;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CartService cartService;

    @Override
    public Order createOrder(OrderReq req, User user) throws Exception {
        System.out.println("Delivery address: " + req.getDeliveryAdd());

        if (req.getDeliveryAdd() == null) {
            throw new Exception("Delivery address cannot be null");
        }

        Addresses shippingAddress = req.getDeliveryAdd();
        Addresses savedAddress = repo1.save(shippingAddress);

        if (!user.getAddressess().contains(savedAddress)) {
            user.getAddressess().add(savedAddress);
            userRepo.save(user);
        }

        Restaurant restaurant = restaurantService.findRestaurantById(req.getRestaurantId());
        if (restaurant == null) {
            throw new Exception("Restaurant not found");
        }

        Order createdOrder = new Order();
        createdOrder.setCustomer(user);
        createdOrder.setCreatedAt(new Date());
        createdOrder.setOrderStatus("PENDING");
        createdOrder.setRestaurant(restaurant);
        createdOrder.setDeliveryAddress(savedAddress);

        Cart cart = cartService.findCartByUserId(user.getId());
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new Exception("Cart is empty. Cannot create order.");
        }

        List<orderItem> orderItems = new ArrayList<>();
        for (cartItems ci : cart.getCartItems()) {
            orderItem orderItem = new orderItem();
            orderItem.setFood(ci.getFood());
            orderItem.setQuantity(ci.getQuantity());
            orderItem.setIngredients(ci.getIngredients());
            orderItem.setTotalPrice(ci.getTotalPrice());

            // IMPORTANT: set relation
            orderItem.setOrder(createdOrder);

            orderItem savedOrderItem = orderItemRepo.save(orderItem);
            orderItems.add(savedOrderItem);
        }

        Long totalPrice = cartService.calculateCartTotal(cart);
        createdOrder.setItems(orderItems);
        createdOrder.setTotalPrice(totalPrice);

        Order savedOrder = orderRepo.save(createdOrder);

        // ensure restaurant orders list is initialized
        if (restaurant.getOrders() == null) {
            restaurant.setOrders(new ArrayList<>());
        }
        restaurant.getOrders().add(savedOrder);

        return savedOrder;
    }


    @Override
    public Order updateOrder(Long orderId, String status) throws Exception {
        Order order=findOrderById(orderId);
        if(status.equals("OUT_FOR_DELIVERY")|| status.equals("DELIVERED") || status.equals("COMPLETED") || status.equals("PENDING") ){
            order.setOrderStatus(status);
            return orderRepo.save(order);
        }
        throw new Exception("please select a valid order status");

    }

    @Override
    public void cancelOrder(Long orderId) throws Exception {
        Order order=findOrderById(orderId);
        orderRepo.deleteById(orderId);
    }

    @Override
    public List<Order> getUsersOrder(Long userId) throws Exception {

        return orderRepo.findByCustomer_Id(userId);
    }

    @Override
    public List<Order> getRestaurantsOrder(Long restaurantId, String orderStatus) throws Exception {
        List<Order> orders= orderRepo.findByRestaurantId(restaurantId);
        if(orderStatus!=null)
        {
            orders=orders.stream().filter(order->order.getOrderStatus().equals(orderStatus)).collect(Collectors.toList());

        }
        return orders;
    }

    @Override
    public Order findOrderById(Long orderId) throws Exception {
        Optional<Order> optorder=orderRepo.findById(orderId);
        if(optorder.isEmpty())
        {
            throw new Exception("Order not found");
        }

        return optorder.get();
    }
}
