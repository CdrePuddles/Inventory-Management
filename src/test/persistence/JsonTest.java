package persistence;

import model.InventoryItem;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    // CREDIT:      this portion is substantively modelled off of the AccountNotRobust TellerApp
    //              provided as a reference for the term project

    protected void checkInventoryItem(int id, String title, int quantity, String description, InventoryItem item) {
        assertEquals(id, item.getId());
        assertEquals(title, item.getTitle());
        assertEquals(quantity, item.getQuantity());
        assertEquals(description, item.getDescription());
    }
}
