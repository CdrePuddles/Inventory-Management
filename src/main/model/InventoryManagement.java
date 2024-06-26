package model;

import exceptions.IllegalQuantityException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.LinkedList;

// Represents a list (with a name) of unique inventory items, each having an id, title, quantity, and description
public class InventoryManagement implements Writable {
    private String name;                                      // name of the inventory list
    private final LinkedList<InventoryItem> inventoryList;    // list of inventory items

    // EFFECTS:     Creates an inventory list with no InventoryItems added.
    //              logs the creation, or loading, of an inventory list

    public InventoryManagement(String name) {
        this.name = name;
        this.inventoryList = new LinkedList<>();
        EventLog.getInstance().logEvent(new Event("Created a new list named " + name));

    }

    // MODIFIES:    this
    // EFFECTS:     adds the provided inventory item to the end of the list,
    //              assigning a unique ID
    //              If provided quantity < 0, throw IllegalQuantityException()
    //              logs the addition of an item
    public void addItem(String title, int quantity, String description) {
        if (quantity < 0) {
            throw new IllegalQuantityException();
        }
        InventoryItem inventoryItem = new InventoryItem(assignId(), title, quantity, description);
        this.inventoryList.add(inventoryItem);
        String event = String.format("Added item with ID: %d, Title: %s, Quantity: %d, and Description: %s",
                inventoryItem.getId(), title, quantity, description);
        EventLog.getInstance().logEvent(new Event(event));
    }

    // MODIFIES:    this
    // EFFECTS:     adds the provided inventory item from the saved JSON file to the end of the list
    public void addItemJson(InventoryItem inventoryItem) {
        this.inventoryList.add(inventoryItem);
    }

    // REQUIRES:    hasItem(id) = true
    // MODIFIES:    this
    // EFFECTS:     removes e inventory item corresponding to the provided ID.
    //              logs removal of an item, or no removal of item if item does not exist.
    public void removeItem(int id) {
        int itemPosition = getPositionOfItem(id);
        String event = "Did not remove item, no such item exists with ID: " + id;
        if (itemPosition >= 0) {
            event = String.format("Removed item with ID: %d, Title: %s, Quantity: %d, and Description: %s",
                    inventoryList.get(itemPosition).getId(), inventoryList.get(itemPosition).getTitle(),
                    inventoryList.get(itemPosition).getQuantity(), inventoryList.get(itemPosition).getDescription());
            this.inventoryList.remove(itemPosition);
        }
        EventLog.getInstance().logEvent(new Event(event));

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

    // EFFECTS:     get the name of the inventory list
    public String getName() {
        return this.name;
    }


    // REQUIRES:    hasItem(id) = true
    // EFFECTS:     check the IDs in the list against the provided ID.
    //              provide corresponding item if the ID can be found in the list
    //              logs parameters of the item found, or logs being unable to find an item with the associated ID
    public InventoryItem getItemFromId(int id) {
        InventoryItem ret = null;
        String event = "Did not find any item with ID: " + id;

        for (int i = 0; i < getListSize(); i++) {
            InventoryItem item = this.inventoryList.get(i);
            if (item.getId() == id) {
                ret = this.inventoryList.get(i);
                event = String.format("Found Item with ID: %d, with Title: %s, Quantity: %d, and Description: %s",
                        inventoryList.get(i).getId(), inventoryList.get(i).getTitle(),
                        inventoryList.get(i).getQuantity(), inventoryList.get(i).getDescription());
            }
        }
        EventLog.getInstance().logEvent(new Event(event));

        return ret;
    }

    // REQUIRES:    inventoryList.size > 0
    // EFFECTS:     check the titles in the list against the provided title.
    //              add to a temporary list any item whose title contains the provided title
    //              if no criteria is matched, return an empty list
    //              NOTE: case-insensitive.
    //              logs parameters of the item(s) found, or logs being unable to find an item(s) containing the string
    public LinkedList<InventoryItem> getItemsFromTitle(String text) {
        LinkedList<InventoryItem> ret = new LinkedList<>();
        int itemCount = 0;
        for (int i = 0; i < getListSize(); i++) {
            InventoryItem item = this.inventoryList.get(i);
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                ret.add(item);
                itemCount++;
            }
        }
        EventLog.getInstance().logEvent(new Event("Found " + itemCount + " item(s) containing \"" + text + "\"."));
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

    // EFFECTS:     get the entire list of inventory items
    //              logs each time the list is viewed
    public LinkedList<InventoryItem> getList() {
        EventLog.getInstance().logEvent(new Event("Viewing list " + this.getName() + "."));
        return this.inventoryList;
    }

    // EFFECTS:     get the size of the inventory list
    public int getListSize() {
        return this.inventoryList.size();
    }

    // EFFECTS:     get the individual item at the specified position
    public InventoryItem getItem(int position) {
        return this.inventoryList.get(position);
    }

    // REQUIRES:    this.inventoryList.getListSize() > 0
    // EFFECTS:     returns the last ID in the list, the last ID will be found in the last entry of the list
    //              useful for outputting the ID of the most recently added item
    public int getLastIdInList() {
        return this.inventoryList.get(getListSize() - 1).getId();
    }

    // jsons
    // CREDIT:      this portion is substantively modelled off of the AccountNotRobust TellerApp
    //              provided as a reference for the term project

    // EFFECTS:    creates a new JSON object of the entire inventory list
    //             logs if the list is saved

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        // TODO
        json.put("items", itemsToJson());

        EventLog.getInstance().logEvent(new Event("Successfully saved " + this.name + "."));

        return json;
    }

    // EFFECTS:     returns items in this inventory list as a JSON array
    private JSONArray itemsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (InventoryItem item : inventoryList) {
            jsonArray.put(item.toJson());
        }
        return jsonArray;
    }

}
