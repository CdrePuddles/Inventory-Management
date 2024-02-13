package model;

import exceptions.CannotFindItemException;
import exceptions.IllegalQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryManagementTest {

    private InventoryManagement testInventoryList;

    private InventoryItem testItem1;
    private InventoryItem testItem2;
    private InventoryItem testItem3;

    private String output1;
    private String output2;
    private String output3;

    @BeforeEach
    void setup() {

        testItem1 = new InventoryItem(1, "Widget A", 10, "This is the first widget");
        testItem2 = new InventoryItem(2, "Widget B", 25, "This is the second widget");
        testItem3 = new InventoryItem(3, "Widget C", 0, "This is the third widget");

        output1 = "ID: 1\n" +
                "Title: Widget A\n" +
                "Quantity: 10\n" +
                "Description: This is the first widget\n\n";

        output2 = "ID: 2\n" +
                "Title: Widget B\n" +
                "Quantity: 25\n" +
                "Description: This is the second widget\n\n";

        output3 = "ID: 3\n" +
                "Title: Widget C\n" +
                "Quantity: 0\n" +
                "Description: This is the third widget\n\n";

        testInventoryList = new InventoryManagement();
    }

    @Test
    void testConstructor() {
        //do I need to do this?
    }

    @Test
    void testGetList() {
        testInventoryList.addItem("Widget A", 10, "This is the first widget");
        assertEquals(output1, testInventoryList.getList());
    }

    @Test
    void testGetListMultipleItems() {
        addThreeItems();

        assertEquals(output1 + output2 + output3, testInventoryList.getList());
    }

    @Test
    void testGetListNoItems() {
        assertEquals("", testInventoryList.getList());
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
        // may need to work on this
        addThreeItems();

        assertEquals(output1, testInventoryList.getItemFromId(1));
    }

/*    // CREDIT:      Initial groundwork provided from following stackoverflow link:
    //              https://stackoverflow.com/questions/40268446/junit-5-how-to-assert-an-exception-is-thrown
    @Test
    void testCheckItemDoesNotExistId() {
        addThreeItems();
        assertThrows(CannotFindItemException.class,
                () -> {
                    testInventoryList.getItemFromId(5);
                });
    }*/

    @Test
    void testGetItemFromTitle() {
        // may need to work on this
        addThreeItems();

        assertEquals(output2, testInventoryList.getItemsFromTitle("Widget B"));
    }

    @Test
    void testGetMultipleItemsFromTitle() {
        // may need to work on this
        addThreeItems();

        assertEquals(output1 + output2 + output3, testInventoryList.getItemsFromTitle("Widget"));
    }

/*    @Test
    void testGetItemsFromTitleNotExist() {
        // may need to work on this
        addThreeItems();

        assertThrows(CannotFindItemException.class,
                () -> {
                    testInventoryList.getItemsFromTitle("Widget Z");
                });
    }*/

    @Test
    void testGetPositionOfItem() {
        addThreeItems();
        // remember zero-based indexing!
        assertEquals(1, testInventoryList.getPositionOfItem(2));
    }

/*    @Test
    void testGetPositionOfItemNotExist() {
        addThreeItems();
        // remember zero-based indexing!
        assertThrows(CannotFindItemException.class,
                () -> {
                    testInventoryList.getPositionOfItem(8);
                });
    }*/

    @Test
    void testOutputItem() {
        addThreeItems();

        assertEquals(output3, testInventoryList.outputItem(2));
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
