package ui;

import exceptions.IllegalQuantityException;
import exceptions.NegativeQuantityException;
import model.InventoryItem;
import model.InventoryManagement;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Scanner;

// This is the front-end terminal interface where terminal commands will be processed.
public class InventoryAppUI extends JFrame {

    private InventoryManagement inventoryList;
    private Scanner input;

    private String jsonStore;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final Border blackline = BorderFactory.createLineBorder(Color.black);

    private JDesktopPane program;
    //    private JLabel title = new JLabel("");
    private JInternalFrame buttons;
    private JInternalFrame inventoryDisplay;
    private JInternalFrame addItem;
    private JTable inventoryTable;

    private JTextField addTitle;
    private JFormattedTextField addQuantity;
    private JTextArea addDescription;

    NumberFormat acceptOnlyIntegers = NumberFormat.getIntegerInstance();

    // EFFECTS:     starts running the inventory application
    public InventoryAppUI() {
        //init();
        gui();
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
    // EFFECTS:     initializes graphical user interface elements
    //              if no list is currently loaded, load the welcome splash screen
    //              if a list is loaded, load action buttons panel
    private void gui() {
        //program = new JPanel(new BorderLayout(20, 15));
        program = new JDesktopPane();
        program.addMouseListener(new MouseFocusAction());

        setContentPane(program);
        setTitle("Inventory Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        setResizable(true);
        setVisible(true);

        if (inventoryList == null) {
            welcomeScreen();
        } else {
            addButtonPanel();
        }
    }

    // MODIFIES:    this
    // EFFECTS:     on the successful load of an inventory list, run initial gui, inventory table, and add-item option
    private void successfulLoad() {
        gui();
        addInventoryArea(inventoryList.getName(), inventoryList.getList());
        addItemArea();

    }

    // EFFECTS:     starts an initial splash screen, which gives the user the following two options:
    //              1. load an existing inventory list
    //              2. create a new list
    //              the splash screen also includes a picture of an adorable, yet chonky, tabby cat
    private void welcomeScreen() {
        buttons = new JInternalFrame("Welcome!", false, false, false, false);
        JPanel buttonsPanel = new JPanel(new BorderLayout());

        try {
            BufferedImage cat = ImageIO.read(new File("./data/cat.jpg"));
            JLabel catImage = new JLabel(new ImageIcon(cat));
            catImage.setBorder(new EmptyBorder(5, 5, 5, 5));
            catImage.setVisible(true);
            buttonsPanel.add(catImage, BorderLayout.NORTH);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to load splash image, continuing program...");
        }
        JPanel actionButtons = new JPanel();
        actionButtons.setLayout(new GridLayout(1, 2));
        actionButtons.add(new JButton(new LoadListAction()));
        actionButtons.add(new JButton(new CreateListAction()));
        buttonsPanel.add(actionButtons, BorderLayout.SOUTH);
        buttons.add(buttonsPanel);

        buttons.setVisible(true);
        buttons.pack();
        buttons.setLocation((program.getWidth() / 2 - buttons.getWidth() / 2),
                (program.getHeight() / 2 - buttons.getHeight() / 2));
        program.add(buttons);
    }

    // EFFECTS:     Loads the panel which includes action buttons.
    //              1) view the active list
    //              2) remove an item from the active list
    //              3) search for a specific item or items
    //              4) save the active list
    //              5) load a new list
    //              6) create a new list
    private void addButtonPanel() {
        buttons = new JInternalFrame("Action buttons", false, false, false, false);

        JPanel actionButtons = new JPanel();
        actionButtons.setLayout(new GridLayout(2, 3));

        actionButtons.add(new JButton(new ViewListAction()));
        actionButtons.add(new JButton(new RemoveListAction()));
        actionButtons.add(new JButton(new SearchListAction()));
        actionButtons.add(new JButton(new SaveListAction()));
        actionButtons.add(new JButton(new LoadListAction()));
        actionButtons.add(new JButton(new CreateListAction()));
        buttons.add(actionButtons);
        buttons.pack();
        buttons.setVisible(true);
        program.add(buttons);
    }

    // EFFECTS:     creates a JInternalFrame to facilitate the addition of new items within the active inventory list
    private void addItemArea() {
        addItem = new JInternalFrame("Add item", false, false, false, false);
        addItem.setMinimumSize(new Dimension(215, 150));

        JPanel addItemPanel = new JPanel(new BorderLayout());
        addItemPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel addItemGridPanel = itemInputFields();
        addItemPanel.add(addItemGridPanel, BorderLayout.CENTER);

        addItemPanel.add(new JButton(new AddListAction()), BorderLayout.EAST);
        addItemPanel.add(addDescription, BorderLayout.SOUTH);

        addItemAreaToProgram(addItemPanel);
    }

    // EFFECTS:     creates the fields necessary for specifying new item parameters
    private JPanel itemInputFields() {
        JPanel addItemGridPanel = new JPanel();
        addItemGridPanel.setLayout(new GridLayout(3, 2));

        addTitle = new JTextField(5);
        addQuantity = new JFormattedTextField(acceptOnlyIntegers);
        addDescription = new JTextArea(3, 5);

        addItemGridPanel.add(new JLabel("Title: "));
        addItemGridPanel.add(addTitle);
        addItemGridPanel.add(new JLabel("Quantity: "));
        addItemGridPanel.add(addQuantity);
        addItemGridPanel.add(new JLabel("Description: "));
        return addItemGridPanel;
    }

    // MODIFIES:    this
    // EFFECTS:     adds the addItemArea() to the program
    private void addItemAreaToProgram(JPanel addItemPanel) {
        addItem.add(addItemPanel);
        addItem.setLocation(0, 200);
        addItem.pack();
        addItem.setVisible(true);
        addItem.setResizable(true);
        program.add(addItem);
    }

    // MODIFIES:    this
    // EFFECTS:     creates a new JTable based on the provided list, with a title of name
    private void addInventoryArea(String name, LinkedList<InventoryItem> list) {
        inventoryDisplay = new InventoryTableUI(name, list, InventoryAppUI.this);
        program.add(inventoryDisplay);
    }

    // MODIFIES:    inventoryList
    // EFFECTS:     ask user to input name for the new inventory list,
    //              create inventory list with the provided name
    private class CreateListAction extends AbstractAction {

        CreateListAction() {
            super("NEW");
        }

        public void actionPerformed(ActionEvent e) {

            String name = JOptionPane.showInputDialog(null,
                    "Please input a list name.",
                    "Enter list name...");
            if (name != null) {
                inventoryList = new InventoryManagement(name);
                saveList();
                successfulLoad();
            } else {
                JOptionPane.showMessageDialog(null, "List not created - please provide a valid list name.");
            }
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

    // EFFECTS:     request user input to specify name of list to load, then run loadList() with provided String name
    private class LoadListAction extends AbstractAction {

        LoadListAction() {
            super("LOAD");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String nameToLoad = JOptionPane.showInputDialog(null,
                    "Specify name of inventory list you wish to load",
                    "Enter list name...").toLowerCase().replace(" ", "");
            loadList(nameToLoad);
        }
    }

    // MODIFIES:    this
    // EFFECTS:     ask user to input name for the existing inventory list to load from file,
    //              if file exists, load the inventory list from JSON
    //              if files does not exist, catch FileNotFoundException
    //              if file cannot be read, catch IOException
    private void loadList(String name) {
        jsonStore = "./data/" + name + ".json";

        try {
            jsonReader = new JsonReader(jsonStore);
            inventoryList = jsonReader.read();
            successfulLoad();
        } catch (FileNotFoundException err) {
            JOptionPane.showMessageDialog(null, "Unable to find an inventory list with name: " + name);
        } catch (IOException err) {
            JOptionPane.showMessageDialog(null, "Unable to read from file: " + jsonStore);
        }
    }


    // EFFECTS:     view every item in the list, formatted according to their parameters, in the order they were added
    private class ViewListAction extends AbstractAction {

        ViewListAction() {
            super("VIEW");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (inventoryList.getListSize() == 0) {
                System.out.println("There are currently no items in the inventory system.");
            } else {
                addInventoryArea(inventoryList.getName(), inventoryList.getList());
            }
        }
    }

    // EFFECTS:     remove an item from the list by providing a valid item ID
    private class RemoveListAction extends AbstractAction {

        RemoveListAction() {
            super("REMOVE");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String remove = JOptionPane.showInputDialog(null,
                    "Enter an item ID to remove.",
                    "Enter integer ID...");
            try {
                int removeId = Integer.parseInt(remove);
                if (inventoryList.hasItem(removeId)) {
                    InventoryItem currentItem = inventoryList.getItem(inventoryList.getPositionOfItem(removeId));
                    String title = currentItem.getTitle();
                    inventoryList.removeItem(removeId);
                    JOptionPane.showMessageDialog(null,
                            "Item " + title + " with ID: " + removeId + " successfully removed!");
                    reloadInventoryTable();
                } else {
                    JOptionPane.showMessageDialog(null, "Unable to find an item with the ID: " + removeId,
                            "Item Does Not Exist", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number",
                        "Number Format Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // EFFECTS:     view every item in the list, formatted according to their parameters, in the order they were added
    private class SearchListAction extends AbstractAction {

        SearchListAction() {
            super("SEARCH");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            LinkedList searchList = new LinkedList<InventoryItem>();
            String search;
            search = JOptionPane.showInputDialog(null,
                    "Enter either an item ID (integer) to search for individual item, or a string for numerous items.",
                    "Enter integer ID or string name...");
            if (isValidInteger(search)) {
                int searchId = Integer.parseInt(search);

                if (inventoryList.hasItem(searchId)) {
                    searchList.add(inventoryList.getItemFromId(searchId));
                } else {
                    JOptionPane.showMessageDialog(null, "Unable to find an item with the ID: " + searchId);
                    return;
                }
            } else {
                searchList = inventoryList.getItemsFromTitle(search);

                if (searchList.size() == 0) {
                    JOptionPane.showMessageDialog(null, "Unable to find an item with the title: " + search);
                    return;
                }
            }
            addInventoryArea("Search", searchList);
        }
    }


    // MODIFIES:    this
    // EFFECTS:     user inputs the item title, quantity, and description to be added to the list.
    //              if quantity entered is < 0, catch IllegalQuantityException.
    //              if quantity provided is not a valid integer, catch NumberFormatException
    //              lastly, return to the previous menu
    private class AddListAction extends AbstractAction {

        AddListAction() {
            super("ADD");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String title = addTitle.getText();
            String quantity = addQuantity.getText();
            String description = addDescription.getText();
            if (checkAllFieldsExist(title, quantity, description)) {
                return;
            }
            try {
                int quantityInt = Integer.parseInt(addQuantity.getText().replace(",", ""));

                try {
                    inventoryList.addItem(addTitle.getText(), quantityInt, addDescription.getText());
                    setFieldsToBlank();
                    reloadInventoryTable();
                    //System.out.println("Item successfully added with ID " + inventoryList.getLastIdInList() + "!\n");
                } catch (IllegalQuantityException err) {
                    JOptionPane.showMessageDialog(null, "Please enter a quantity greater than 0.",
                            "Illegal Quantity", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number",
                        "Number Format Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void setFieldsToBlank() {
            addTitle.setText("");
            addQuantity.setText("");
            addDescription.setText("");
        }

        private boolean checkAllFieldsExist(String title, String quantity, String description) {
            if (title.equals("") | quantity.equals("") | description.equals("")) {
                JOptionPane.showMessageDialog(null, "Please input all fields to add an item.",
                        "Missing fields", JOptionPane.ERROR_MESSAGE);
                return true;
            }
            return false;
        }
    }

    // EFFECTS:     destroys and reloads the main inventory table
    private void reloadInventoryTable() {
        inventoryDisplay.dispose();
        addInventoryArea(inventoryList.getName(), inventoryList.getList());
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


    private class SaveListAction extends AbstractAction {

        SaveListAction() {
            super("SAVE");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
//            int n = JOptionPane.showConfirmDialog(null, "Would you like to save and overwrite the existing file?",
//                    "Save and Overwrite?",
//                    JOptionPane.YES_NO_OPTION);
//
//            if (n != JOptionPane.YES_OPTION) {
//                JOptionPane.showMessageDialog(null,"File not saved!");
//                return;
//            }
//            String nameToSave = inventoryList.getName().toLowerCase().replace(" ", "");
            saveList();

        }
    }

    // EFFECTS:     saves the current inventory list to a file using inventorylist.getName() as the name.
    //              invalid characters will be removed, and the string will be converted toLowerCase()
    private void saveList() {
        String name = inventoryList.getName().toLowerCase().replace(" ", "");
        jsonStore = "./data/" + name + ".json";
        if (new File(jsonStore).isFile()) {
            int n = JOptionPane.showConfirmDialog(null,
                    "File already exists - would you like to save and overwrite the existing file?",
                    "Save and Overwrite?",
                    JOptionPane.YES_NO_OPTION);

            if (n != JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "File not saved!");
                return;
            }
        }

        try {
            jsonWriter = new JsonWriter(jsonStore);
            jsonWriter.open();
            jsonWriter.write(inventoryList);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null, "Saved " + inventoryList.getName() + " to " + jsonStore);

        } catch (FileNotFoundException err) {
            JOptionPane.showMessageDialog(null, "Unable to write to file: " + jsonStore + ".");
            System.out.println("Unable to write to file: " + jsonStore + ".");
            printReturnToPreviousMenu();
        }
    }


    // MODIFIES:    this
    // EFFECTS:     ask user to confirm they wish to overwrite the existing file,
    //              save the inventory list to file
    //              if files does not exist, catch FileNotFoundException
    private void writeInventoryList() {
        System.out.println("Do you want to save and overwrite the inventory list "
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


    // EFFECTS: Represents action to be taken when user clicks desktop to switch focus. (Needed for key handling.)
    // CREDIT:      this portion is substantively modelled off of the AlarmSystem code
    //              provided as a reference for the term project
    private class MouseFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            InventoryAppUI.this.requestFocusInWindow();
        }
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


    // EFFECTS:     creates a JPanel for each inventory item, wrapping each in a black border and
    //              printing out their parameters.
    private void printItemParameters(InventoryItem item) {
        String[] column = {"ID", "TITLE", "QUANTITY", "DESCRIPTION"};
        DefaultTableModel tableModel = new DefaultTableModel(column, 0);
        inventoryTable = new JTable(tableModel);

        /*

        JPanel output = new JPanel(new GridLayout(4,2));
        //output.setBorder(new EmptyBorder(10,10,10,10));
        output.setBorder(blackline);
        output.add(printJLabel("ID: "));
        output.add(printJLabel(Integer.toString(item.getId())));
        output.add(printJLabel("Title: "));
        output.add(printJLabel(item.getTitle()));
        output.add(printJLabel("Quantity: "));
        output.add(printJLabel(Integer.toString(item.getQuantity())));
        output.add(printJLabel("Description: "));
        output.add(printJLabel(item.getDescription()));
*/
        //inventoryDisplay.add(output);
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
            addInventoryArea(inventoryList.getName(), inventoryList.getList());
        }
        printReturnToPreviousMenu();

    }

    // EFFECTS:     converts the provided string into a JLabel with a font size of 20.
    private JLabel printJLabel(String string) {
        JLabel ret = new JLabel(string);
        ret.setFont(new Font(null, 0, 20));
        return ret;
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

    // MODIFIES:    this
    // EFFECTS:     processes user input
    // CREDIT:      this portion is substantively modelled off of the AccountNotRobust TellerApp
    //              provided as a reference for the term project
    private void runLoadMenu() {
        boolean processNext = true;
        String command;
        while (processNext) {
            // displayLoadMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                processNext = false;
            } else {
                //processLoadMenu(command);
            }
        }
        System.out.println("Thanks for using the system!");
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
            //doViewList();
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
