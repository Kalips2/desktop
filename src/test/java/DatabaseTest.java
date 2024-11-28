import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertSame;

import database.Database;
import database.Row;
import java.util.List;
import org.junit.Test;

public class DatabaseTest {

  @Test
  public void createTable() {
    Database database = new Database(null);
    database.query("CREATE TABLE TEST1 (INT id, MONEY amountMoney)");
    database.query("CREATE TABLE TEST2 (INT id, MONEY amountMoney)");

    assertSame(2, database.getTableNames().size());
  }

  @Test
  public void deleteTable() {
    Database database = new Database(null);
    database.query("CREATE TABLE TEST1 (INT id, MONEY amountMoney)");
    database.query("CREATE TABLE TEST2 (INT id, MONEY amountMoney)");
    database.query("CREATE TABLE TEST3 (INT id, MONEY amountMoney)");

    database.query("DELETE TABLE TEST2");

    assertSame(2, database.getTableNames().size());
  }

  @Test
  public void sortByIdDescending() throws Exception {
    Database database = new Database(null);
    database.query("CREATE TABLE TEST1 (INT id, STR name, MONEY money, MONEY_INV moneyRange)");

    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(3, Charlie, 5, 7;8)");
    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(1, Alice, 6, 9;10)");
    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(2, Bob, 2, 10;400)");

    database.query("SORT FROM TEST1 BY id DESC");
    List<Row> rows = database.getTableRows("TEST1").stream().toList();
    Row row1 = rows.get(0);
    assertEquals("3", row1.getElement("id").getAsString());
    assertEquals("Charlie", row1.getElement("name").getAsString());
    assertEquals("$5.00", row1.getElement("money").getValue());
    assertEquals("$7.00;$8.00", row1.getElement("moneyRange").getValue());

    Row row2 = rows.get(1);
    assertEquals("2", row2.getElement("id").getAsString());
    assertEquals("Bob", row2.getElement("name").getAsString());
    assertEquals("$2.00", row2.getElement("money").getValue());
    assertEquals("$10.00;$400.00", row2.getElement("moneyRange").getValue());

    Row row3 = rows.get(2);
    assertEquals("1", row3.getElement("id").getAsString());
    assertEquals("Alice", row3.getElement("name").getAsString());
    assertEquals("$6.00", row3.getElement("money").getValue());
    assertEquals("$9.00;$10.00", row3.getElement("moneyRange").getValue());
  }

  @Test
  public void sortByMoneyDescending() throws Exception {
    Database database = new Database(null);
    database.query("CREATE TABLE TEST1 (INT id, STR name, MONEY money, MONEY_INV moneyRange)");

    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(3, Charlie, 5, 7;8)");
    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(1, Alice, 6, 9;10)");
    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(2, Bob, 2, 10;400)");

    database.query("SORT FROM TEST1 BY money DESC");
    List<Row> rows = database.getTableRows("TEST1").stream().toList();

    Row row1 = rows.get(0);
    assertEquals("1", row1.getElement("id").getAsString());
    assertEquals("Alice", row1.getElement("name").getAsString());
    assertEquals("$6.00", row1.getElement("money").getValue());
    assertEquals("$9.00;$10.00", row1.getElement("moneyRange").getValue());

    Row row2 = rows.get(1);
    assertEquals("3", row2.getElement("id").getAsString());
    assertEquals("Charlie", row2.getElement("name").getAsString());
    assertEquals("$5.00", row2.getElement("money").getValue());
    assertEquals("$7.00;$8.00", row2.getElement("moneyRange").getValue());

    Row row3 = rows.get(2);
    assertEquals("2", row3.getElement("id").getAsString());
    assertEquals("Bob", row3.getElement("name").getAsString());
    assertEquals("$2.00", row3.getElement("money").getValue());
    assertEquals("$10.00;$400.00", row3.getElement("moneyRange").getValue());
  }

  @Test
  public void sortByMoneyRangeDescending() throws Exception {
    Database database = new Database(null);
    database.query("CREATE TABLE TEST1 (INT id, STR name, MONEY money, MONEY_INV moneyRange)");

    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(3, Charlie, 5, 7;8)");
    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(1, Alice, 6, 9;10)");
    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(2, Bob, 2, 10;400)");

    database.query("SORT FROM TEST1 BY moneyRange DESC");
    List<Row> rows = database.getTableRows("TEST1").stream().toList();

    Row row1 = rows.get(0);
    assertEquals("2", row1.getElement("id").getAsString());
    assertEquals("Bob", row1.getElement("name").getAsString());
    assertEquals("$2.00", row1.getElement("money").getValue());
    assertEquals("$10.00;$400.00", row1.getElement("moneyRange").getValue());

    Row row2 = rows.get(1);
    assertEquals("1", row2.getElement("id").getAsString());
    assertEquals("Alice", row2.getElement("name").getAsString());
    assertEquals("$6.00", row2.getElement("money").getValue());
    assertEquals("$9.00;$10.00", row2.getElement("moneyRange").getValue());

    Row row3 = rows.get(2);
    assertEquals("3", row3.getElement("id").getAsString());
    assertEquals("Charlie", row3.getElement("name").getAsString());
    assertEquals("$5.00", row3.getElement("money").getValue());
    assertEquals("$7.00;$8.00", row3.getElement("moneyRange").getValue());
  }

