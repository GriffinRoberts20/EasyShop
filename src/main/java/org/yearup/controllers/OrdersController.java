package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.*;
import org.yearup.models.*;

import java.security.Principal;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("orders")
@CrossOrigin
@PreAuthorize("hasRole('ROLE_USER')")
public class OrdersController {
    private ProductDao productDao;
    private ProfileDao profileDao;
    private UserDao userDao;
    private OrderDao orderDao;
    private OrderLineItemDao orderLineItemDao;
    private ShoppingCartDao shoppingCartDao;

    @Autowired
    public OrdersController(ProductDao productDao, ProfileDao profileDao, UserDao userDao, OrderDao orderDao, OrderLineItemDao orderLineItemDao, ShoppingCartDao shoppingCartDao) {
        this.productDao = productDao;
        this.profileDao = profileDao;
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.shoppingCartDao = shoppingCartDao;
    }

    @PostMapping("")
    public Order checkout(Principal principal){
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            ShoppingCart shoppingCart=shoppingCartDao.getByUserId(userId);
            if(shoppingCart.getItems().isEmpty()) return null;
            Profile profile=profileDao.getByUserId(userId);
            AtomicInteger shippingTotal= new AtomicInteger();
            shoppingCart.getItems().forEach((key,ShoppingCartItem)->{
                shippingTotal.getAndIncrement();
            });
            Order order=new Order(profile,shippingTotal.doubleValue());
            order=orderDao.add(order);
            Order finalOrder = order;
            shoppingCart.getItems().forEach((key, ShoppingCartItem)->{
                Product product=ShoppingCartItem.getProduct();
                OrderLineItem orderLineItem=new OrderLineItem(finalOrder.getOrderId(),product.getProductId(),product.getPrice(),ShoppingCartItem.getQuantity());
                orderLineItemDao.add(orderLineItem);
            });
            return order;
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
