package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.utils.DataDummy;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.utils.DataDummy.dummyCart;
import static com.example.demo.utils.DataDummy.dummyUser;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserOrder.class)
public class OrderControllerTest {

    OrderController orderController;

    private UserRepository userRepo = mock(UserRepository.class);

    private OrderRepository orderRepo = mock(OrderRepository.class);

    private static User dummyUser = dummyUser();

    private static Cart dummyCart = dummyCart();

    private static UserOrder dummyUserOrder = DataDummy.dummyUserOrder();

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
    }

    @Test
    public void verify_submit_order() throws Exception {
        User usr = dummyUser;
        usr.setCart(dummyCart);
        when(userRepo.findByUsername(usr.getUsername())).thenReturn(dummyUser);

        PowerMock.mockStatic(UserOrder.class);
        expect(UserOrder.createFromCart(usr.getCart())).andReturn(dummyUserOrder);
        PowerMock.replayAll();

        ResponseEntity<UserOrder> response = orderController.submit(usr.getUsername());
        UserOrder respondAsUserOrder = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(respondAsUserOrder);

        assertEquals(dummyUserOrder.getId(), respondAsUserOrder.getId());
        assertEquals(dummyUserOrder.getItems(), respondAsUserOrder.getItems());
        assertEquals(dummyUserOrder.getTotal(), respondAsUserOrder.getTotal());
        assertEquals(dummyUserOrder.getUser(), respondAsUserOrder.getUser());
    }

    @Test
    public void verify_get_order_by_username(){
        when(userRepo.findByUsername(dummyUser.getUsername())).thenReturn(dummyUser);
        List<UserOrder> userOrders = new ArrayList<>();
        userOrders.add(dummyUserOrder);
        when(orderRepo.findByUser(dummyUser)).thenReturn(userOrders);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(dummyUser.getUsername());
        List<UserOrder> respondAsUserOrder = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(respondAsUserOrder);
        assertEquals(1, respondAsUserOrder.size());
        assertEquals(dummyUserOrder.getId(), respondAsUserOrder.get(0).getId());
        assertEquals(dummyUserOrder.getTotal(), respondAsUserOrder.get(0).getTotal());
        assertEquals(dummyUserOrder.getItems(), respondAsUserOrder.get(0).getItems());
        assertEquals(dummyUserOrder.getUser(), respondAsUserOrder.get(0).getUser());
    }

}
