package ui;

import exceptions.IllegalQuantityException;
import exceptions.NegativeQuantityException;
import model.InventoryItem;
import model.InventoryManagement;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class InventoryApp {

    private InventoryManagement inventoryList;
    private Scanner input;

    private String jsonStore;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS:     starts running the inventory application
    public InventoryApp() {
        runInventoryManagement();
    }

    // EFFECTS:     runs init() and loads the initial menu
    private void runInventoryManagement() {
        init();
        runLoadMenu();
    }

    // MODIFIES:    this
    // EFFECTS:     initializes input scanner
    // CREDIT:      this portion is substantively modelled off of the AccountNotRobust TellerApp
    //              provided as a reference for the term project
    private void init() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // MODIFIES:    this
    // EFFECTS:     processes user input
    // CREDIT:      this portion is substantively modelled off of the AccountNotRobust TellerApp
    //              provided as a reference for the term project
    private void runLoadMenu() {
        boolean processNext = true;
        String command;
        while (processNext) {
            displayLoadMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                processNext = false;
            } else {
                processLoadMenu(command);
            }
        }
        System.out.println("Thanks for using the system!");
    }

    // EFFECTS:     processes the initial menu of options
    //              n = new inventory list
    //              l = load an existing list
    // CREDIT:      ths portion is substantively modelled off of the AccountNotRobust TellerApp
    //              provided as a reference for the term project
    private void processLoadMenu(String command) {
        if (command.equals("n")) {
            createNewInventoryList();
        } else if (command.equals("l")) {
            loadInventoryList();
        } else {
            printInvalidSelection();
        }
    }

    // MODIFIES:    inventoryList
    // EFFECTS:     ask user to input name for the new inventory list,
    //              create inventory list with the provided name
    private void createNewInventoryList() {
        System.out.println("Enter a name for the new inventory list: ");
        String name = input.next();
        inventoryList = new InventoryManagement(name);
        System.out.println("Successfully created an inventory list with name: " + name);

        runMainMenu();
    }

    // MODIFIES:    this
    // EFFECTS:     ask user to input name for the existing inventory list to load from file,
    //              if file exists, load the inventory list from JSON
    //              if files does not exist, catch FileNotFoundException
    //              if file cannot be read, catch IOException
    private void loadInventoryList() {
        System.out.println("Enter the name of the inventory list to load: ");
        String nameToLoad = input.next().toLowerCase().replace(" ", "");
        jsonStore = "./data/" + nameToLoad + ".json";

        try {
            jsonReader = new JsonReader(jsonStore);
            inventoryList = jsonReader.read();
            System.out.println("Loaded " + inventoryList.getName() + " from " + jsonStore);
            System.out.println("Loading main menu... \n");
            runMainMenu();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to find a inventory list with name: " + nameToLoad + ".");
            System.out.println("Did you perhaps make a typo?");
            printReturnToPreviousMenu();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + jsonStore + ".");
            printReturnToPreviousMenu();
        }
    }

    // MODIFIES:    this
    // EFFECTS:     processes user input
    // CREDIT:      this portion is substantively modelled off of the AccountNotRobust TellerApp
    //              provided as a reference for the term project
    private void runMainMenu() {
        boolean processNext = true;
        String command;

        while (processNext) {
            displayMainMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                System.out.println("Would you like to save before going back? (y/n)");
                if (input.next().equalsIgnoreCase("y")) {
                    writeInventoryList();
                }
                processNext = false;
            } else {
                processMainMenu(command);
            }
        }
    }

    // EFFECTS:     processes the main menu of options
    //              v = view list
    //              s = search for item
    //              a = add item
    //              r = remove item
    //              e = edit item
    // CREDIT:      this portion is substantively modelled off of the AccountNotRobust TellerApp
    //              provided as a reference for the term project
    private void processMainMenu(String command) {
        if (command.equals("v")) {
            doViewList();
        } else if (command.equals("c")) {
            runSearchMenu();
        } else if (command.equals("a")) {
            doAddItem();
        } else if (command.equals("r")) {
            doRemoveItem();
        } else if (command.equals("e")) {
            runEditMenu();
        } else if (command.equals("s")) {
            writeInventoryList();
        } else {
            printInvalidSelection();
        }
    }

    // MODIFIES:    this
    // EFFECTS:     processes user input for the search menu
    // CREDIT:      this portion is substantively modelled offs of the AccountNotRobust TellerApp
    //              provided as a reference for the term project
    private void runSearchMenu() {
        boolean processNext = true;
        String command;

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
            displayMainMenu();
            runMainMenu();
        } else {
            printInvalidSelection();
        }
    }

    // MODIFIES:    this
    // EFFECTS:     processes user input for the edit menu
    // CREDIT:      this portion is substantively modelled offs of the AccountNotRobust TellerApp
    //              provided as a reference for the term project
    private void runEditMenu() {
        boolean processNext = true;
        String command;

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

    // EFFECTS:     process the command for the edit menu
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
            displayMainMenu();
            runMainMenu();
        } else {
            printInvalidSelection();
        }
    }

    // EFFECTS:     view every item in the list, formatted according to their parameters, in the order they were added
    private void doViewList() {
        if (inventoryList.getListSize() == 0) {
            System.out.println("There are currently no items in the inventory system.");
        } else {
            outputNumerousItemParameters(inventoryList.getList());
        }
        printReturnToPreviousMenu();
    }

    // MODIFIES:    this
    // EFFECTS:     search to see if an item exists with the provided id.
    //              if the item does exist, print its parameters.
    //              if item does not exist, print statement that item cannot be found
    //              lastly, return to the previous menu
    private void doSearchById() {
        System.out.println("Enter item ID: ");
        String idStr = input.next();

        if (isValidInteger(idStr)) {
            int searchId = Integer.parseInt(idStr);

            if (inventoryList.hasItem(searchId)) {
                printItemParameters(inventoryList.getItemFromId(searchId));
            } else {
                printCouldNotFindItem(searchId);
            }

        } else {
            printIllegalValue();
        }
        printReturnToPreviousMenu();
    }

    // EFFECTS:     user inputs the item title, which then outputs the parameters (ID, title, description,
    //              and quantity.) of any item containing the input as part of its title.
    //              if item does not exist, print "could not find..." comment and do return to previous menu
    private void doSearchByTitle() {
        System.out.println("Please note the system will provide any item which contains part of your input.");
        System.out.println("Enter item Title: ");
        String searchTitle = input.next();

        LinkedList<InventoryItem> listOfItems = inventoryList.getItemsFromTitle(searchTitle);

        if (listOfItems.size() == 0) {
            System.out.println("Could not find any inventory item containing this value: " + searchTitle);
        } else {
            outputNumerousItemParameters(listOfItems);
        }
        printReturnToPreviousMenu();

    }

    // MODIFIES:    this
    // EFFECTS:     user inputs the item title, quantity, and description to be added to the list.
    //              if quantity entered is < 0, catch IllegalQuantityException.
    //              if quantity provided is not a valid integer, catch NumberFormatException
    //              lastly, return to the previous menu
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
                System.out.println("Item not added - please enter a quantity 0 or greater! \n");
            }
        } catch (NumberFormatException e) {
            printIllegalValue();
        } finally {
            printReturnToPreviousMenu();
        }
    }

    // MODIFIES:    this
    // EFFECTS:     user specifies the item id to remove from the list.
    //              if provided id is invalid integer, do not process and printIllegalValue();
    //              if provided id cannot be found in the list of InventoryItems, do not process and
    //                  printCouldNotFindItem(removeId)
    //              lastly, return to the previous menu
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
    //              if item exists, ask for user input to adjust title and adjust item's title
    //              if input is not a valid integer, do not process and print printIllegalValue()
    //              if item does not exist, do not process and print printCouldNotFindItem(editId)
    //              lastly, return to the previous menu
    private void doEditItemTitle() {
        System.out.println("Enter item ID: ");
        String editIdStr = input.next();

        if (isValidInteger(editIdStr)) {
            int editId = Integer.parseInt(editIdStr);
            if (inventoryList.hasItem(editId)) {
                InventoryItem currentItem = inventoryList.getItem(inventoryList.getPositionOfItem(editId));
                System.out.println("Current item parameters:");
                printItemParameters(inventoryList.getItemFromId(editId));

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
    //              if item exists, ask for user input to adjust description and adjust item's description
    //              if input is not a valid integer, do not process and print printIllegalValue()
    //              if item does not exist, do not process and print printCouldNotFindItem(editId)
    //              lastly, return to the previous menu
    private void doEditItemDescription() {
        System.out.println("Enter item ID: ");
        String editIdStr = input.next();

        if (isValidInteger(editIdStr)) {
            int editId = Integer.parseInt(editIdStr);
            if (inventoryList.hasItem(editId)) {
                InventoryItem currentItem = inventoryList.getItem(inventoryList.getPositionOfItem(editId));
                System.out.println("Current item parameters:");
                printItemParameters(inventoryList.getItemFromId(editId));

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
    //              if item exists, ask for user input to adjust quantity and adjust item's quantity
    //              if item does not exist, do not process and print printCouldNotFindItem(editId)
    //              if quantity would make InventoryItem Quantity fall below 0, catch NegativeQuantityException
    //                  and do not process
    //              lastly, return to the previous menu
    private void doEditItemQuantity() {
        System.out.println("Enter item ID: ");
        int editId = input.nextInt();

        if (!inventoryList.hasItem(editId)) {
            printCouldNotFindItem(editId);
            return;
        }
        InventoryItem currentItem = inventoryList.getItem(inventoryList.getPositionOfItem(editId));
        System.out.println("Current item parameters:");
        printItemParameters(inventoryList.getItemFromId(editId));

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

    // MODIFIES:    this
    // EFFECTS:     ask user to confirm they wish to overwrite the existing file,
    //              save the inventory list to file
    //              if files does not exist, catch FileNotFoundException
    private void writeInventoryList() {
        System.out.println("Do you want to save and overwrite the inventory list"
                + inventoryList.getName() + "? (y/n)");

        if (!input.next().equalsIgnoreCase("y")) {
            System.out.println("File not saved - returning to previous menu...");
            return;
        }
        String nameToSave = inventoryList.getName().toLowerCase().replace(" ", "");
        jsonStore = "./data/" + nameToSave + ".json";

        try {
            jsonWriter = new JsonWriter(jsonStore);
            jsonWriter.open();
            jsonWriter.write(inventoryList);
            jsonWriter.close();
            System.out.println("Saved " + inventoryList.getName() + " to " + jsonStore);
            System.out.println("Loading main menu... \n");
            //runMainMenu();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + jsonStore + ".");
            printReturnToPreviousMenu();
        }
    }

    // EFFECTS:     return true if the input, as a string, is a valid integer
    //              catch NumberFormatException and return false if not valid integer
    // NOTE:        useful for determining if the provided input, such as for id or quantity, is a valid integer
    private boolean isValidInteger(String num) {
        try {
            Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }


    // EFFECTS:     displays menu of creating a new list of items or loading an existing one from file
    // CREDIT:      this portion is substantively modelled off of the AccountNotRobust TellerApp
    //              provided as a reference for the term project
    private void displayLoadMenu() {
        System.out.println("Welcome to the inventory management system!");
        System.out.println("Would you like to create a new list of inventory items or load an existing one from file?");
        System.out.println("Please select from the following options (case-insensitive):");
        System.out.println("\t n -> create a new list");
        System.out.println("\t l -> load an existing list");
        System.out.println("\t q -> quit the system");
    }


    // EFFECTS:     displays main menu of options available to user
    private void displayMainMenu() {
        System.out.println("Main Menu");
        System.out.println("Please select from the following options (case-insensitive):");
        System.out.println("\t v -> view current list of all inventory items");
        System.out.println("\t c -> check for specific items in the list");
        System.out.println("\t a -> add an item to the list");
        System.out.println("\t r -> remove an item from the list");
        System.out.println("\t e -> edit an item in the list");
        System.out.println("\t s -> save the list");
        System.out.println("\t b -> go back to previous menu");
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

    // EFFECTS:     print the parameters of all items in the provided list
    private void outputNumerousItemParameters(LinkedList<InventoryItem> list) {
        for (int i = 0; i < list.size(); i++) {
            printItemParameters(list.get(i));
        }
    }

    // EFFECTS:     prints out the provided item's parameters
    private void printItemParameters(InventoryItem item) {
        String id = "ID: " + item.getId() + "\n";
        String title = "Title: " + item.getTitle() + "\n";
        String quantity = "Quantity: " + item.getQuantity() + "\n";
        String description = "Description: " + item.getDescription() + "\n";

        System.out.println(id + title + quantity + description);
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
        System.out.println("Illegal value - please enter a number!");
    }

}
