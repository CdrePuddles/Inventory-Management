package ui;

import model.InventoryItem;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class InventoryTableUI extends JInternalFrame {
    private static final int minWIDTH = 400;
    private static final int minHEIGHT = 200;
    private static final int prefWIDTH = 600;
    private static final int prefHEIGHT = 200;

    private static final int descWIDTH = 300;

    private static final int posX = 250;
    private static final int posY = 200;

    private JTable inventoryTable;


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
