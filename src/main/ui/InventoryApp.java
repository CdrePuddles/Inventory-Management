package ui;

import exceptions.CannotFindItemException;
import exceptions.IllegalQuantityException;
import exceptions.NegativeQuantityException;
import model.InventoryItem;
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
            doRemoveItem();
        } else if (command.equals("e")) {
            runEditMenu();
        } else {
            printInvalidSelection();
        }
    }

    // EFFECTS:     processes user input for the search menu
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
    }

    // EFFECTS:     process the command for the search menu
    //                  i = search for item by ID
    //                  t = search for any items which include part of input
    //                  b = go back to the previous page
    private void processSearchMenu(String command) {
        if (command.equals("i")) {
            doSearchById();
        } else if (command.equals("t")) {
            doSearchByTitle();
        } else if (command.equals("b")) {
            displayInitialMenu();
            runInitialMenu();
        } else {
            printInvalidSelection();
        }
    }

    // EFFECTS:     processes user input for the search menu
    // CREDIT:      this portion is substantively modelled offs of the AccountNotRobust TellerApp
    //              provided as a reference for the term project
    private void runEditMenu() {
        boolean processNext = true;
        String command = null;

        while (processNext) {
            displayEditMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                processNext = false;
            } else {
                processEditMenu(command);
            }
        }
    }

    // EFFECTS:     process the command for the search menu
    //                  t = edit an item's title
    //                  q = edit an item's quantity (increase or decrease)
    //                  d = edit an item's description
    //                  b = go back to the previous page
    private void processEditMenu(String command) {
        if (command.equals("t")) {
            doEditItemTitle();
        } else if (command.equals("q")) {
            doEditItemQuantity();
        } else if (command.equals("d")) {
            doEditItemDescription();
        } else if (command.equals("b")) {
            displayInitialMenu();
            runInitialMenu();
        } else {
            printInvalidSelection();
        }
    }

    // EFFECTS:     view every item in the list in the order they were added
    private void doViewList() {
        String inventoryText = inventoryList.getList();
        if (inventoryText == "") {
            System.out.println("There are currently no items in the inventory system.");
        } else {
            System.out.println(inventoryText);
        }
        printReturnToPreviousMenu();
    }

    // MODIFIES:    this
    // EFFECTS:     search to see if an item exists with the provided id.
    //              if the item does exist, print its parameters.
    //              if item does not exist, print statement that item cannot be found
    //              and return to the previous menu
    private void doSearchById() {
        System.out.println("Enter item ID: ");
        String idStr = input.next();

        if (isValidInteger(idStr)) {
            int searchId = Integer.parseInt(idStr);

            if (inventoryList.hasItem(searchId)) {
                String output = inventoryList.getItemFromId(searchId);
                System.out.println(output);
            } else {
                printCouldNotFindItem(searchId);
            }

        } else {
            printIllegalValue();
        }
        printReturnToPreviousMenu();
    }

    // EFFECTS:     user inputs the item title, which outputs the item's ID, title, description, and quantity.
    //              if item does not exist, throw error
    private void doSearchByTitle() {
        // TRY... CATCH...
        System.out.println("Please note the system will provide any item which contains part of your input.");
        System.out.println("Enter item Title: ");
        String searchTitle = input.next();

        try {
            String output = inventoryList.getItemsFromTitle(searchTitle);
            System.out.println(output);
        } catch (CannotFindItemException e) {
            System.out.println("Could not find any inventory item containing this value: " + searchTitle);
        } finally {
            printReturnToPreviousMenu();
        }
    }

    // MODIFIES:    this
    // EFFECTS:     user inputs the item title, quantity, and description to be added to the list.
    private void doAddItem() {

        System.out.println("Enter item title: ");
        String title = input.next();

        System.out.println("Enter item description: ");
        String description = input.next();

        System.out.println("Enter initial item quantity (0 or greater): ");
        String quantityStr = input.next();

        try {
            int quantity = Integer.parseInt(quantityStr);

            try {
                inventoryList.addItem(title, quantity, description);
                System.out.println("Item successfully added with ID " + inventoryList.getLastIdInList() + "!\n");
            } catch (IllegalQuantityException e) {
                System.out.println("Please enter a quantity 0 or greater! \n");
            }
        } catch (NumberFormatException e) {
            printIllegalValue();
        } finally {
            printReturnToPreviousMenu();
        }
    }

    // MODIFIES:    this
    // EFFECTS:     user specifies the item id to remove from the list.
    private void doRemoveItem() {
        System.out.println("Enter ID of item to remove: ");
        String removeIdStr = input.next();

        if (isValidInteger(removeIdStr)) {
            int removeId = Integer.parseInt(removeIdStr);
            if (inventoryList.hasItem(removeId)) {
                InventoryItem currentItem = inventoryList.getItem(inventoryList.getPositionOfItem(removeId));
                String title = currentItem.getTitle();
                inventoryList.removeItem(removeId);
                System.out.println("Item " + title + " successfully removed!");
            } else {
                printCouldNotFindItem(removeId);
            }
        } else {
            printIllegalValue();
        }
        printReturnToPreviousMenu();
    }


    // MODIFIES:    InventoryItem Title
    // EFFECTS:     user inputs the item ID, where the system outputs the current item parameters.
    //              if item exists, ask for user input to adjust title
    //              if input is not a valid integer, catch NumberFormatException
    //              if item does not exist, do not process
    private void doEditItemTitle() {
        System.out.println("Enter item ID: ");
        String editIdStr = input.next();

        if (isValidInteger(editIdStr)) {
            int editId = Integer.parseInt(editIdStr);
            if (inventoryList.hasItem(editId)) {
                InventoryItem currentItem = inventoryList.getItem(inventoryList.getPositionOfItem(editId));
                System.out.println("Current item parameters:");
                System.out.println(inventoryList.getItemFromId(editId));

                String currentTitle = currentItem.getTitle();

                System.out.println("Enter a new item title: ");
                String newTitle = input.next();
                currentItem.setTitle(newTitle);

                System.out.println("Successfully changed item title from: " + currentTitle + " to: " + newTitle);
            } else {
                printCouldNotFindItem(editId);
            }
        } else {
            printIllegalValue();
        }
        printReturnToPreviousMenu();
    }

    // MODIFIES:    InventoryItem Description
    // EFFECTS:     user inputs the item ID, where the system outputs the current item parameters.
    //              if item exists, ask for user input to adjust description
    //              if input is not a valid integer, catch NumberFormatException
    //              if item does not exist, do not process
    private void doEditItemDescription() {
        System.out.println("Enter item ID: ");
        String editIdStr = input.next();

        if (isValidInteger(editIdStr)) {
            int editId = Integer.parseInt(editIdStr);
            if (inventoryList.hasItem(editId)) {
                InventoryItem currentItem = inventoryList.getItem(inventoryList.getPositionOfItem(editId));
                System.out.println("Current item parameters:");
                System.out.println(inventoryList.getItemFromId(editId));

                String currentDesc = currentItem.getDescription();

                System.out.println("Enter a new item description: ");
                String newDesc = input.next();
                currentItem.setDescription(newDesc);

                System.out.println("Successfully changed item description from: " + currentDesc + " to: " + newDesc);
            } else {
                printCouldNotFindItem(editId);
            }

        } else {
            printIllegalValue();
        }
        printReturnToPreviousMenu();

    }

    // MODIFIES:    InventoryItem Quantity
    // EFFECTS:     user inputs the item ID, where the system outputs the current item parameters.
    //              if item exists, ask for user input to adjust description
    //              if input is not a valid integer, catch NumberFormatException
    //              if item does not exist, do not process
    private void doEditItemQuantity() {
        System.out.println("Enter item ID: ");
        int editId = input.nextInt();

        if (!inventoryList.hasItem(editId)) {
            printCouldNotFindItem(editId);
            return;
        }
        InventoryItem currentItem = inventoryList.getItem(inventoryList.getPositionOfItem(editId));
        System.out.println("Current item parameters:");
        System.out.println(inventoryList.getItemFromId(editId));

        int currentQuantity = currentItem.getQuantity();

        System.out.println("Enter a value (negative values to reduce quantity, positive to increase): ");
        int updateQuantity = input.nextInt();

        try {
            currentItem.updateQuantity(updateQuantity);
            int newQuantity = currentItem.getQuantity();
            System.out.println("Successfully updated item title from: " + currentQuantity + " to: " + newQuantity);
        } catch (NegativeQuantityException e) {
            System.out.println("Item quantity not updated - quantity would fall below 0!  Please try again.");
        } finally {
            printReturnToPreviousMenu();
        }
    }

    private boolean isValidInteger(String num) {
        try {
            Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
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

    // EFFECTS:     displays menu of item searchable options available to user
    private void displaySearchMenu() {
        System.out.println("You may search for an individual item by its unique identifier,");
        System.out.println("or search for all items which contain a piece of text.");
        System.out.println("\t i -> search by unique identifier");
        System.out.println("\t t -> search for items which include any part of an input");
        System.out.println("\t b -> go back to previous menu");
    }

    // EFFECTS:     displays menu of item edit options available to user
    private void displayEditMenu() {
        System.out.println("Here you can edit various parameters of an existing item in the system");
        System.out.println("\t t -> title");
        System.out.println("\t q -> quantity");
        System.out.println("\t d -> description");
        System.out.println("\t b -> go back to previous menu");
    }

    // EFFECTS:     print a statement that the inventory item with the provided ID could not be found.
    private static void printCouldNotFindItem(int id) {
        System.out.println("Could not find an inventory item with the following ID: " + id);
    }

    // EFFECTS:     print a statement that the system is returning to the previous menu
    private static void printReturnToPreviousMenu() {
        System.out.println("Returning to previous menu... \n");
    }

    // EFFECTS:     print a statement that the menu selection is invalid
    private static void printInvalidSelection() {
        System.out.println("Invalid selection, please try again! \n");
    }

    // EFFECTS:     print a statement that the inputted value is illegal
    private static void printIllegalValue() {
        System.out.println("Illegal value - please enter a number for ID!");
    }

}
