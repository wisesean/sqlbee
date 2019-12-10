package site.autzone.sqlbee.sql.insert;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * sql更新构建器测试用例
 * 
 * @author wisesean
 *
 */
public class SqlInsertBuilderTest {
  private Connection connection;

  @Before
  public void setupDB() throws Exception {
    Class.forName("org.h2.Driver");
    Class.forName("org.h2.Driver");
    String db = "jdbc:h2:mem:;INIT=runscript from 'classpath:/employees.sql'";
    connection = DriverManager.getConnection(db);
  }

  @Test
  public void batchInsertTest() throws Exception {
    QueryRunner run = new QueryRunner();
    int[] ret = run.batch(connection,
        "INSERT INTO employee (firstname,lastname,salary,hireddate)  " + "VALUES (?,?,?,?);",
        new Object[][] {{"John", "Doe", 10000.10, new Date()}});
    for (int i : ret) {
      assertEquals(1, i);
    }
  }
  
  @After
  public void closeBD() {
    DbUtils.closeQuietly(connection);
  }
}
