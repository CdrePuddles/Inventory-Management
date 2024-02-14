package model;

import exceptions.IllegalQuantityException;

import java.util.LinkedList;

// Represents a list of unique inventory items
public class InventoryManagement {

    private final LinkedList<InventoryItem> inventoryList;    // list of inventory items

    // EFFECTS:     Creates an inventory list with no InventoryItems added.
    public InventoryManagement() {
        this.inventoryList = new LinkedList<>();
    }

    // MODIFIES:    this
    // EFFECTS:     adds the provided inventory item to the end of the list,
    //              assigning a unique ID
    //              If provided quantity < 0, throw IllegalQuantityException()
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
    // EFFECTS:     checks through the entire list of unique IDs, and provides the next unique sequential value.
    //              returns 1 if no items exist in the list.
    protected int assignId() {
        int id = 1;
        for (int i = 0; i < getListSize(); i++) {
            InventoryItem item = this.inventoryList.get(i);
            id = item.getId() + 1;
        }
        return id;
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

    // getters
    // REQUIRES:    hasItem(id) = true
    // EFFECTS:     check the IDs in the list against the provided ID.
    //              provide corresponding item if the ID can be found in the list
    public InventoryItem getItemFromId(int id) {
        InventoryItem ret = null;
        for (int i = 0; i < getListSize(); i++) {
            InventoryItem item = this.inventoryList.get(i);
            if (item.getId() == id) {
                ret = this.inventoryList.get(i);
            }
        }
        return ret;
    }

    // REQUIRES:    inventoryList.size > 0 and hasItem = true
    // EFFECTS:     check the titles in the list against the provided title.
    //              add to a temporary list any item whose title contains the provided title
    //              NOTE: case-insensitive.
    public LinkedList<InventoryItem> getItemsFromTitle(String text) {
        LinkedList<InventoryItem> ret = new LinkedList<>();
        for (int i = 0; i < getListSize(); i++) {
            InventoryItem item = this.inventoryList.get(i);
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                ret.add(item);
            }
        }
        return ret;
    }

    // REQUIRES:    hasItem(id) = true
    // EFFECTS:     return the position of the item corresponding to the provided ID
    //              return -1 if unable to find item
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

    // EFFECTS:     return a string of the entire list of inventory items and their parameters
    //              if not items exist in the list, return an empty string
    public LinkedList<InventoryItem> getList() {
        return this.inventoryList;
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
