package model;

import java.util.LinkedList;

public class InventoryManagement {

    private LinkedList<InventoryItem> inventoryList;

    // constructor, not adding anything initially yet
    public InventoryManagement() {
        this.inventoryList = new LinkedList<InventoryItem>();
    }

    // EFFECTS:     print the entire list of items, return null if empty
    public String getList() {
        String output = "";
        for (int i = 0; i < getListSize(); i++) {
            output += outputItem(i) + "\n";
        }
        return output;
    }

    // MODIFIES:    this
    // EFFECTS:     adds the provided inventory item to the end of the list,
    //              assigning an unique ID
    public void addItem(String title, int quantity, String description) {
        InventoryItem inventoryItem = new InventoryItem(assignId(), title, quantity, description);
        this.inventoryList.add(inventoryItem);
    }

    // MODIFIES:    this
    // EFFECTS:     removes the inventory item corresponding to the provided ID.
    //              assigning a unique ID
    public void removeItem(int id) {
        // likley need to try catch
        int itemPosition = getPositionOfItem(id);
        if (itemPosition >= 0) {
            this.inventoryList.remove(itemPosition);
        } else {
            //need to try catch?
            // NEED TO DELETE
            System.out.println("Item cannot be found in the system");
        }

    }

    // MODIFIES:    this
    // EFFECTS:     checks through the entire list of unique IDs, and provides the next unique sequential value
    protected int assignId() {
        int id = 1;
        for (int i = 0; i < getListSize(); i++) {
            InventoryItem item = this.inventoryList.get(i);
            id = item.getId() + 1;
        }
        return id;
    }



    // REQUIRES:    checkItemExistsId = true
    // EFFECTS:     output the InventoryItem corresponding to the provided i-th row
    protected String outputItem(int i) {
        InventoryItem item = this.inventoryList.get(i);

        // I think these will need to change to return a string, not print
        String id = "ID: " + item.getId() + "\n";
        String title = "Title: " + item.getTitle() + "\n";
        String quantity = "Quantity " + item.getQuantity() + "\n";
        String description = "Description: " + item.getDescription() + "\n";

        return id + title + quantity + description;
    }

    // getters
    // REQUIRES:    inventoryList.size > 0
    // EFFECTS:     check the IDs in the list against the provided ID.
    //              output corresponding item if the ID can be found in the list
    public void getItemFromId(int id) {
        for (int i = 0; i < getListSize(); i++) {
            InventoryItem item = this.inventoryList.get(i);
            if (item.getId() == id) {
                outputItem(i);
            }
        }
    }

    // REQUIRES:    inventoryList.size > 0
    // EFFECTS:     check the titles in the list against the provided title.
    //              true if the title can be found in the list, false if not.
    //              NOTE: the list will provide every item which satisfies the condition
    //              NOTE: case-insensitive.
    public void getItemsFromTitle(String text) {
        for (int i = 0; i < getListSize(); i++) {
            InventoryItem item = this.inventoryList.get(i);
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                outputItem(i);
            }
        }

        //return false;
    }

    // EFFECTS:     return the position of the item corresponding to the provided ID
    //              return -1 if ID cannot be found
    public int getPositionOfItem(int id) {
        for (int i = 0; i < getListSize(); i++) {
            InventoryItem item = this.inventoryList.get(i);
            if (item.getId() == id) {
                return i;
            }
        }
        return -1;
    }

    // EFFECTS:     get the size of the inventory list
    public int getListSize() {

        return this.inventoryList.size();
    }


}
