package ui;

import database.Column;

import javax.swing.*;
import java.awt.*;
import models.AddTableModel;

class AddPanelTabel extends JPanel {
    private final JTextField tableName = new JTextField();
    private final AddTableModel tableAddModel = new AddTableModel();

    AddPanelTabel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new JLabel("Table name:"));
        this.add(tableName);

        JTable columnsTable = new JTable();
        columnsTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
        columnsTable.setFillsViewportHeight(true);
        columnsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        columnsTable.setModel(tableAddModel);
        JComboBox typeCompoBox = new JComboBox(Column.Type.values());
        columnsTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(typeCompoBox));
        this.add(new JLabel("Columns:"));
        JScrollPane tableScroll = new JScrollPane();
        tableScroll.setViewportView(columnsTable);
        this.add(tableScroll);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add column");
        addButton.addActionListener(e -> addColumn());
        buttonPanel.add(addButton);
        JButton removeButton = new JButton("Remove column");
        removeButton.addActionListener(e -> {
            if (columnsTable.getSelectedRowCount() > 0) {
                tableAddModel.remove(columnsTable.getSelectedRow());
            }
        });
        buttonPanel.add(removeButton);
        this.add(buttonPanel);

        addColumn();
    }

    void addColumn() {
        tableAddModel.add(new Column(Column.Type.INT, ""));
    }

    String getDBQuery() {
        return "create table " + tableName.getText() + " (" + tableAddModel.getColumnsAsString() + ")";
    }
}

