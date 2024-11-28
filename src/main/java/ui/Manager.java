package ui;

import database.Database;
import database.DatabaseReader;
import database.Result;
import database.Row;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import models.TableModel;

public class Manager extends JFrame {
  private Database database;

  private final JList tableList = new JList();
  private final DefaultListModel tableListModel = new DefaultListModel();
  private final JLabel resultMessage = new JLabel();
  private final TableModel resultTableModel = new TableModel();
  private final JPanel tableControlPanel = new JPanel();
  private final JFileChooser fileChooser = new JFileChooser();

  public Manager() {
    super("Lab1");

    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setSize(800, 400);
    this.getContentPane().setBackground(Color.yellow);
    this.getContentPane().add(BorderLayout.NORTH, initializeMenuBar()).setBackground(Color.white);
    this.getContentPane().add(BorderLayout.WEST, initializeMenuPanel()).setBackground(Color.white);
    this.getContentPane().add(BorderLayout.CENTER, initializeResultPanel())
        .setBackground(Color.white);
  }

  private JMenuBar initializeMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    JMenu menuFile = new JMenu("Файл");
    JMenu menuAction = new JMenu("Таблиці");
    JMenu sortAscByMenu = new JMenu("[ASC] Сортувати  за");
    JMenu sortDesByMenu = new JMenu("[DES] Сортувати  за");
    menuBar.add(menuFile);
    menuBar.add(menuAction);
    menuBar.add(sortAscByMenu);
    menuBar.add(sortDesByMenu);
    JMenuItem menuFileOpen = new JMenuItem("Відкрити БД");
    menuFileOpen.addActionListener(e -> {
      fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
      if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        String databasePath = fileChooser.getSelectedFile().getAbsolutePath();
        try {
          database = new DatabaseReader(databasePath).read();
        } catch (Exception ex) {
          database = new Database(databasePath);
          ex.printStackTrace();
        }
        populateTableList();
      }
    });
    JMenuItem menuFileSaveAs = new JMenuItem("Зберегти як");
    menuFileSaveAs.addActionListener(e -> {
      fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
      if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        database.setFilePath(fileChooser.getSelectedFile().getAbsolutePath());
        try {
          database.save();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    });
    JMenuItem createDB = new JMenuItem("Створити");
    createDB.addActionListener(e -> {
      fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
      if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        String newDatabasePath = fileChooser.getSelectedFile().getAbsolutePath();

        // Перевірка, чи має файл розширення .json. Якщо ні - додаємо його.
        if (!newDatabasePath.endsWith(".json")) {
          newDatabasePath += ".json";
        }

        File newDatabaseFile = new File(newDatabasePath);
        if (!newDatabaseFile.exists()) {
          try {
            newDatabaseFile.createNewFile();
            // Ініціалізація нової бази даних та запис її в файл.
            database = new Database(newDatabasePath);
            database.save();
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        } else {
          // Тут можна вивести повідомлення про те, що файл вже існує, і запитати, чи хоче користувач його перезаписати.
          System.out.println("File already exists!");
        }
      }
    });
    menuFile.add(createDB);
    menuFile.add(menuFileOpen);
    menuFile.add(menuFileSaveAs);
    JMenuItem menuCreateTable = new JMenuItem("Створити таблицю");
    JMenuItem menuDeleteTable = new JMenuItem("Видалити таблицю");
    menuCreateTable.addActionListener(e -> {
      if (database == null) {
        JOptionPane.showMessageDialog(this, "Оберіть спочатку БД");
        return;
      }
      AddPanelTabel tableAdd = new AddPanelTabel();
      if (JOptionPane.showConfirmDialog(this, tableAdd,
          "Введіть дані до таблиці:", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        displayQueryResults(database.query(tableAdd.getDBQuery()), false);
        populateTableList();
      }
    });
    menuDeleteTable.addActionListener(e -> {
      String tableName = (String) tableList.getSelectedValue();
      if (tableName == null || database == null) {
        JOptionPane.showMessageDialog(this, "Таблицю не обрано!");
        return;
      }
      if (JOptionPane.showConfirmDialog(this,
          "Ви впевнені,що хочете видалити " + tableName + "?", "Будь ласка, підтвердіть",
          JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        displayQueryResults(database.query("delete table " + tableName), false);
        populateTableList();
      }
    });
    sortAscByMenu.addMenuListener(new MenuListener() {
      @Override
      public void menuSelected(MenuEvent e) {
        try {
          sortAscByMenu.removeAll();

          ArrayList<String> columns = resultTableModel.columns;

          for (String column : columns) {
            JMenuItem sortMenuItem = new JMenuItem(column);

            sortMenuItem.addActionListener(event -> {
              displayQueryResults(
                  database.query("sort from " + tableList.getSelectedValue() + " by " + column),
                  false);
              populateTableList();
            });

            sortAscByMenu.add(sortMenuItem);
          }

          menuBar.revalidate();
          menuBar.repaint();

        } catch (Exception ex) {
          JOptionPane.showMessageDialog(null,
              "Помилка при створенні меню для сортування: " + ex.getMessage());
        }
      }

      @Override
      public void menuDeselected(MenuEvent e) {

      }

      @Override
      public void menuCanceled(MenuEvent e) {

      }
    });
    sortDesByMenu.addMenuListener(new MenuListener() {
      @Override
      public void menuSelected(MenuEvent e) {
        try {
          sortDesByMenu.removeAll();

          ArrayList<String> columns = resultTableModel.columns;

          for (String column : columns) {
            JMenuItem sortMenuItem = new JMenuItem(column);

            sortMenuItem.addActionListener(event -> {
              displayQueryResults(database.query(
                  "sort from " + tableList.getSelectedValue() + " by " + column + " desc"), false);
              populateTableList();
            });

            sortDesByMenu.add(sortMenuItem);
          }

          menuBar.revalidate();
          menuBar.repaint();

        } catch (Exception ex) {
          JOptionPane.showMessageDialog(null,
              "Помилка при створенні меню для сортування: " + ex.getMessage());
        }
      }

      @Override
      public void menuDeselected(MenuEvent e) {

      }

      @Override
      public void menuCanceled(MenuEvent e) {

      }
    });
    menuAction.add(menuCreateTable);
    menuAction.add(menuDeleteTable);
    return menuBar;
  }

  private JPanel initializeMenuPanel() {
    JPanel menuPanel = new JPanel();
    menuPanel.setLayout(new BorderLayout());
    tableList.setModel(tableListModel);
    tableList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    tableList.setLayoutOrientation(JList.VERTICAL);
    tableList.setVisibleRowCount(-1);
    tableList.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          int index = tableList.locationToIndex(e.getPoint());
          String tableName = tableListModel.getElementAt(index).toString();
          displayQueryResults(database.query("select * from " + tableName), true);
        }
      }
    });
    JScrollPane listScroll = new JScrollPane(tableList);
    listScroll.setPreferredSize(new Dimension(150, 100));
    menuPanel.add(BorderLayout.CENTER, listScroll);
    return menuPanel;
  }

  private JPanel initializeResultPanel() {
    JPanel resultPanel = new JPanel();
    resultPanel.setLayout(new BorderLayout());
    resultPanel.add(BorderLayout.NORTH, resultMessage);
    JTable resultTable = new JTable(resultTableModel);
    JButton addRowButton = getAddRowButton();
    JButton deleteRowButton = getDeleteRowButton(resultTable);
    deleteRowButton.setVisible(false);
    tableControlPanel.add(addRowButton);
    tableControlPanel.add(deleteRowButton);

    resultTable.getSelectionModel().addListSelectionListener(x -> {
      int selectedRow = resultTable.getSelectedRow();
      deleteRowButton.setVisible(selectedRow >= 0);
    });

    JScrollPane tableScroll = new JScrollPane(resultTable);
    resultTable.setFillsViewportHeight(true);
    resultPanel.add(BorderLayout.CENTER, tableScroll);
    tableControlPanel.setVisible(false);
    resultPanel.add(BorderLayout.SOUTH, tableControlPanel);
    return resultPanel;
  }

  private JButton getAddRowButton() {
    JButton addRowButton = new JButton("Додати рядок");
    addRowButton.addActionListener(e -> {
      if (database == null) {
        JOptionPane.showMessageDialog(this, "No open database");
        return;
      }
      String tableName = (String) tableList.getSelectedValue();
      try {
        AddRowPanel rowAdd = new AddRowPanel(tableName, database.getTableColumns(tableName));
        if (JOptionPane.showConfirmDialog(this, rowAdd,
            "Введіть дані для рядка:", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

          List<String> values = rowAdd.getRowAddModel().getValues();
          for (String value : values) {
            if (value == null || value.trim().isEmpty()) {
              JOptionPane.showMessageDialog(this, "Будь ласка введіть всі дані.");
              return;
            }
          }

          var query = rowAdd.getDBQuery();
          displayQueryResults(database.query(query), false);
          populateTableList();
        }
      } catch (Exception ex) {
      }
    });
    return addRowButton;
  }

  private JButton getDeleteRowButton(JTable table) {
    JButton deleteRowButton = new JButton("Видалити рядок");
    deleteRowButton.addActionListener(e -> {
      if (database == null) {
        JOptionPane.showMessageDialog(this, "No open database");
        return;
      }
      String tableName = (String) tableList.getSelectedValue();
      int selectedRow = table.getSelectedRow();

      StringBuilder whereClause = new StringBuilder();
      for (int col = 0; col < table.getColumnCount(); col++) {
        String columnName = table.getColumnName(col);
        Object cellValue = table.getValueAt(selectedRow, col);

        whereClause.append(columnName)
            .append("=")
            .append(cellValue.toString())
            .append(", ");
      }

      if (!whereClause.isEmpty()) {
        whereClause.setLength(whereClause.length() - 2);
      }

      displayQueryResults(database.query("delete from " + tableName + " where " + whereClause), false);
      populateTableList();
    });
    return deleteRowButton;
  }

  private void populateTableList() {
    if (database != null) {
      Result tables = database.query("list tables");
      tableListModel.clear();
      for (Row row : tables.getRows()) {
        tableListModel.addElement(row.getElement("table_name").getValue());
      }
    }
  }

  private void displayQueryResults(Result result, boolean isTableDisplayed) {
    tableControlPanel.setVisible(isTableDisplayed);

    if (result.getStatus() == Result.Status.FAIL) {
      JOptionPane.showMessageDialog(
          null,
          "Result: " + "\n" + result.getReport(),
          "Error",
          JOptionPane.ERROR_MESSAGE
      );
    }
    resultTableModel.setResult(result);
  }
}