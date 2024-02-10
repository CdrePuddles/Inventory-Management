package ui;

import model.InventoryManagement;

import java.util.Scanner;

public class InventoryApp {

    private InventoryManagement inventoryList;
    private Scanner input;

    // EFFECTS: starts running the inventory application
    public InventoryApp() {
        runInventoryManagement();
    }

    // MODIFIES:    this
    // EFFECTS:     processes user input
    // CREDIT:      this portion is substantively modelled off of the AccountNotRobust TellerApp
    //              provided as a reference for the term project
    private void runInventoryManagement() {
        boolean processNext = true;
        String command = null;

        init();

        while (processNext) {
            displayInitialMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                processNext = false;
            } else {
                processInitialMenu(command);
            }
        }

        System.out.println("Thanks for using the system!");
    }


    // MODIFIES:    this
    // EFFECTS:     initializes inventory list
    // CREDIT:      this portion is substantively modelled off of the AccountNotRobust TellerApp
    //              provided as a reference for the term project
    private void init() {
        inventoryList = new InventoryManagement();

        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // EFFECTS:     processes the initial menu of options
    //              v = view list
    //              s = search for item
    //              a = add item
    //              r = remove item
    //              e = edit item
    // CREDIT:      this portion is substantively modelled off of the AccountNotRobust TellerApp
    //              provided as a reference for the term project
    private void processInitialMenu(String command) {
        if (command.equals("v")) {
            doViewList();
        } else if (command.equals("s")) {
            //doSearchMenu();
        } else if (command.equals("a")) {
            doAddItem();
        } else if (command.equals("r")) {
            //doRemoveItem();
        } else if (command.equals("e")) {
            //doEditItemMenu();
        } else {
            System.out.println("Invalid selection, please try again!");
        }
    }

    // EFFECTS:     view every item in the list in the order they were added
    private void doViewList() {
        String inventoryText = inventoryList.getList();
        if (inventoryText == "") {
            System.out.println("There are currently no items in the inventory system, returning to main menu..");
            runInventoryManagement();
        } else {
            System.out.println(inventoryText);
        }
    }

    // MODIFIES:    this
    // EFFECTS:     user inputs the item title, quantity, and description to be added to the list.
    private void doAddItem() {
        Scanner addItem = new Scanner(System.in);

        System.out.println("Enter item title: ");
        String title = addItem.nextLine();

        System.out.println("Enter item description: ");
        String description = addItem.nextLine();

        System.out.println("Enter initial item quantity (0 or greater): ");
        int quantity = addItem.nextInt();

        //TODO
        //LIKELY NEED TO ADD TRY CATCH
        inventoryList.addItem(title, quantity, description);
    }


    // EFFECTS:     displays menu of initial options available to user
    // CREDIT:      this portion is substantively modelled off of the AccountNotRobust TellerApp
    //              provided as a reference for the term project
    private void displayInitialMenu() {
        System.out.println("Welcome to the inventory management system!");
        System.out.println("Please select from the following options (case-insensitive):");
        System.out.println("\t v -> view current list of all inventory items");
        System.out.println("\t s -> search for item in the list");
        System.out.println("\t a -> add an item to the list");
        System.out.println("\t r -> remove an item from the list");
        System.out.println("\t e -> edit an item in the list");
        System.out.println("\t q -> quit the system");
    }

    // EFFECTS:     displays menu of initial options available to user
    private void displaySearchMenu() {
        System.out.println("You may search for an individual item by its unique identifier,");
        System.out.println("or search for all items which contain a piece of text.");
        System.out.println("\t i -> search by unique identifier");
        System.out.println("\t t -> search for items which include any part of an input");
        System.out.println("\t b -> go back to previous menu");
    }

    // EFFECTS:     displays menu of initial options available to user
    private void displayEditMenu() {
        System.out.println("Here you can edit various parameters of an existing item in the system");
        System.out.println("\t t -> title");
        System.out.println("\t q -> quantity");
        System.out.println("\t d -> description");
        System.out.println("\t b -> go back to previous menu");
    }

}
