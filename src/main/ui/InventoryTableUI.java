package ui;

import model.InventoryItem;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/*
This method extends JInternalFrame.
Its purpose is to allow the creation of numerous JInternalFrames of inventory list tables.
 */
public class InventoryTableUI extends JInternalFrame {
    private static final int minWIDTH = 400;
    private static final int minHEIGHT = 200;
    private static final int prefWIDTH = 600;
    private static final int prefHEIGHT = 200;

    private static final int descWIDTH = 300;

    private static final int posX = 250;
    private static final int posY = 200;

    private JTable inventoryTable;

    //EFFECTS:  Creates a JInternalFrame of specified minWIDTH and minHEIGHT at the target location posX, posY.
    //          A new InventoryTableModel will be created for the new JTable which will also be created.
    public InventoryTableUI(String name, LinkedList<InventoryItem> list, Component parent) {
        super(name, true, true, false, false);
        setMinimumSize(new Dimension(minWIDTH, minHEIGHT));
        setPreferredSize(new Dimension(prefWIDTH, prefHEIGHT));

        InventoryTableModel tableModel = new InventoryTableModel(list);
        inventoryTable = new JTable(tableModel);
        inventoryTable.getColumn("DESCRIPTION").setPreferredWidth(descWIDTH);

        JScrollPane sp = new JScrollPane(inventoryTable);
        add(sp);
        pack();
        setLocation(posX, posY);
        setVisible(true);
    }
}
