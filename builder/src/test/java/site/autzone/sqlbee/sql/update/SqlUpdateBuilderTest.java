package site.autzone.sqlbee.sql.update;

import org.junit.Assert;
import org.junit.Test;
import site.autzone.sqlbee.value.Value;
import site.autzone.sqlbee.sql.Sql;
import site.autzone.sqlbee.builder.SqlBuilder;

/**
 * sql更新构建器测试用例
 *
 * @author wisesean
 */
public class SqlUpdateBuilderTest {
  /**
   * 测试更新字段为空值
   */
  @Test
  public void testNullValue() {
    SqlBuilder sb = SqlBuilder.createUpdate().table("TABLE").column("NAME", new Value(null)).end();
    Assert.assertEquals("UPDATE TABLE SET NAME = ?", sb.build().output());
  }
  @Test
  public void testApp() throws Exception {
    SqlBuilder sb = SqlBuilder.createUpdate().table("TABLE").column("NAME", "TEST").end();

    // 更新表格TABLE, 无条件
    Sql sqlUpdate = sb.build();
    Assert.assertEquals("UPDATE TABLE SET NAME = ?", sqlUpdate.output());
    Assert.assertEquals(1, sqlUpdate.getParameters().size());
    Assert.assertEquals("TEST", sqlUpdate.getParameters().get(0));

    // 带条件更新表格TABLE
    sb.condition("=").left("ID").right(new Value(123)).end();
    sqlUpdate = sb.build();
    Assert.assertEquals("UPDATE TABLE SET NAME = ? WHERE (ID = ?)", sqlUpdate.output());
    Assert.assertEquals(2, sqlUpdate.getParameters().size());
    Assert.assertEquals("TEST", sqlUpdate.getParameters().get(0));
    Assert.assertEquals(123, sqlUpdate.getParameters().get(1));

    // 增加更新项
    sb.update()
        .updateColumn("CREATE_DATE", "2018")
        .updateColumn("NUMBER", new Value(10, Integer.class));
    sqlUpdate = sb.build();
    Assert.assertEquals(
        "UPDATE TABLE SET NAME = ?,CREATE_DATE = ?,NUMBER = ? WHERE (ID = ?)", sqlUpdate.output());
    Assert.assertEquals(4, sqlUpdate.getParameters().size());
    Assert.assertEquals("TEST", sqlUpdate.getParameters().get(0));
    Assert.assertEquals(123, sqlUpdate.getParameters().get(3));
    Assert.assertEquals("2018", sqlUpdate.getParameters().get(1));
    Assert.assertEquals(10, sqlUpdate.getParameters().get(2));
  }
}
