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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import site.autzone.sqlbee.ISql;
import site.autzone.sqlbee.sql.Sql;
import site.autzone.sqlbee.sql.Table;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.sql.sql.entity.Employee;
import site.autzone.sqlbee.value.DateValue;
import site.autzone.sqlbee.value.Value;

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
    String db = "jdbc:h2:mem:;INIT=runscript from 'classpath:/employees.sql'";
    connection = DriverManager.getConnection(db);
  }

  @Test
  public void testValue() throws SQLException {
    SqlBuilder sb = SqlBuilder.createQuery().table("employee", "employee")
            .condition("=").left("employee.isleader").right(new Value(true)).end();
    List<Employee> list = this.queryBeans(sb.sql(), Employee.class);
    Assert.assertEquals(2, list.size());
    sb.condition("=").left("employee.salary").right(new Value(10000.10)).end();
    list = this.queryBeans(sb.sql(), Employee.class);
    Assert.assertEquals(1, list.size());
  }
  
  @Test
  public void leftJoin() throws SQLException {
    SqlBuilder sb = SqlBuilder.createQuery().table("employee", "employee")
            .leftJoin(new Table("email", "email")).joinCondition("employee.id", "email.employeeid")
            .end().isCount();
    long count = count(sb.build());
    assertEquals(2, count);
  }
  
  @Test
  public void leftJoin2() throws SQLException {
    SqlBuilder sb = SqlBuilder.createQuery().table("employee", "employee").end();
    sb.select().column("employee.*").end();
    sb.table().leftJoin(new Table("email", "email")).joinCondition("employee.id", "email.employeeid").end();
    List<Employee> employees = queryBeans(sb.build(), Employee.class);
    assertEquals(2, employees.size());
  }
  
  @Test
  public void leftJoin3() throws SQLException {
    SqlBuilder sb = SqlBuilder.createQuery().table("email", "email").end();
    sb.select().column("employee.*").end();
    sb.table().leftJoin(new Table("employee", "employee")).joinCondition("employee.id", "email.employeeid").end();
    List<Employee> employees = queryBeans(sb.build(), Employee.class);
    assertEquals(3, employees.size());
  }
  
  @Test
  public void rightJoin() throws SQLException {
    SqlBuilder sb = SqlBuilder.createQuery().table("employee", "employee").end();
    sb.table().rightJoin(new Table("email", "email")).joinCondition("employee.id", "email.employeeid").end();
    sb.isCount();
    long count = count(sb.build());
    assertEquals(3, count);
  }
  
  @Test
  public void rightJoin2() throws SQLException {
    SqlBuilder sb = SqlBuilder.createQuery().table("employee", "employee").end();
    sb.select().column("employee.*").end();
    sb.table().rightJoin(new Table("email", "email")).joinCondition("employee.id", "email.employeeid").end();
    List<Employee> employees = queryBeans(sb.build(), Employee.class);
    assertEquals(3, employees.size());
  }
  
  @Test
  public void rightJoin3() throws SQLException {
    SqlBuilder sb = SqlBuilder.createQuery().table("email", "email").end();
    sb.select().column("employee.*").end();
    sb.table().rightJoin(new Table(0, "employee", "employee")).joinCondition("employee.id", "email.employeeid").end();
    List<Employee> employees = queryBeans(sb.build(), Employee.class);
    assertEquals(2, employees.size());
  }


  @Test
  public void count() throws SQLException {
    SqlBuilder sb = SqlBuilder.createQuery().table("employee").end();
    sb.isCount();
    long count = count(sb.build());
    assertEquals(2, count);
  }

  @Test
  public void queryBeans() throws SQLException {
    SqlBuilder sb = SqlBuilder.createQuery().table("employee").end();
    List<Employee> employees = queryBeans(sb.build(), Employee.class);
    assertEquals(employees.size(), 2);
    assertEquals(employees.get(0).getLastname(), "Doe");
    sb.condition("=").left("hireddate")
        .right(new DateValue("01-01-2001", "dd-mm-yyyy"));
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

  public Long count(ISql sql) {
    if (!sql.isCount()) {
      throw new RuntimeException("非Count的Query不能支持!");
    }
    QueryRunner run = new QueryRunner();
    try {
      return run.query(connection, sql.output(), new ScalarHandler<Long>(),
          sql.getParameters().toArray());
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public <T> List<T> queryBeans(ISql sql, Class<T> t) {
    BeanProcessor bean = new GenerousBeanProcessor();
    RowProcessor processor = new BasicRowProcessor(bean);
    QueryRunner run = new QueryRunner();
    try {
      return run.query(connection, sql.prepareSql(), new BeanListHandler<T>(t, processor),
          sql.getParameters().toArray());
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
