package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InventoryManagementTest {

    private InventoryManagement testInventoryList;

    private InventoryItem testItem1;
    private InventoryItem testItem2;
    private InventoryItem testItem3;
    private String output3;

    @BeforeEach
    void setup() {

        testItem1 = new InventoryItem(1, "Widget A", 10, "This is the first widget");
        testItem2 = new InventoryItem(2, "Widget B", 25, "This is the second widget");
        testItem3 = new InventoryItem(3, "Widget C", 0, "This is the third widget");

        output3 = "ID: 3\n" +
                "Title: Widget C\n" +
                "Quantity 0\n" +
                "Description: This is the third widget\n";

        testInventoryList = new InventoryManagement();
    }

    @Test
    void testConstructor() {
        //do I need to do this?
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
    void testRemoveItemDoesNotExist() {
        addThreeItems();

        testInventoryList.removeItem(123);

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
    void testCheckItemExistsId() {
        // may need to work on this
        addThreeItems();

        testInventoryList.getItemFromId(2);

        //assertTrue(testInventoryList.checkItemExistsId(1));
    }

    @Test
    void testCheckItemDoesNotExistId() {
        // may need to work on this
        addThreeItems();

        //may need to try catch?
        //assertFalse(testInventoryList.checkItemExistsId(5));
    }

    @Test
    void testGetItemsFromTitle() {
        // may need to work on this
        addThreeItems();

        testInventoryList.getItemsFromTitle("Widget B");
    }

    @Test
    void testGetItemsFromTitleNotExist() {
        // may need to work on this
        addThreeItems();

        //assertFalse(testInventoryList.checkItemExistsTitle("Widget Z"));
    }

    @Test
    void testGetPositionOfItem() {
        addThreeItems();
        // remember zero-based indexing!
        assertEquals(1, testInventoryList.getPositionOfItem(2));
    }

    @Test
    void testOutputItem() {
        addThreeItems();

        assertEquals(output3, testInventoryList.outputItem(2));
    }


    private void addThreeItems() {
        testInventoryList.addItem("Widget A", 10, "This is the first widget");
        testInventoryList.addItem("Widget B", 25, "This is the second widget");
        testInventoryList.addItem("Widget C", 0, "This is the third widget");
    }


}
