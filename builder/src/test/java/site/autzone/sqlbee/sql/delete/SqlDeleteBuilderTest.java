package site.autzone.sqlbee.sql.delete;

import org.junit.Assert;
import org.junit.Test;
import site.autzone.sqlbee.value.Value;
import site.autzone.sqlbee.builder.SqlBuilder;

/**
 * 测试删除语句
 */
public class SqlDeleteBuilderTest {
    @Test
    public void testApp() throws Exception {
        Assert.assertEquals("DELETE FROM TABLE1", SqlBuilder.createDelete()
                .table("TABLE1").sql().prepareSql());
        Assert.assertEquals("DELETE FROM TABLE1 AS T1", SqlBuilder.createDelete()
                .table("TABLE1", "T1").sql().prepareSql());
        Assert.assertEquals("DELETE FROM TABLE1 AS T1 WHERE (T1.ID = ?)", SqlBuilder.createDelete()
                .table("TABLE1", "T1")
                .condition("=").left("T1.ID").right(new Value(1111)).end()
                .sql().prepareSql());
    }
}
