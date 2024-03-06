package model;

import exceptions.NegativeQuantityException;
import org.json.JSONObject;
import persistence.Writable;

// Represents an inventory item having an id, title, quantity, and description
public class InventoryItem implements Writable {

    private int id;                 // unique indicator for inventory item
    private String title;           // title of inventory item
    private int quantity;           // quantity of inventory item ( >0)
    private String description;     // description of inventory item

    /*   EFFECTS: constructs an inventory item.
                  id is a positive integer not already assigned to an item
                  title is a non-unique string
                  quantity is any integer > 0
                  description is a non-unique depiction of what the item is
      */
    public InventoryItem(int id, String title, int quantity, String description) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.description = description;
    }

    // REQUIRES:    quantity != 0
    // EFFECTS:     determines if the quantity update should be increased or decreased.
    //              if value is 0, quantity update will not be processed.
    public void updateQuantity(int quantity) {
        if (quantity > 0) {
            increaseQuantity(quantity);
        } else if (quantity < 0) {
            decreaseQuantity(quantity);
        }
    }

    // REQUIRES:    int > 0
    // MODIFIES:    this
    // EFFECTS:     increase the existing quantity by the provided quantity number
    protected void increaseQuantity(int increase) {
        this.quantity = this.quantity + increase;
    }

    // REQUIRES:    int < 0
    // MODIFIES:    this
    // EFFECTS:     decrease the existing quantity by the provided quantity number.
    //              if value would fall below zero, throw  NegativeQuantityException()
    protected void decreaseQuantity(int decrease) {
        int quantityDecrease = this.quantity + decrease;
        if (quantityDecrease < 0) {
            throw new NegativeQuantityException();
        } else {
            this.quantity = quantityDecrease;
        }
    }

    //getters
    // EFFECTS:     get item id
    public int getId() {
        return this.id;
    }

    // EFFECTS:     get item title
    public String getTitle() {
        return this.title;
    }

    // EFFECTS:     get item quantity
    public int getQuantity() {
        return this.quantity;
    }

    // EFFECTS:     get item description
    public String getDescription() {
        return this.description;
    }

    //setters
    // MODIFIES:    this
    // EFFECTS:     sets the initial item id on item creation
    public void setId(int id) {
        this.id = id;
    }

    // MODIFIES:    this
    // EFFECTS:     sets the item title to the provided title
    public void setTitle(String title) {
        this.title = title;
    }

    // MODIFIES:    this
    // EFFECTS:     sets the item title to the provided title
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // MODIFIES:    this
    // EFFECTS:     sets the item description to the provided title
    public void setDescription(String description) {
        this.description = description;
    }

    // EFFECTS:     creates a JSON object of an inventory item
    // CREDIT:      this portion is substantively modelled off of the AccountNotRobust TellerApp
    //              provided as a reference for the term project
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("title", title);
        json.put("quantity", quantity);
        json.put("description", description);
        return json;
    }
}
