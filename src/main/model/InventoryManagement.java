package model;

import exceptions.IllegalQuantityException;

import java.util.LinkedList;

// Represents a list of unique inventory items
public class InventoryManagement {

    private LinkedList<InventoryItem> inventoryList;    // list of inventory items

    // EFFECTS:     Creates an inventory list with no InventoryItems added.
    public InventoryManagement() {
        this.inventoryList = new LinkedList<InventoryItem>();
    }

    // MODIFIES:    this
    // EFFECTS:     adds the provided inventory item to the end of the list,
    //              assigning an unique ID
    public void addItem(String title, int quantity, String description) {
        if (quantity < 0) {
            throw new IllegalQuantityException();
        }
        InventoryItem inventoryItem = new InventoryItem(assignId(), title, quantity, description);
        this.inventoryList.add(inventoryItem);
    }

    // REQUIRES:    hasItem(id) = true
    // MODIFIES:    this
    // EFFECTS:     removes the inventory item corresponding to the provided ID.
    public void removeItem(int id) {
        int itemPosition = getPositionOfItem(id);
        if (itemPosition >= 0) {
            this.inventoryList.remove(itemPosition);
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

    // EFFECTS:     print the entire list of inventory items and their parameters
    public String getList() {
        String output = "";
        for (int i = 0; i < getListSize(); i++) {
            output += outputItem(i);
        }
        return output;
    }

    // REQUIRES:    inventoryList.get(i) exists
    // EFFECTS:     output the InventoryItem corresponding to the provided i-th row
    protected String outputItem(int i) {
        InventoryItem item = this.inventoryList.get(i);

        // I think these will need to change to return a string, not print
        String id = "ID: " + item.getId() + "\n";
        String title = "Title: " + item.getTitle() + "\n";
        String quantity = "Quantity: " + item.getQuantity() + "\n";
        String description = "Description: " + item.getDescription() + "\n";

        String output = id + title + quantity + description + "\n";

        return output;
    }

    // getters
    // REQUIRES:    hasItem(id) = true
    // EFFECTS:     check the IDs in the list against the provided ID.
    //              output corresponding item if the ID can be found in the list
    public String getItemFromId(int id) {
        String output = "";
        for (int i = 0; i < getListSize(); i++) {
            InventoryItem item = this.inventoryList.get(i);
            if (item.getId() == id) {
                output = outputItem(i);
            }
        }
        return output;
    }

    // REQUIRES:    inventoryList.size > 0 and hasItem = true
    // EFFECTS:     check the titles in the list against the provided title.
    //              true if the title can be found in the list, false if not.
    //              NOTE: the list will provide every item which satisfies the condition
    //              NOTE: case-insensitive.
    public String getItemsFromTitle(String text) {
        String output = "";
        for (int i = 0; i < getListSize(); i++) {
            InventoryItem item = this.inventoryList.get(i);
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                output += outputItem(i);
            }
        }
        return output;
    }

    // REQUIRES:    hasItem(id) = true
    // EFFECTS:     return the position of the item corresponding to the provided ID
    //              throw CannotFindItemException() if unable to find item
    public int getPositionOfItem(int id) {
        int position = -1;
        for (int i = 0; i < getListSize(); i++) {
            InventoryItem item = this.inventoryList.get(i);
            if (item.getId() == id) {
                position = i;
            }
        }
        return position;
    }

    // EFFECTS:     return true if the item id can be found in the list
    //              else, return false
    public boolean hasItem(int id) {
        for (int i = 0; i < getListSize(); i++) {
            InventoryItem item = this.inventoryList.get(i);
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS:     get the size of the inventory list
    public int getListSize() {
        return this.inventoryList.size();
    }

    // EFFECTS:     get the size of the inventory list
    public InventoryItem getItem(int position) {
        return this.inventoryList.get(position);
    }

    // REQUIRES:    this.inventoryList.getListSize() > 0
    // EFFECTS:     returns the last ID in the list, the last ID will be found in the last entry of the list
    //              useful for outputting the ID of the most recently added item
    public int getLastIdInList() {
        return this.inventoryList.get(getListSize() - 1).getId();
    }
}
