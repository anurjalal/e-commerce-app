package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.utils.DataDummy;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static com.example.demo.utils.DataDummy.dummyItem;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    private static Item dummyItem = dummyItem();

    private static List<Item> dummy5Items = DataDummy.dummy5Items();

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void verify_get_all_items() {
        when(itemRepo.findAll()).thenReturn(dummy5Items);

        ResponseEntity<List<Item>> response = itemController.getItems();
        List<Item> respondAsItemList = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(respondAsItemList);
        assertEquals(dummy5Items.size(), respondAsItemList.size());
    }

    @Test
    public void verify_not_give_items_when_given_wrong_name() {
        dummy5Items.forEach(item -> when(itemRepo.findByName(item.getName())).thenReturn(dummy5Items));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Baby Yoda Doll");
        List<Item> respondAsItemList = response.getBody();

        assertEquals(404, response.getStatusCodeValue());
        assertNull(respondAsItemList);
    }

    @Test
    public void verify_give_item_by_id() {
        when(itemRepo.findById(dummyItem.getId())).thenReturn(Optional.of(dummyItem));
        ResponseEntity<Item> response = itemController.getItemById(dummyItem.getId());

        Item respondAsItem = response.getBody();

        assertEquals(200, response.getStatusCodeValue());

        assertEquals(dummyItem.getId(), respondAsItem.getId());
        assertEquals(dummyItem.getName(), respondAsItem.getName());
        assertEquals(dummyItem.getPrice(), respondAsItem.getPrice());
        assertEquals(dummyItem.getDescription(), respondAsItem.getDescription());
    }

}