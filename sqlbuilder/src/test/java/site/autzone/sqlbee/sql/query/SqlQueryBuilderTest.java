package site.autzone.sqlbee.sql.query;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import site.autzone.sqlbee.model.Field;
import site.autzone.sqlbee.model.IValue;
import site.autzone.sqlbee.model.Value;
import site.autzone.sqlbee.sql.query.SqlQuery;
import site.autzone.sqlbee.sql.query.builder.SqlQueryBuilder;

/**
 * sql查询构建器测试用例
 *
 * @author wisesean
 */
public class SqlQueryBuilderTest {
	@Test
	public void testApp() throws Exception {
		SqlQueryBuilder sb = new SqlQueryBuilder().from().queryObject("TABLE1", "T1").end();

		// 拼装查询一张表的语句
		SqlQuery sqlQuery = sb.build();
		Assert.assertEquals("SELECT T1.* FROM TABLE1 T1", sqlQuery.toText());
		Assert.assertEquals(0, sqlQuery.getValues().size());

		// 拼装两张表关联查询
		sb.select().cloumn("T1.*").end().from().queryObject("TABLE2", "T2").end().binaryCondition("=").left("T1.ID")
				.right("T2.T1_ID").end();
		sqlQuery = sb.build();
		Assert.assertEquals("SELECT T1.* FROM TABLE1 T1,TABLE2 T2 WHERE (T1.ID = T2.T1_ID)", sqlQuery.toText());
		Assert.assertEquals(0, sqlQuery.getValues().size());

		// 拼装两张表关联查询,带二元查询条件
		sb.binaryCondition().connector("=").left("T1.CATAGORY").right(new Value("TEST")).end();
		sqlQuery = sb.build();
		Assert.assertEquals("SELECT T1.* FROM TABLE1 T1,TABLE2 T2 WHERE (T1.ID = T2.T1_ID) AND (T1.CATAGORY = ?)",
				sqlQuery.toText());
		Assert.assertEquals(1, sqlQuery.getValues().size());
		Assert.assertEquals("TEST", sqlQuery.getValues().get(0));

		// 拼装两张表关联查询,带二元查询条件,增加排序
		sb.orderBy().field("T1.CREATE_DATE").order("DESC").end();
		sqlQuery = sb.build();

		Assert.assertEquals(
				"SELECT T1.* FROM TABLE1 T1,TABLE2 T2 WHERE (T1.ID = T2.T1_ID) AND (T1.CATAGORY = ?) ORDER BY T1.CREATE_DATE DESC",
				sqlQuery.toText());
		Assert.assertEquals(1, sqlQuery.getValues().size());
		Assert.assertEquals("TEST", sqlQuery.getValues().get(0));

		// 去掉排序
		sb.removeOrderBy();
		sqlQuery = sb.build();
		Assert.assertEquals("SELECT T1.* FROM TABLE1 T1,TABLE2 T2 WHERE (T1.ID = T2.T1_ID) AND (T1.CATAGORY = ?)",
				sqlQuery.toText());
		Assert.assertEquals(1, sqlQuery.getValues().size());
		Assert.assertEquals("TEST", sqlQuery.getValues().get(0));

		// LIMIT查询
		sb.firstResult(100).end().maxResults(400);
		sqlQuery = sb.build();
		Assert.assertEquals(
				"SELECT * FROM (SELECT A.*, ROWNUM RNUM FROM (SELECT T1.* FROM TABLE1 T1,TABLE2 T2 WHERE (T1.ID = T2.T1_ID) AND (T1.CATAGORY = ?)) A WHERE ROWNUM <= ?) WHERE RNUM >= ?",
				sqlQuery.toText());
		Assert.assertEquals(3, sqlQuery.getValues().size());
		Assert.assertEquals("TEST", sqlQuery.getValues().get(0));
		Assert.assertEquals(400, sqlQuery.getValues().get(1));
		Assert.assertEquals(100, sqlQuery.getValues().get(2));

		// MYSQL LIMIT查询
		sb.isMysql();
		sqlQuery = sb.build();
		Assert.assertEquals(
				"SELECT T1.* FROM TABLE1 T1,TABLE2 T2 WHERE (T1.ID = T2.T1_ID) AND (T1.CATAGORY = ?) LIMIT ?,?",
				sqlQuery.toText());
		Assert.assertEquals(3, sqlQuery.getValues().size());
		Assert.assertEquals("TEST", sqlQuery.getValues().get(0));
		Assert.assertEquals(100, sqlQuery.getValues().get(1));
		Assert.assertEquals(400, sqlQuery.getValues().get(2));

		// 移除LIMIT
		sb.removeFirstResult().removeMaxResults();

		// COUNT查询
		sb.isCount();
		sqlQuery = sb.build();
		Assert.assertEquals(
				"SELECT COUNT(T1.*) COUNT FROM TABLE1 T1,TABLE2 T2 WHERE (T1.ID = T2.T1_ID) AND (T1.CATAGORY = ?)",
				sqlQuery.toText());
		Assert.assertEquals(1, sqlQuery.getValues().size());
		Assert.assertEquals("TEST", sqlQuery.getValues().get(0));

		// 去掉COUNT
		sb.isCount(false);
		sqlQuery = sb.build();
		Assert.assertEquals("SELECT T1.* FROM TABLE1 T1,TABLE2 T2 WHERE (T1.ID = T2.T1_ID) AND (T1.CATAGORY = ?)",
				sqlQuery.toText());
		Assert.assertEquals(1, sqlQuery.getValues().size());
		Assert.assertEquals("TEST", sqlQuery.getValues().get(0));

		// 多列查询
		sb.select().cloumn("T2", "ID", "ID2").cloumn("T2.CODE", "CODE2").end();
		sqlQuery = sb.build();
		Assert.assertEquals(
				"SELECT T1.*,T2.ID ID2,T2.CODE CODE2 FROM TABLE1 T1,TABLE2 T2 WHERE (T1.ID = T2.T1_ID) AND (T1.CATAGORY = ?)",
				sqlQuery.toText());
		Assert.assertEquals(1, sqlQuery.getValues().size());
		Assert.assertEquals("TEST", sqlQuery.getValues().get(0));

		// IS NULL查询条件
		sb.isNullCondition().field(new Field("T1.CREATOR")).end();
		sqlQuery = sb.build();
		Assert.assertEquals(
				"SELECT T1.*,T2.ID ID2,T2.CODE CODE2 FROM TABLE1 T1,TABLE2 T2 WHERE (T1.ID = T2.T1_ID) AND (T1.CATAGORY = ?) AND (T1.CREATOR IS NULL)",
				sqlQuery.toText());
		Assert.assertEquals(1, sqlQuery.getValues().size());
		Assert.assertEquals("TEST", sqlQuery.getValues().get(0));

		// IS NOT NULL查询条件
		sb.isNotNullCondition().field(new Field("T2.ENABLED")).end();
		sqlQuery = sb.build();
		Assert.assertEquals(
				"SELECT T1.*,T2.ID ID2,T2.CODE CODE2 FROM TABLE1 T1,TABLE2 T2 WHERE (T1.ID = T2.T1_ID) AND (T1.CATAGORY = ?) AND (T1.CREATOR IS NULL) AND (T2.ENABLED IS NOT NULL)",
				sqlQuery.toText());
		Assert.assertEquals(1, sqlQuery.getValues().size());
		Assert.assertEquals("TEST", sqlQuery.getValues().get(0));

		// BETWEEN查询条件
		sb.betweenCondition().field("T1.START_DATE").leftValue(new Value(2015, Value.TYPE.INT.name()))
				.rightValue(new Value(2018, Value.TYPE.INT.name())).end();
		sqlQuery = sb.build();
		Assert.assertEquals(
				"SELECT T1.*,T2.ID ID2,T2.CODE CODE2 FROM TABLE1 T1,TABLE2 T2 WHERE (T1.ID = T2.T1_ID) AND (T1.CATAGORY = ?) AND (T1.CREATOR IS NULL) AND (T2.ENABLED IS NOT NULL) AND (T1.START_DATE BETWEEN ? AND ?)",
				sqlQuery.toText());
		Assert.assertEquals(3, sqlQuery.getValues().size());
		Assert.assertEquals("TEST", sqlQuery.getValues().get(0));
		Assert.assertEquals(2015, sqlQuery.getValues().get(1));
		Assert.assertEquals(2018, sqlQuery.getValues().get(2));

		// IN查询条件
		sb.inCondition().field("T2.CODE").addValue(new Value("1")).addValue(new Value("2")).addValue(new Value("3"))
				.addValue(new Value("4")).addValue(new Value("5")).addValue(new Value("6")).end();
		sqlQuery = sb.build();
		Assert.assertEquals(
				"SELECT T1.*,T2.ID ID2,T2.CODE CODE2 FROM TABLE1 T1,TABLE2 T2 WHERE (T1.ID = T2.T1_ID) AND (T1.CATAGORY = ?) AND (T1.CREATOR IS NULL) AND (T2.ENABLED IS NOT NULL) AND (T1.START_DATE BETWEEN ? AND ?) AND (T2.CODE IN(?,?,?,?,?,?))",
				sqlQuery.toText());
		Assert.assertEquals(9, sqlQuery.getValues().size());
		Assert.assertEquals("TEST", sqlQuery.getValues().get(0));
		Assert.assertEquals(2015, sqlQuery.getValues().get(1));
		Assert.assertEquals(2018, sqlQuery.getValues().get(2));
		Assert.assertEquals("1", sqlQuery.getValues().get(3));
		Assert.assertEquals("2", sqlQuery.getValues().get(4));
		Assert.assertEquals("3", sqlQuery.getValues().get(5));
		Assert.assertEquals("4", sqlQuery.getValues().get(6));
		Assert.assertEquals("5", sqlQuery.getValues().get(7));
		Assert.assertEquals("6", sqlQuery.getValues().get(8));

		// 大于1000个值的IN语句
		List<IValue> values = new ArrayList<>();
		for (int i = 0; i < 2235; i++) {
			values.add(new Value("test" + i));
		}

		sb.inCondition().field(new Field("T1.NAME")).addValue(values).end();
		sqlQuery = sb.build();
		Assert.assertEquals(
				"SELECT T1.*,T2.ID ID2,T2.CODE CODE2 FROM TABLE1 T1,TABLE2 T2 WHERE (T1.ID = T2.T1_ID) AND (T1.CATAGORY = ?) AND (T1.CREATOR IS NULL) AND (T2.ENABLED IS NOT NULL) AND (T1.START_DATE BETWEEN ? AND ?) AND (T2.CODE IN(?,?,?,?,?,?)) AND (T1.NAME IN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) OR T1.NAME IN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) OR T1.NAME IN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?))",
				sqlQuery.toText());
		Assert.assertEquals(2244, sqlQuery.getValues().size());
		Assert.assertEquals("TEST", sqlQuery.getValues().get(0));
		Assert.assertEquals(2015, sqlQuery.getValues().get(1));
		Assert.assertEquals(2018, sqlQuery.getValues().get(2));
		Assert.assertEquals("1", sqlQuery.getValues().get(3));
		Assert.assertEquals("2", sqlQuery.getValues().get(4));
		Assert.assertEquals("3", sqlQuery.getValues().get(5));
		Assert.assertEquals("4", sqlQuery.getValues().get(6));
		Assert.assertEquals("5", sqlQuery.getValues().get(7));
		Assert.assertEquals("6", sqlQuery.getValues().get(8));

		for (int i = 0; i < 2235; i++) {
			Assert.assertEquals("test" + i, sqlQuery.getValues().get(i + 9));
		}
	}
}
