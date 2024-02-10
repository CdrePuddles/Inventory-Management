package model;

public class InventoryItem {

    private int id;                 // unique indicator for inventory item
    private String title;           // title of inventory item
    private int quantity;           // quantity of inventory item ( >0)
    private String description;     // description of inventory item

    public InventoryItem(int id, String title, int quantity, String description) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.description = description;
    }

    // EFFECTS:     determines if the quantity update should be increase or decrease.
    //              if value is 0, do not process quantity update.
    public void updateQuantity(int quantity) {
        if (quantity > 0) {
            increaseQuantity(quantity);
        } else if (quantity < 0) {
            decreaseQuantity(quantity);
        } else {
            // may need to try-catch?
            System.out.println("No quantity changed - input value is 0.");
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
    //              if value would fall below zero, output exception
    protected void decreaseQuantity(int decrease) {
        int quantityDecrease = this.quantity + decrease;
        if (quantityDecrease < 0) {
            // may need to try-catch?
            System.out.println("No quantity changed - quantity cannot be negative.");
        } else {
            this.quantity = quantityDecrease;
        }
    }

    //getters
    public int getId() {

        return this.id;
    }

    public String getTitle() {

        return this.title;
    }

    public int getQuantity() {

        return this.quantity;
    }

    public String getDescription() {

        return this.description;
    }

    //setters
    // MODIFIES:    this
    // EFFECTS:     sets the initial item id on item creation
    protected void setId(int id) {
        this.id = id;
    }

    // MODIFIES:    this
    // EFFECTS:     sets the item title to the provided title
    protected void setTitle(String title) {
        this.title = title;
    }

    // MODIFIES:    this
    // EFFECTS:     sets the item title to the provided title
    protected void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // MODIFIES:    this
    // EFFECTS:     sets the item description to the provided title
    protected void setDescription(String description) {
        this.description = description;
    }
}
