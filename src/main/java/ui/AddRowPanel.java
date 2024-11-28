package ui;

import database.Column;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import models.AddRowModel;

class AddRowPanel extends JPanel {
    final AddRowModel rowAddModel;
    private final String table;

    AddRowPanel(String table, Collection<Column> columns) {
        this.table = table;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new JLabel("Table name: " + table));

        JTable columnsTable = new JTable();
        columnsTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
        columnsTable.setFillsViewportHeight(true);
        rowAddModel = new AddRowModel(columns);
        columnsTable.setModel(rowAddModel);
        this.add(new JLabel("Values:"));
        JScrollPane tableScroll = new JScrollPane();
        tableScroll.setViewportView(columnsTable);
        this.add(tableScroll);
    }

    String getDBQuery() {
        return String.format("insert into %s (%s) values (%s)", table,
                rowAddModel.getColumnsAsString(), rowAddModel.getValuesAsString());
    }

    public AddRowModel getRowAddModel() {
        return rowAddModel;
    }
}

