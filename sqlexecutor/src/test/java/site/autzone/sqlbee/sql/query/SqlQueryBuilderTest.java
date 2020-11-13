package site.autzone.sqlbee.sql.query;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import site.autzone.sqlbee.model.DateValue;
import site.autzone.sqlbee.model.Value;
import site.autzone.sqlbee.query.IQuery;
import site.autzone.sqlbee.sql.query.builder.SqlQueryBuilder;
import site.autzone.sqlbee.sql.query.entity.Employee;

/**
 * sql查询构建器测试用例
 *
 * @author wisesean
 */
public class SqlQueryBuilderTest {
  private Connection connection;

  @Before
  public void setupDB() throws Exception {
    Class.forName("org.h2.Driver");
    Class.forName("org.h2.Driver");
    String db = "jdbc:h2:mem:;INIT=runscript from 'classpath:/employees.sql'";
    connection = DriverManager.getConnection(db);
  }
  
  @Test
  public void leftJoin() throws SQLException {
    SqlQueryBuilder sb = new SqlQueryBuilder().queryObject().queryObject("employee", "employee").end();
    sb.leftJoin("email", "email").addBinaryCondition("employee.id", "email.employeeid").end();
    sb.isCount();
    long count = count(sb.build());
    assertEquals(2, count);
  }
  
  @Test
  public void leftJoin2() throws SQLException {
    SqlQueryBuilder sb = new SqlQueryBuilder().queryObject().queryObject("employee", "employee").end();
    sb.select().cloumn("employee.*").end();
    sb.leftJoin(0, "email", "email").addBinaryCondition("employee.id", "email.employeeid").end();
    List<Employee> employees = queryBeans(sb.build(), Employee.class);
    assertEquals(2, employees.size());
  }
  
  @Test
  public void leftJoin3() throws SQLException {
    SqlQueryBuilder sb = new SqlQueryBuilder().queryObject().queryObject("email", "email").end();
    sb.select().cloumn("employee.*").end();
    sb.leftJoin(0, "employee", "employee").addBinaryCondition("employee.id", "email.employeeid").end();
    List<Employee> employees = queryBeans(sb.build(), Employee.class);
    assertEquals(3, employees.size());
  }
  
  @Test
  public void rightJoin() throws SQLException {
    SqlQueryBuilder sb = new SqlQueryBuilder().queryObject().queryObject("employee", "employee").end();
    sb.rightJoin("email", "email").addBinaryCondition("employee.id", "email.employeeid").end();
    sb.isCount();
    long count = count(sb.build());
    assertEquals(3, count);
  }
  
  @Test
  public void rightJoin2() throws SQLException {
    SqlQueryBuilder sb = new SqlQueryBuilder().queryObject().queryObject("employee", "employee").end();
    sb.select().cloumn("employee.*").end();
    sb.rightJoin(0, "email", "email").addBinaryCondition("employee.id", "email.employeeid").end();
    List<Employee> employees = queryBeans(sb.build(), Employee.class);
    assertEquals(3, employees.size());
  }
  
  @Test
  public void rightJoin3() throws SQLException {
    SqlQueryBuilder sb = new SqlQueryBuilder().queryObject().queryObject("email", "email").end();
    sb.select().cloumn("employee.*").end();
    sb.rightJoin(0, "employee", "employee").addBinaryCondition("employee.id", "email.employeeid").end();
    List<Employee> employees = queryBeans(sb.build(), Employee.class);
    assertEquals(2, employees.size());
  }


  @Test
  public void count() throws SQLException {
    SqlQueryBuilder sb = new SqlQueryBuilder().queryObject().queryObject("employee").end();
    sb.isCount();
    long count = count(sb.build());
    assertEquals(2, count);
  }

  @Test
  public void queryBeans() throws SQLException {
    SqlQueryBuilder sb = new SqlQueryBuilder().queryObject().queryObject("employee").end();
    List<Employee> employees = queryBeans(sb.build(), Employee.class);
    assertEquals(employees.size(), 2);
    assertEquals(employees.get(0).getLastname(), "Doe");
    sb.binaryCondition("=").left("hireddate")
        .right(new DateValue(false, "01-01-2001", "dd-mm-yyyy"));
    employees = queryBeans(sb.build(), Employee.class);
    assertEquals(employees.size(), 2);
    assertEquals(employees.get(0).getLastname(), "Doe");
  }

  public Date StrToDate(String str, String formatter) {
    SimpleDateFormat format = new SimpleDateFormat(formatter);
    Date date = null;
    try {
      date = format.parse(str);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }

  public Long count(IQuery query) {
    if (!query.isCount()) {
      throw new RuntimeException("非Count的Query不能支持!");
    }
    QueryRunner run = new QueryRunner();
    try {
      return run.query(connection, query.toText(), new ScalarHandler<Long>(),
          query.getValues().toArray());
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public <T> List<T> queryBeans(IQuery query, Class<T> t) {
    BeanProcessor bean = new GenerousBeanProcessor();
    RowProcessor processor = new BasicRowProcessor(bean);
    QueryRunner run = new QueryRunner();
    try {
      return run.query(connection, query.toText(), new BeanListHandler<T>(t, processor),
          query.getValues().toArray());
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
