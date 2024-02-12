package model;

import exceptions.CannotFindItemException;
import exceptions.CannotFindItemIdException;
import exceptions.IllegalQuantityException;

import java.util.LinkedList;

import static java.util.Objects.isNull;

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
            output += outputItem(i);
        }
        return output;
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
            //TODO
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
        String quantity = "Quantity: " + item.getQuantity() + "\n";
        String description = "Description: " + item.getDescription() + "\n";

        return id + title + quantity + description + "\n";
    }

    // getters
    // REQUIRES:    inventoryList.size > 0
    // EFFECTS:     check the IDs in the list against the provided ID.
    //              output corresponding item if the ID can be found in the list
    public String getItemFromId(int id) {
        for (int i = 0; i < getListSize(); i++) {
            InventoryItem item = this.inventoryList.get(i);
            if (item.getId() == id) {
                return outputItem(i);
            }
        }
        throw new CannotFindItemException();
    }

    // REQUIRES:    inventoryList.size > 0
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
        if (output == "") {
            throw new CannotFindItemException();
        } else {
            return output;
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

    // REQUIRES:    this.inventoryList.getListSize() > 0
    // EFFECTS:     returns the last ID in the list, the last ID will be found in the last entry of the list
    //              useful for outputting the ID of the most recently added item
    public int getLastIdInList() {

        return this.inventoryList.get(getListSize() - 1).getId();
    }


}
