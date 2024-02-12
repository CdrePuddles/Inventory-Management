package ui;

import exceptions.CannotFindItemIdException;
import exceptions.CannotFindItemTitleException;
import exceptions.IllegalQuantityException;
import model.InventoryManagement;

import java.util.Scanner;

public class InventoryApp {

    private InventoryManagement inventoryList;
    private Scanner input;

    // EFFECTS: starts running the inventory application
    public InventoryApp() {
        runInventoryManagement();
    }

    private void runInventoryManagement() {
        init();
        runInitialMenu();

    }

    // MODIFIES:    this
    // EFFECTS:     processes user input
    // CREDIT:      this portion is substantively modelled off of the AccountNotRobust TellerApp
    //              provided as a reference for the term project
    private void runInitialMenu() {
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
            runSearchMenu();
        } else if (command.equals("a")) {
            doAddItem();
        } else if (command.equals("r")) {
            //doRemoveItem();
        } else if (command.equals("e")) {
            //doEditItemMenu();
        } else {
            System.out.println("Invalid selection, please try again!\n");
        }
    }

    // EFFECTS:     view every item in the list in the order they were added
    private void doViewList() {
        String inventoryText = inventoryList.getList();
        if (inventoryText == "") {
            System.out.println("There are currently no items in the inventory system, returning to main menu.. \n");
            runInventoryManagement();
        } else {
            System.out.println(inventoryText);
        }
    }

    //TODO
    // MODIFIES:    this
    // EFFECTS:     processes user input
    // CREDIT:      this portion is substantively modelled offs of the AccountNotRobust TellerApp
    //              provided as a reference for the term project
    private void runSearchMenu() {
        boolean processNext = true;
        String command = null;

        //displaySearchMenu();

        while (processNext) {
            displaySearchMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                processNext = false;
            } else {
                processSearchMenu(command);
            }
        }

        System.out.println("This is what happens when pressing b \n");
    }

    //TODO
    // MODIFIES:    this
    // EFFECTS:     user inputs the item title, quantity, and description to be added to the list.
    private void processSearchMenu(String command) {
        if (command.equals("i")) {
            doSearchById();
        } else if (command.equals("t")) {
            doSearchByTitle();
        } else if (command.equals("b")) {
            displayInitialMenu();
            runInitialMenu();
        } else {
            System.out.println("Invalid selection, please try again! \n");
        }

    }

    //TODO
    // MODIFIES:    this
    // EFFECTS:     user inputs the item ID, which outputs the item's ID, title, description, and quantity.
    //              if item does not exist, throw error?
    private void doSearchById() {
        System.out.println("Enter item ID: ");
        String idStr = input.next();

        try {
            int searchId = Integer.parseInt(idStr);

            try {
                String output = inventoryList.getItemFromId(searchId);
                System.out.println(output);
            } catch (CannotFindItemIdException e) {
                System.out.println("Could not find an inventory item with the following ID: " + searchId + "\n");
            }

        } catch (NumberFormatException e) {
            System.out.println("Illegal value - please enter a number for ID! \n");
        } finally {
            System.out.println("Returning to previous menu... \n");
        }


    }

    //TODO
    // MODIFIES:    this
    // EFFECTS:     user inputs the item ID, which outputs the item's ID, title, description, and quantity.
    //              if item does not exist, throw error?
    private void doSearchByTitle() {
        // TRY... CATCH...
        System.out.println("Please note the system will provide any item which contains part of your input.");
        System.out.println("Enter item Title: ");
        String searchTitle = input.next();


        try {
            String output = inventoryList.getItemsFromTitle(searchTitle);
            System.out.println(output);
        } catch (CannotFindItemTitleException e) {
            System.out.println("Could not find any inventory item containing this value: " + searchTitle + "\n");
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
        String quantityStr = addItem.nextLine();

        try {
            int quantity = Integer.parseInt(quantityStr);

            try {
                inventoryList.addItem(title, quantity, description);
                System.out.println("Item successfully added with ID " + inventoryList.getLastIdInList() + "!\n");
            } catch (IllegalQuantityException e) {
                System.out.println("Please enter a quantity 0 or greater!  Returning to main menu... \n");
            }
        } catch (NumberFormatException e) {
            System.out.println("Illegal value - please enter a number for quantity!  Returning to main menu... \n");
        }

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
