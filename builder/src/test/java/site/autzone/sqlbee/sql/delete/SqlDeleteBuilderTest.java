package site.autzone.sqlbee.sql.delete;

import org.junit.Assert;
import org.junit.Test;
import site.autzone.sqlbee.value.Value;
import site.autzone.sqlbee.builder.SqlBuilder;

public class SqlDeleteBuilderTest {
    @Test
    public void testApp() throws Exception {
        Assert.assertEquals("DELETE * FROM TABLE1", SqlBuilder.createDelete().table("TABLE1").build().output());
        Assert.assertEquals("DELETE * FROM TABLE1 AS T1", SqlBuilder.createDelete().table("TABLE1", "T1").build().output());
        Assert.assertEquals("DELETE T1.* FROM TABLE1 AS T1", SqlBuilder.createDelete().table("TABLE1", "T1").end().select().column("T1.*").end().build().output());
        Assert.assertEquals("DELETE T1.* FROM TABLE1 AS T1 WHERE (T1.ID = ?)", SqlBuilder.createDelete().table("TABLE1", "T1").end().select().column("T1.*").end().condition("=").left("T1.ID").right(new Value(1111)).end().build().output());
    }
}
