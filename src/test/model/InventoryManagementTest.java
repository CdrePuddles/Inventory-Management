package model;

import exceptions.IllegalQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryManagementTest {

    private InventoryManagement testInventoryList;
    private LinkedList<InventoryItem> testInventoryList2;

    private InventoryItem testItem1;
    private InventoryItem testItem2;
    private InventoryItem testItem3;
    private InventoryItem testItem4;

    @BeforeEach
    void setup() {

        testItem1 = new InventoryItem(1, "Widget A", 10, "This is the first widget");
        testItem2 = new InventoryItem(2, "Widget B", 25, "This is the second widget");
        testItem3 = new InventoryItem(3, "Widget C", 0, "This is the third widget");
        testItem4 = new InventoryItem(4, "Zubat", 6, "This is the fourth item, not a widget");

        testInventoryList = new InventoryManagement();
    }

    @Test
    void testConstructor() {
        testInventoryList.addItem("Widget A", 10, "This is the first widget");

        InventoryItem getItem = testInventoryList.getItem(0);
        assertEquals(testItem1.getId(), getItem.getId());
        assertEquals(testItem1.getTitle(), getItem.getTitle());
        assertEquals(testItem1.getQuantity(), getItem.getQuantity());
        assertEquals(testItem1.getDescription(), getItem.getDescription());

    }

    @Test
    void testGetItem() {
        addThreeItems();
        InventoryItem getItem = testInventoryList.getItem(1);
        assertEquals(testItem2.getId(), getItem.getId());
        assertEquals(testItem2.getTitle(), getItem.getTitle());
        assertEquals(testItem2.getQuantity(), getItem.getQuantity());
        assertEquals(testItem2.getDescription(), getItem.getDescription());

    }

    @Test
    void testGetList() {
        testInventoryList.addItem("Widget A", 10, "This is the first widget");
        assertEquals(1, testInventoryList.getList().size());
    }

    @Test
    void testGetListMultipleItems() {
        addThreeItems();

        assertEquals(3, testInventoryList.getList().size());
    }

    @Test
    void testGetListNoItems() {
        assertEquals(0, testInventoryList.getList().size());
    }

    @Test
    void testAddItem() {
        assertEquals(0, testInventoryList.getListSize());

        testInventoryList.addItem("Widget A", 10, "This is the first widget");

        assertEquals(1, testInventoryList.getListSize());

    }

    @Test
    void testAddMultipleItems() {
        assertEquals(0, testInventoryList.getListSize());

        addThreeItems();

        assertEquals(3, testInventoryList.getListSize());

    }

    @Test
    void testAddItemIllegalQuantity() {
        assertEquals(0, testInventoryList.getListSize());

        assertThrows(IllegalQuantityException.class,
                () -> {
                    testInventoryList.addItem("Widget A", -10, "This is the first widget");
                });

        assertEquals(0, testInventoryList.getListSize());

    }

    @Test
    void testRemoveItem() {
        addThreeItems();
        testInventoryList.removeItem(1);
        assertEquals(2, testInventoryList.getListSize());
    }

    @Test
    void testRemoveMultipleItems() {
        addThreeItems();

        testInventoryList.removeItem(1);
        testInventoryList.removeItem(3);

        assertEquals(1, testInventoryList.getListSize());
    }

    @Test
    void testRemoveItemNoItem() {
        addThreeItems();
        testInventoryList.removeItem(10);
        assertEquals(3, testInventoryList.getListSize());
    }

    @Test
    void testAssignIdFirstItem() {
        assertEquals(1, testInventoryList.assignId());
    }

    @Test
    void testAssignIdExistingItems() {
        addThreeItems();

        assertEquals(4, testInventoryList.assignId());
    }

    @Test
    void testGetItemFromId() {
        addThreeItems();

        assertEquals(testItem1.getId(), testInventoryList.getItemFromId(1).getId());
        assertEquals(testItem1.getTitle(), testInventoryList.getItemFromId(1).getTitle());
        assertEquals(testItem1.getQuantity(), testInventoryList.getItemFromId(1).getQuantity());
        assertEquals(testItem1.getDescription(), testInventoryList.getItemFromId(1).getDescription());
    }

    @Test
    void testGetItemFromTitle() {
        // may need to work on this
        addThreeItems();
        testInventoryList2 = testInventoryList.getItemsFromTitle("Widget B");
        assertEquals(1, testInventoryList2.size());

        assertEquals(testItem2.getId(), testInventoryList2.get(0).getId());
        assertEquals(testItem2.getTitle(), testInventoryList2.get(0).getTitle());
        assertEquals(testItem2.getQuantity(), testInventoryList2.get(0).getQuantity());
        assertEquals(testItem2.getDescription(), testInventoryList2.get(0).getDescription());
    }

    @Test
    void testGetMultipleItemsFromTitle() {
        // may need to work on this
        addThreeItems();

        //note that this 4th item will not appear in the list below as it is not a widget
        testInventoryList.addItem("Zubat", 6,"This is the fourth item, not a widget");
        assertEquals(4, testInventoryList.getListSize());

        testInventoryList2 = testInventoryList.getItemsFromTitle("Widget");
        assertEquals(3, testInventoryList2.size());

        assertEquals(testItem1.getId(), testInventoryList2.get(0).getId());
        assertEquals(testItem1.getTitle(), testInventoryList2.get(0).getTitle());
        assertEquals(testItem1.getQuantity(), testInventoryList2.get(0).getQuantity());
        assertEquals(testItem1.getDescription(), testInventoryList2.get(0).getDescription());

        assertEquals(testItem2.getId(), testInventoryList2.get(1).getId());
        assertEquals(testItem2.getTitle(), testInventoryList2.get(1).getTitle());
        assertEquals(testItem2.getQuantity(), testInventoryList2.get(1).getQuantity());
        assertEquals(testItem2.getDescription(), testInventoryList2.get(1).getDescription());

        assertEquals(testItem3.getId(), testInventoryList2.get(2).getId());
        assertEquals(testItem3.getTitle(), testInventoryList2.get(2).getTitle());
        assertEquals(testItem3.getQuantity(), testInventoryList2.get(2).getQuantity());
        assertEquals(testItem3.getDescription(), testInventoryList2.get(2).getDescription());

    }

    @Test
    void testGetItemsFromTitleNotExist() {
        // may need to work on this
        addThreeItems();
        assertEquals(0, testInventoryList.getItemsFromTitle("Widget Z").size());
    }

    @Test
    void testGetPositionOfItem() {
        addThreeItems();
        // remember zero-based indexing!
        assertEquals(1, testInventoryList.getPositionOfItem(2));
    }

    @Test
    void testGetLastIdInList() {
        addThreeItems();
        assertEquals(3, testInventoryList.getLastIdInList());
    }

    @Test
    void testHasItem() {
        addThreeItems();
        assertTrue(testInventoryList.hasItem(1));
    }

    @Test
    void testHasNoItem() {
        addThreeItems();
        assertFalse(testInventoryList.hasItem(6));
    }

    private void addThreeItems() {
        testInventoryList.addItem("Widget A", 10, "This is the first widget");
        testInventoryList.addItem("Widget B", 25, "This is the second widget");
        testInventoryList.addItem("Widget C", 0, "This is the third widget");
    }


}
