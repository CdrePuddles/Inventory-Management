package persistence;

import model.InventoryItem;
import model.InventoryManagement;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noFile.json");
        try {
            InventoryManagement im = reader.read();
            fail("Should have thrown IOException");
        } catch (IOException e) {
            System.out.println("Caught IOException");
        }
    }

    @Test
    void testReaderEmptyInventoryList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyInventoryList.json");
        try {
            InventoryManagement im = reader.read();
            assertEquals("testReaderEmptyInventoryList", im.getName());
            assertEquals(0, im.getListSize());
        } catch (IOException e) {
            fail("Should not have thrown exception - couldn't read from file.");
        }
    }

    @Test
    void testReaderValidInventoryList() {
        JsonReader reader = new JsonReader("./data/testReaderListWithValidItems.json");
        try {
            InventoryManagement im = reader.read();
            assertEquals("testReaderListWithValidItems", im.getName());
            List<InventoryItem> inventoryItemList = im.getList();
            assertEquals(2, im.getListSize());
            checkInventoryItem(1, "Item 1", 10, "This is the first item",
                    inventoryItemList.get(0));
            checkInventoryItem(2, "Item 2", 20, "This is the second item",
                    inventoryItemList.get(1));
        } catch (IOException e) {
            fail("Should not have thrown exception - couldn't read from file.");
        }
    }
}
