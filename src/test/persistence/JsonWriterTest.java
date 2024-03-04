package persistence;

import model.InventoryItem;
import model.InventoryManagement;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {

    private InventoryItem testItem1 =
            new InventoryItem(1, "Item 1", 10, "This is the first item");
    private InventoryItem testItem2 =
            new InventoryItem(2, "Item 2", 20, "This is the second item");

    @Test
    void testWriterInvalidFileName() {
        try {
            InventoryManagement im = new InventoryManagement("The Best List");
            JsonWriter writer = new JsonWriter("./data/bad\0name:here.json");
            writer.open();
            fail("Should have thrown IOException");
        } catch (IOException e) {
            //pass
        }
    }

    @Test
    void testWriterEmptyInventoryList() {
        try {
            InventoryManagement im = new InventoryManagement("Test Writer Empty Inventory List");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyInventoryList.json");
            writer.open();
            writer.write(im);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyInventoryList.json");
            im = reader.read();
            assertEquals("Test Writer Empty Inventory List", im.getName());
            assertEquals(0, im.getListSize());
        } catch (IOException e) {
            fail("Should not have through IOException");
        }
    }

    @Test
    void testWriterInventoryListHasItems() {
        try {
            InventoryManagement im = new InventoryManagement("Test Writer List Has Items");
            JsonWriter writer = new JsonWriter("./data/testWriterListHasItems.json");
            im.addItemJson(testItem1);
            im.addItemJson(testItem2);
            writer.open();
            writer.write(im);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterListHasItems.json");
            im = reader.read();
            assertEquals("Test Writer List Has Items", im.getName());
            List<InventoryItem> inventoryItemList = im.getList();
            assertEquals(2, im.getListSize());
            checkInventoryItem(1, "Item 1", 10, "This is the first item",
                    inventoryItemList.get(0));
            checkInventoryItem(2, "Item 2", 20, "This is the second item",
                    inventoryItemList.get(1));

        } catch (IOException e) {
            fail("Should not have through IOException");
        }
    }

}
