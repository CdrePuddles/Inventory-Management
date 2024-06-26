package model;

import exceptions.NegativeQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryItemTest {

    private InventoryItem testItem;

    @BeforeEach
    void setup() {
        testItem = new InventoryItem(1, "Widget A", 10, "This is the first widget");
    }

    @Test
    void testConstructor() {
        assertEquals(1, testItem.getId());
        assertEquals("Widget A", testItem.getTitle());
        assertEquals(10, testItem.getQuantity());
        assertEquals("This is the first widget", testItem.getDescription());
    }

    @Test
    void testQuantityPositive() {
        assertEquals(10, testItem.getQuantity());
        testItem.updateQuantity(5);
        assertEquals(15, testItem.getQuantity());
    }

    @Test
    void testQuantityNegative() {
        assertEquals(10, testItem.getQuantity());
        testItem.updateQuantity(-5);
        assertEquals(5, testItem.getQuantity());
    }

    @Test
    void testQuantityZero() {
        assertEquals(10, testItem.getQuantity());
        testItem.updateQuantity(0);
        assertEquals(10, testItem.getQuantity());
    }

    @Test
    void testIncreaseQuantity() {
        assertEquals(10, testItem.getQuantity());
        testItem.increaseQuantity(5);
        assertEquals(15, testItem.getQuantity());
    }

    @Test
    void testDecreaseQuantity() {
        assertEquals(10, testItem.getQuantity());
        testItem.decreaseQuantity(-5);
        assertEquals(5, testItem.getQuantity());
    }

    // CREDIT:      Initial groundwork provided from following stackoverflow link:
    //              https://stackoverflow.com/questions/40268446/junit-5-how-to-assert-an-exception-is-thrown
    @Test
    void testDecreaseQuantityCannotNegative() {
        assertEquals(10, testItem.getQuantity());

        assertThrows(NegativeQuantityException.class,
                () -> {
                    testItem.decreaseQuantity(-15);
                });
        assertEquals(10, testItem.getQuantity());
    }

    @Test
    void testSetId() {
        testItem.setId(2);
        assertEquals(2, testItem.getId());

    }

    @Test
    void testSetTitle() {
        testItem.setTitle("Widget B");
        assertEquals("Widget B", testItem.getTitle());

    }

    @Test
    void testSetQuantity() {
        testItem.setQuantity(20);
        assertEquals(20, testItem.getQuantity());

    }

    @Test
    void testSetDescription() {
        testItem.setDescription("This is a revised first widget");
        assertEquals("This is a revised first widget", testItem.getDescription());
    }
}
