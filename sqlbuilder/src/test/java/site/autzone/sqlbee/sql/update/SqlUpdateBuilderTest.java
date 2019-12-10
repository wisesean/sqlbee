package site.autzone.sqlbee.sql.update;

import org.junit.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import site.autzone.sqlbee.model.Value;
import site.autzone.sqlbee.sql.update.SqlUpdate;
import site.autzone.sqlbee.sql.update.builder.SqlUpateBuilder;

/**
 * sql更新构建器测试用例
 * @author wisesean
 *
 */
public class SqlUpdateBuilderTest extends TestCase {
	public SqlUpdateBuilderTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(SqlUpdateBuilderTest.class);
	}

	public void testApp() throws Exception {
		SqlUpateBuilder sb = new SqlUpateBuilder()
				.from().updateObject("TABLE").end()
				.updateItem().item("NAME", "TEST").end();
		
		//更新表格TABLE, 无条件
		SqlUpdate sqlUpdate = sb.build();
		Assert.assertEquals("UPDATE TABLE SET NAME = ?", sqlUpdate.toText());
		Assert.assertEquals(1, sqlUpdate.getValues().size());
		Assert.assertEquals("TEST", sqlUpdate.getValues().get(0));
		
		//带条件更新表格TABLE
		sb.binaryCondition("=").left("ID").right(new Value("123")).end();
		sqlUpdate = sb.build();
		Assert.assertEquals("UPDATE TABLE SET NAME = ? WHERE (ID = ?)", sqlUpdate.toText());
		Assert.assertEquals(2, sqlUpdate.getValues().size());
		Assert.assertEquals("TEST", sqlUpdate.getValues().get(0));
		Assert.assertEquals("123", sqlUpdate.getValues().get(1));
		
		//增加更新项
		sb.updateItem().item("CREATE_DATE", "2018").item("NUMBER", new Value(10, Value.TYPE.INT.name()));
		sqlUpdate = sb.build();
		Assert.assertEquals("UPDATE TABLE SET NAME = ?,CREATE_DATE = ?,NUMBER = ? WHERE (ID = ?)", sqlUpdate.toText());
		Assert.assertEquals(4, sqlUpdate.getValues().size());
		Assert.assertEquals("TEST", sqlUpdate.getValues().get(0));
		Assert.assertEquals("123", sqlUpdate.getValues().get(3));
		Assert.assertEquals("2018", sqlUpdate.getValues().get(1));
		Assert.assertEquals(10, sqlUpdate.getValues().get(2));
	}
}