  @Test
  public void sortByIdAscending() throws Exception {
    Database database = new Database(null);
    database.query("CREATE TABLE TEST1 (INT id, STR name, MONEY money, MONEY_INV moneyRange)");

    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(3, Charlie, 5, 7;8)");
    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(1, Alice, 6, 9;10)");
    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(2, Bob, 2, 10;400)");

    database.query("SORT FROM TEST1 BY id ASC");
    List<Row> rows = database.getTableRows("TEST1").stream().toList();
    Row row3 = rows.get(2);
    assertEquals("3", row3.getElement("id").getAsString());
    assertEquals("Charlie", row3.getElement("name").getAsString());
    assertEquals("$5.00", row3.getElement("money").getValue());
    assertEquals("$7.00;$8.00", row3.getElement("moneyRange").getValue());

    Row row2 = rows.get(1);
    assertEquals("2", row2.getElement("id").getAsString());
    assertEquals("Bob", row2.getElement("name").getAsString());
    assertEquals("$2.00", row2.getElement("money").getValue());
    assertEquals("$10.00;$400.00", row2.getElement("moneyRange").getValue());

    Row row1 = rows.get(0);
    assertEquals("1", row1.getElement("id").getAsString());
    assertEquals("Alice", row1.getElement("name").getAsString());
    assertEquals("$6.00", row1.getElement("money").getValue());
    assertEquals("$9.00;$10.00", row1.getElement("moneyRange").getValue());
  }

  @Test
  public void sortByMoneyAscending() throws Exception {
    Database database = new Database(null);
    database.query("CREATE TABLE TEST1 (INT id, STR name, MONEY money, MONEY_INV moneyRange)");

    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(3, Charlie, 5, 7;8)");
    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(1, Alice, 6, 9;10)");
    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(2, Bob, 2, 10;400)");

    database.query("SORT FROM TEST1 BY money ASC");
    List<Row> rows = database.getTableRows("TEST1").stream().toList();

    Row row3 = rows.get(2);
    assertEquals("1", row3.getElement("id").getAsString());
    assertEquals("Alice", row3.getElement("name").getAsString());
    assertEquals("$6.00", row3.getElement("money").getValue());
    assertEquals("$9.00;$10.00", row3.getElement("moneyRange").getValue());

    Row row2 = rows.get(1);
    assertEquals("3", row2.getElement("id").getAsString());
    assertEquals("Charlie", row2.getElement("name").getAsString());
    assertEquals("$5.00", row2.getElement("money").getValue());
    assertEquals("$7.00;$8.00", row2.getElement("moneyRange").getValue());

    Row row1 = rows.get(0);
    assertEquals("2", row1.getElement("id").getAsString());
    assertEquals("Bob", row1.getElement("name").getAsString());
    assertEquals("$2.00", row1.getElement("money").getValue());
    assertEquals("$10.00;$400.00", row1.getElement("moneyRange").getValue());
  }


  @Test
  public void sortByMoneyRangeAscending() throws Exception {
    Database database = new Database(null);
    database.query("CREATE TABLE TEST1 (INT id, STR name, MONEY money, MONEY_INV moneyRange)");

    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(3, Charlie, 5, 7;8)");
    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(1, Alice, 6, 9;10)");
    database.query("INSERT INTO TEST1 (id, name, money, moneyRange) VALUES(2, Bob, 2, 10;400)");

    database.query("SORT FROM TEST1 BY moneyRange ASC");
    List<Row> rows = database.getTableRows("TEST1").stream().toList();

    Row row1 = rows.get(0);
    assertEquals("3", row1.getElement("id").getAsString());
    assertEquals("Charlie", row1.getElement("name").getAsString());
    assertEquals("$5.00", row1.getElement("money").getValue());
    assertEquals("$7.00;$8.00", row1.getElement("moneyRange").getValue());

    Row row3 = rows.get(2);
    assertEquals("2", row3.getElement("id").getAsString());
    assertEquals("Bob", row3.getElement("name").getAsString());
    assertEquals("$2.00", row3.getElement("money").getValue());
    assertEquals("$10.00;$400.00", row3.getElement("moneyRange").getValue());

    Row row2 = rows.get(1);
    assertEquals("1", row2.getElement("id").getAsString());
    assertEquals("Alice", row2.getElement("name").getAsString());
    assertEquals("$6.00", row2.getElement("money").getValue());
    assertEquals("$9.00;$10.00", row2.getElement("moneyRange").getValue());
  }
}
