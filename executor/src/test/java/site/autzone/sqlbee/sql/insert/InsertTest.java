package site.autzone.sqlbee.sql.insert;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import site.autzone.sqlbee.ISql;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.value.Value;

/**
 * sql更新构建器测试用例
 * 
 * @author wisesean
 *
 */
public class InsertTest {
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
        "INSERT INTO employee (firstname,lastname,salary,hireddate) VALUES (?,?,?,?);",
        new Object[][] {{"John", "Doe", 10000.10, new Date()}});
    for (int i : ret) {
      assertEquals(1, i);
    }
  }

  @Test
  public void insertTest() {
    Assert.assertEquals(1, insert(SqlBuilder.createInsert().table("employee").end()
            .insert().insertColumn("firstname", new Value("John1"))
            .insertColumn("lastname", new Value("ddd"))
            .insertColumn("salary", new Value("10000.10"))
            .insertColumn("hireddate", new Value("2020-12-01"))
            .end().build()));
  }

  public int insert(ISql sql) {
    QueryRunner run = new QueryRunner();
    try {
      return run.execute(connection, sql.output(), sql.getParameters().toArray());
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
  
  @After
  public void closeBD() {
    DbUtils.closeQuietly(connection);
  }
}
