package ui;

import model.InventoryItem;

import javax.swing.table.AbstractTableModel;
import java.util.LinkedList;
import java.util.List;

// CREDIT: implementation came from https://www.codejava.net/java-se/swing/editable-jtable-example
/*
This method extends the AbstractTableModel.
Its purpose is to facilitate the editing of items in the inventory list, by editing the JTable itself
 */
public class InventoryTableModel extends AbstractTableModel {
    private final List<InventoryItem> inventoryList;
    private final String[] columnNames = {"ID", "TITLE", "QUANTITY", "DESCRIPTION"};
    private final Class[] columnClass = new Class[] {Integer.class, String.class, Integer.class, String.class};


    public InventoryTableModel(LinkedList<InventoryItem> inventoryList) {

        this.inventoryList = inventoryList;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClass[columnIndex];
    }

    @Override
    public int getRowCount() {
        return inventoryList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (col == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InventoryItem row = inventoryList.get(rowIndex);
        if (0 == columnIndex) {
            return row.getId();
        } else if (1 == columnIndex) {
            return row.getTitle();
        } else if (2 == columnIndex) {
            return row.getQuantity();
        } else if (3 == columnIndex) {
            return row.getDescription();
        }
        return null;
    }

    @Override
    public void setValueAt(Object inputValue, int rowIndex, int columnIndex) {
        InventoryItem row = inventoryList.get(rowIndex);
        if (0 == columnIndex) {
            row.setId((Integer) inputValue);
        } else if (1 == columnIndex) {
            row.setTitle((String) inputValue);
        } else if (2 == columnIndex) {
            row.setQuantity((Integer) inputValue);
        } else if (3 == columnIndex) {
            row.setDescription((String) inputValue);
        }
    }

}
