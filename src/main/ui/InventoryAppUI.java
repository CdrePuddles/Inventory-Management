package ui;

import exceptions.IllegalQuantityException;
import model.InventoryItem;
import model.InventoryManagement;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.LinkedList;

// This is the front-end terminal interface where terminal commands will be processed.
public class InventoryAppUI extends JFrame {

    private InventoryManagement inventoryList;

    private String jsonStore;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private JDesktopPane program;
    private JInternalFrame buttons;
    private JInternalFrame inventoryDisplay;
    private JInternalFrame addItem;

    private JTextField addTitle;
    private JFormattedTextField addQuantity;
    private JTextArea addDescription;

    NumberFormat acceptOnlyIntegers = NumberFormat.getIntegerInstance();

    // EFFECTS:     starts running the inventory application
    public InventoryAppUI() {
        gui();
    }

    // MODIFIES:    this
    // EFFECTS:     initializes graphical user interface elements
    //              if no list is currently loaded, load the welcome splash screen
    //              if a list is loaded, load menu buttons panel
    private void gui() {
        program = new JDesktopPane();
        program.addMouseListener(new MouseFocusAction());

        setContentPane(program);
        setTitle("Inventory Management System");
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
    // EFFECTS:     listens for WINDOW_CLOSING event.  If closing, and if list exists, ask if the user wants to save
    //              before exiting.
    //              If yes, call saveList().  If no, exit program.
    //              If no list exists, exit program.
    // CREDIT:      substantively learned from https://www.clear.rice.edu/comp310/JavaResources/frame_close.html
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (inventoryList != null) {
                int n = JOptionPane.showConfirmDialog(null,
                        "Would you like to save the file before exiting?",
                        "Save before quitting?",
                        JOptionPane.YES_NO_CANCEL_OPTION);

                if (n == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(null, "File not saved!");
                } else if (n != JOptionPane.YES_OPTION) {
                    return;
                } else if (!saveList()) {
                    return;
                }
            }
            super.processWindowEvent(e);
            System.exit(0);
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
        buttons = new JInternalFrame("Menu buttons", false, false, false, false);

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
        inventoryDisplay.moveToFront();

    }

    // MODIFIES:    inventoryList
    // EFFECTS:     ask user to input name for the new inventory list,
    //              create inventory list with the provided name
    private class CreateListAction extends AbstractAction {

        CreateListAction() {
            super("NEW LIST");
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


    // EFFECTS:     request user input to specify name of list to load, then run loadList() with provided String name
    private class LoadListAction extends AbstractAction {

        LoadListAction() {
            super("LOAD");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String nameToLoad = JOptionPane.showInputDialog(null,
                        "Specify name of inventory list you wish to load",
                        "Enter list name...").toLowerCase().replace(" ", "");
                loadList(nameToLoad);
            } catch (NullPointerException err) {
                JOptionPane.showMessageDialog(null, "List not loaded - please provide a list name.");
            }
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
                JOptionPane.showMessageDialog(null, "There are currently no items in the inventory system.");
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

    // EFFECTS:     search for a specific item by providing its item ID, or search for any item whose title
    //              contains the provided phrase
    private class SearchListAction extends AbstractAction {

        SearchListAction() {
            super("SEARCH");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            LinkedList searchList = new LinkedList<InventoryItem>();
            String search;
            try {
                search =
                        JOptionPane.showInputDialog(
                                null,
                                "Enter either an item ID (int) to search for one item, or a string for many items.",
                                "Enter integer ID or string name...");
                try {
                    if (searchByID(searchList, search) == null) {
                        return;
                    }
                } catch (NumberFormatException err) {
                    searchList = inventoryList.getItemsFromTitle(search);
                    if (searchList.size() == 0) {
                        JOptionPane.showMessageDialog(null, "Unable to find an item with the title: " + search);
                        return;
                    }
                }
                addInventoryArea("Search", searchList);
            } catch (NullPointerException err) {
                JOptionPane.showMessageDialog(null, "Please specify an ID or title to search for.");
            }
        }

        // EFFECTS:     searches for the item by item ID
        private LinkedList searchByID(LinkedList searchList, String search) {
            int searchId = Integer.parseInt(search);
            if (inventoryList.hasItem(searchId)) {
                searchList.add(inventoryList.getItemFromId(searchId));
            } else {
                JOptionPane.showMessageDialog(null, "Unable to find an item with the ID: " + searchId);
                return null;
            }
            return searchList;
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
                } catch (IllegalQuantityException err) {
                    JOptionPane.showMessageDialog(null, "Please enter a quantity greater than 0.",
                            "Illegal Quantity", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number",
                        "Number Format Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // EFFECTS:     resets the input fields to blank following a successful item add
        private void setFieldsToBlank() {
            addTitle.setText("");
            addQuantity.setText("");
            addDescription.setText("");
        }

        // EFFECTS:     checks to see if the add inventory fields are all filled in
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

    // EFFECTS:     runs the saveList() method, which saves the list to file
    private class SaveListAction extends AbstractAction {

        SaveListAction() {
            super("SAVE");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            saveList();
        }
    }

    // EFFECTS:     saves the current inventory list to a file using inventorylist.getName() as the name.
    //              invalid characters will be removed, and the string will be converted toLowerCase()
    private boolean saveList() {
        String name = inventoryList.getName().toLowerCase().replace(" ", "");
        jsonStore = "./data/" + name + ".json";
        if (new File(jsonStore).isFile()) {
            int n = JOptionPane.showConfirmDialog(null,
                    "File already exists - would you like to save and overwrite the existing file?",
                    "Save and Overwrite?",
                    JOptionPane.YES_NO_OPTION);
            if (n != JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "File not saved!");
                return false;
            }
        }

        try {
            jsonWriter = new JsonWriter(jsonStore);
            jsonWriter.open();
            jsonWriter.write(inventoryList);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null, "Saved " + inventoryList.getName() + " to " + jsonStore);
            return true;
        } catch (FileNotFoundException err) {
            JOptionPane.showMessageDialog(null, "Unable to write to file: " + jsonStore + ".");
            return false;
        }
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
}
