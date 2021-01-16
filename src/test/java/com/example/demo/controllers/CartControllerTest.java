package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static com.example.demo.utils.DataDummy.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private ItemRepository itemRepo = mock(ItemRepository.class);

    private static User dummyUser = dummyUser();

    private static ModifyCartRequest dummyModifyCartReq = dummyModCartReq();

    private static Item dummyItem = dummyItem();


    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void verify_add_item_to_cart() {
        when(userRepo.findByUsername(dummyModifyCartReq.getUsername())).thenReturn(dummyUser);
        when(itemRepo.findById(any())).thenReturn(Optional.of(dummyItem));
        ModifyCartRequest modifyCartReq = dummyModifyCartReq;

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartReq);
        Cart respondAsCart = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dummyItem.getPrice().multiply(new BigDecimal(dummyModifyCartReq.getQuantity())), respondAsCart.getTotal());
        assertEquals(dummyModifyCartReq.getQuantity(), respondAsCart.getItems().size());
    }

    @Test
    public void verify_remove_item_from_cart() {
        when(userRepo.findByUsername(dummyModifyCartReq.getUsername())).thenReturn(dummyUser);
        when(itemRepo.findById(any())).thenReturn(Optional.of(dummyItem));
        ModifyCartRequest modifyCartReq = dummyModifyCartReq;

        cartController.addTocart(modifyCartReq);
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartReq);
        Cart respondAsCart = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(new BigDecimal(0L), respondAsCart.getTotal());
    }


}