package site.autzone.sqlbee.sql.insert;

import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.value.Value;
import site.autzone.sqlbee.sql.Sql;
import site.autzone.sqlbee.builder.SqlBuilder;

/**
 * sql更新构建器测试用例
 *
 * @author wisesean
 */
public class SqlInsertBuilderTest {

  @Test
  public void testApp() throws Exception {
    Date currentDate = new Date();
    Sql sqlInsert =
        SqlBuilder.createInsert()
            .table("TABLE")
            .column("NAME", "TEST")
            .column("CREATE_DATE", new Value(currentDate, Date.class))
            .column(new Column("DESC"), new Value("这是一个测试的语句！"))
            .build();
    Assert.assertEquals("INSERT INTO TABLE(NAME,CREATE_DATE,DESC) VALUES(?,?,?)", sqlInsert.output());
    Assert.assertEquals(3, sqlInsert.getParameters().size());
    Assert.assertEquals("TEST", sqlInsert.getParameters().get(0));
    Assert.assertEquals(currentDate, sqlInsert.getParameters().get(1));
    Assert.assertEquals("这是一个测试的语句！", sqlInsert.getParameters().get(2));
  }

  @Test
  public void testNullValue() {
    Sql sqlInsert =
            SqlBuilder.createInsert()
                    .table("TABLE")
                    .column("NAME", "TEST")
                    .column("CREATE_DATE", new Value(null))
                    .column(new Column("DESC"), new Value("这是一个测试的语句！"))
                    .sql();
    Assert.assertEquals("INSERT INTO TABLE(NAME,CREATE_DATE,DESC) VALUES(?,?,?)", sqlInsert.prepareSql());
  }
}
