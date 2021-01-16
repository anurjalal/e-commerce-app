package com.example.demo.utils;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DataDummy {
    public static User dummyUser(){
        User user = new User();
        user.setUsername("Jalal");
        user.setPassword("thisisMyPassword");
        user.setCart(new Cart());
        return user;
    }

    public static ModifyCartRequest dummyModCartReq(){
        ModifyCartRequest modifyCartReq = new ModifyCartRequest();
        modifyCartReq.setUsername(dummyUser().getUsername());
        modifyCartReq.setItemId(1L);
        modifyCartReq.setQuantity(4);
        return modifyCartReq;
    }

    public static Item dummyItem(){
        Item item = new Item();
        item.setId(1L);
        item.setDescription("Gayo coffee premium from Indonesia");
        item.setName("Coffee");
        item.setPrice(new BigDecimal(90000));
        return item;
    }

    public static List<Item> dummy5Items(){
        List<Item> items = new ArrayList<>();
        for(int i=1;i<6;i++){
            Item item = dummyItem();
            item.setId((long) i);
            items.add(item);
        }
        return items;
    }

    public static Cart dummyCart(){
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(dummy5Items());
        cart.setTotal(dummyItem().getPrice().multiply(new BigDecimal(5)));
        cart.setUser(dummyUser());
        return cart;
    }

    public static UserOrder dummyUserOrder(){
        UserOrder userOrder = new UserOrder();
        userOrder.setId(1L);
        userOrder.setItems(dummy5Items());
        userOrder.setUser(dummyUser());
        userOrder.setTotal(dummyCart().getTotal());
        return userOrder;
    }

    public static CreateUserRequest dummyUserReq(){
        CreateUserRequest createUsrReq = new CreateUserRequest();
        createUsrReq.setUsername("Jalal");
        createUsrReq.setPassword("thisisMyPassword");
        createUsrReq.setConfirmPassword("thisisMyPassword");
        return createUsrReq;
    }
}
