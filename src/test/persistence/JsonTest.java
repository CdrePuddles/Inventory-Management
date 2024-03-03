package persistence;

import model.InventoryItem;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkInventoryItem(int id, String title, int quantity, String description, InventoryItem item) {
        assertEquals(id, item.getId());
        assertEquals(title, item.getTitle());
        assertEquals(quantity, item.getQuantity());
        assertEquals(description, item.getDescription());
    }
}
