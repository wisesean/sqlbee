package site.autzone.sqlbee.sql.insert;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import site.autzone.sqlbee.model.Field;
import site.autzone.sqlbee.model.Value;
import site.autzone.sqlbee.sql.insert.SqlInsert;
import site.autzone.sqlbee.sql.insert.builder.SqlInsertBuilder;

/**
 * sql更新构建器测试用例
 * 
 * @author wisesean
 *
 */
public class SqlInsertBuilderTest {

  @Test
  public void testApp() throws Exception {
    Date currentDate = new Date();
    SqlInsert sqlInsert = new SqlInsertBuilder().from().insertObject("TABLE").end().insertItem()
        .item("NAME", "TEST").end().insertItem()
        .item("CREATE_DATE", new Value(currentDate, Value.TYPE.DATE.getName())).end().insertItem()
        .item(new Field("DESC"), new Value("这是一个测试的语句！")).end().build();
    Assert.assertEquals("INSERT INTO TABLE(NAME,CREATE_DATE,DESC) VALUES(?,?,?)",
        sqlInsert.toText());
    Assert.assertEquals(3, sqlInsert.getValues().size());
    Assert.assertEquals("TEST", sqlInsert.getValues().get(0));
    Assert.assertEquals(currentDate, sqlInsert.getValues().get(1));
    Assert.assertEquals("这是一个测试的语句！", sqlInsert.getValues().get(2));
  }
}
