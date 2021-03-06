package site.autzone.sqlbee.sql.query;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.IValue;
import site.autzone.sqlbee.condition.Condition;
import site.autzone.sqlbee.condition.Operator;
import site.autzone.sqlbee.sql.Table;
import site.autzone.sqlbee.value.Value;
import site.autzone.sqlbee.sql.Sql;
import site.autzone.sqlbee.builder.SqlBuilder;

/**
 * sql查询构建器测试用例
 *
 * @author wisesean
 */
public class SqlQueryBuilderTest {
    /**
     * 构建示例测试用例
     */
    @Test
    public void demo() {
        SqlBuilder.createQuery().aliasColumn("T1", "ID", "SS").table("TABLE1", "T1").sql();
        SqlBuilder.createQuery().table("TABLE1", "T1").maxResults(300).sql();
        SqlBuilder.createQuery()
                .table("TABLE1", "T1")
                .firstResults(100)
                .maxResults(300)
                .condition("=")
                .left("T1.CODE")
                .right(new Value("000000"))
                .end()
                .condition("=")
                .left("T1.is_new")
                .right(new Value(true))
                .end()
                .inCondition().column("T1.TID").subSql(SqlBuilder.createQuery().table("TABLE2", "T2").column("t2.id").condition("=")
                .left("T2.is_new")
                .right(new Value(true))
                .end()
                .sql())
                .end()
                .inCondition().column("T1.SID")
                .subSql("SELECT T3.ID FROM TABLE3 AS T3 WHERE T3.ID = T1.SID OR (1 = 1)").end()
                .sql();
    }

    /**
     * 测试多个SqlBuilder实例并存的时候是否出现问题
     */
    @Test
    public void testMultiSqlConfigurer() {
        SqlBuilder sql = SqlBuilder.createQuery().table("TABLE").end();
        SqlBuilder sql2 = SqlBuilder.createQuery().table("TABLE2").end();
        sql2.table().table("aaa", "bb").joinCondition("ss", "=", "bb.id");
        sql2.table().column("sss");

        sql2.condition("=").left("sss").right("2222");
        sql.table().table("bbb", "aaa").joinCondition("ss", "=", "aaa.id");
        sql.table().column("sss");

        sql2.table().table("TABLE3", "T3").joinCondition("S3", "=", "T3.SS");
        sql.table().table("TABLE3", "T3").joinCondition("S3", "=", "T3.SS");

        sql.isCount(true);
        Assert.assertEquals("SELECT COUNT(*) COUNT FROM TABLE INNER JOIN bbb AS aaa ON (ss = aaa.id) INNER JOIN TABLE3 AS T3 ON (S3 = T3.SS)",
                sql.build().output());
        Assert.assertEquals("SELECT sss FROM TABLE2 INNER JOIN aaa AS bb ON (ss = bb.id) INNER JOIN TABLE3 AS T3 ON (S3 = T3.SS) WHERE (sss = 2222)",
                sql2.build().output());
    }

    /**
     * 测试count 添加count字段
     */
    @Test
    public void testCount() {
        SqlBuilder sb = SqlBuilder.createQuery().table("Table1", "t1")
                .innerJoin(new Table("innerJoin", "t2")).joinCondition("t1.id","=","t2.parentId").end();
        sb.table().innerJoin(new Table("tab3", "t3")).joinCondition("t1.id","=","t3.parentId").end();
        sb.table().column("t1.id").column("t1.wsid", "wsid");
        sb.isCount(false);
        Assert.assertEquals("SELECT t1.id,t1.wsid FROM Table1 AS t1 INNER JOIN innerJoin AS t2 ON (t1.id = t2.parentId) INNER JOIN tab3 AS t3 ON (t1.id = t3.parentId)",
                sb.build().output());

        sb.isCount(true);
        //默认的COUNT语句
        Assert.assertEquals("SELECT COUNT(*) COUNT FROM Table1 AS t1 INNER JOIN innerJoin AS t2 ON (t1.id = t2.parentId) INNER JOIN tab3 AS t3 ON (t1.id = t3.parentId)",
                sb.build().output());

        //设置COUNT字段
        sb.table().countColumn("COUNT(DISTINCT T1.ID) COUNT");
        Assert.assertEquals("SELECT COUNT(DISTINCT T1.ID) COUNT FROM Table1 AS t1 INNER JOIN innerJoin AS t2 ON (t1.id = t2.parentId) INNER JOIN tab3 AS t3 ON (t1.id = t3.parentId)",
                sb.build().output());
        //每次取最后一个COUNT字段
        sb.select().countColumn("COUNT(T2.ID) COUNT");
        Assert.assertEquals("SELECT COUNT(T2.ID) COUNT FROM Table1 AS t1 INNER JOIN innerJoin AS t2 ON (t1.id = t2.parentId) INNER JOIN tab3 AS t3 ON (t1.id = t3.parentId)",
                sb.build().output());
    }

    @Test
    public void testSelect() {
        Sql sql= SqlBuilder.createQuery().table("TABLE1", "T1").end().select().column("t1", "id", "ID").end().select().column("T1", "wsid", "WSID").end().sql();
        Assert.assertEquals("SELECT t1.id AS ID,T1.wsid AS WSID FROM TABLE1 AS T1", sql.output());
    }

    @Test
    public void testUnion() {
        //UNION
        Sql sql = SqlBuilder.createQuery().table("TABLE1", "T1").union(SqlBuilder.createQuery().table("TABLE1", "T2").build()).sql();
        Assert.assertEquals("SELECT * FROM TABLE1 AS T1 UNION DISTINCT SELECT * FROM TABLE1 AS T2", sql.output());
        //UNION ALL
        sql = SqlBuilder.createQuery().table("TABLE1", "T1").unionAll(SqlBuilder.createQuery().table("TABLE1", "T2").build()).sql();
        Assert.assertEquals("SELECT * FROM TABLE1 AS T1 UNION ALL SELECT * FROM TABLE1 AS T2", sql.output());

        //带子查询的UNION
        sql = SqlBuilder.createQuery().table("TABLE1", "T1")
                .condition("=")
                .left("T1.is_new")
                .right(new Value(true))
                .end()
                .firstResults(100)
                .maxResults(300)
                .table().union(SqlBuilder.createQuery().table("TABLE1", "T2")
                .condition("=")
                .left("T2.is_new")
                .right(new Value(true))
                .end()
                .inCondition().column("T1.TID").subSql(SqlBuilder.createQuery().table("TABLE2", "T2").column("t2.id").condition("=")
                        .left("T2.is_new")
                        .right(new Value(true))
                        .end()
                        .sql())
                .end()
                .build()).sql();
        Assert.assertEquals("SELECT * FROM TABLE1 AS T1 WHERE (T1.is_new = ?) LIMIT ?,? UNION DISTINCT SELECT * FROM TABLE1 AS T2 WHERE (T2.is_new = ?) AND (T1.TID IN(SELECT t2.id FROM TABLE2 AS T2 WHERE (T2.is_new = ?)))", sql.output());
    }

    @Test
    public void groupHavingOrderTest() {
        SqlBuilder sb = SqlBuilder
                .createQuery().table("TABLE1", "T1")
                .groupBy("T1.CODE")
                .groupBy("T1.CLASS")
                .having(new Condition(Operator.GT.operator(), "COUNt(*)", new Value(2, Integer.class)))
                .order("T1.ID").desc().end();
        Sql sql = sb.build();
		Assert.assertEquals("SELECT * FROM TABLE1 AS T1 GROUP BY T1.CODE,T1.CLASS HAVING (COUNt(*) > ?) ORDER BY T1.ID DESC",
                sql.output());
        Assert.assertEquals(2, sql.getParameters().get(0));
    }

    @Test
    public void existsTest() {
        SqlBuilder sb = SqlBuilder.createQuery()
                .table("TABLE1", "T1").end()
                .condition(">").left("T1.CODE").right(new Value(111)).end()
                .exists().sub(
                        SqlBuilder.createSubQuery()
                                .table("TABLE2", "T2").end()
                                .condition("=").left("T2.NAME").right(new Value("测试")).end()
                                .condition("=").left("T1.ID").right("T2.ID").end()
                                .build())
                .end()
                .condition("=").left("T1.CLASS").right(new Value("$ABC")).end();
        Sql sql = sb.build();
        Assert.assertEquals("SELECT * FROM TABLE1 AS T1 WHERE (T1.CODE > ?) AND (T1.CLASS = ?) AND EXISTS (SELECT * FROM TABLE2 AS T2 WHERE (T2.NAME = ?) AND (T1.ID = T2.ID))",
                sql.output());
        Assert.assertEquals(111, sql.getParameters().get(0));
        Assert.assertEquals("$ABC", sql.getParameters().get(1));
        Assert.assertEquals("测试", sql.getParameters().get(2));
    }

    @Test
    public void notExistsTest() {
        SqlBuilder sb = SqlBuilder.createQuery().table("TABLE1", "T1").end()
                .notExists().sub(
                        SqlBuilder.createQuery()
                                .table("TABLE2", "T2").end()
                                .condition("=").left("T2.NAME").right(new Value("测试")).end()
                                .condition("=").left("T1.ID").right("T2.ID").end()
                                .build())
                .end();
        Assert.assertEquals("SELECT * FROM TABLE1 AS T1 WHERE " +
                        "NOT EXISTS (SELECT * FROM TABLE2 AS T2 WHERE (T2.NAME = ?) AND (T1.ID = T2.ID))",
                sb.build().output());
    }

    @Test
    public void testLeftJoinApp() throws Exception {
        SqlBuilder sb = SqlBuilder.createQuery().table("TABLE1", "T1").column("T1.*")
                .leftJoin(new Table("TABLE2", "T2")).joinCondition("T1.ID", "T2.ID").leftJoin(new Table("TABLE4", "T4"))
                .joinCondition("T1.ID", "T4.ID").end();
        Sql sqlQuery = sb.build();
        Assert.assertEquals(
                "SELECT T1.* FROM TABLE1 AS T1 LEFT JOIN TABLE2 AS T2 ON (T1.ID = T2.ID) LEFT JOIN TABLE4 AS T4 ON (T1.ID = T4.ID)",
                sqlQuery.output());

        sb.table().leftJoin(new Table("TABLE4", "T4")).joinCondition("T3.ID", new Value("1"))
                .end();
        sb.condition("LIKE").left("T1.NAME").right(new Value("test")).end();
        sqlQuery = sb.build();
        sqlQuery.output();
    }

    @Test
    public void testRightJoinApp() throws Exception {
        SqlBuilder sb = SqlBuilder.createQuery().table("TABLE1", "T1").rightJoin(new Table("TABLE2", "T2"))
                .joinCondition("T1.ID", "T2.ID").end().select().column("T1.*").end();
        Sql sqlQuery = sb.build();
        Assert.assertEquals("SELECT T1.* FROM TABLE1 AS T1 RIGHT JOIN TABLE2 AS T2 ON (T1.ID = T2.ID)", sqlQuery.output());

        sb.table().rightJoin(new Table(1, "TABLE4", "T4")).joinCondition("T3.ID", new Value("1"))
                .end();
        sb.condition("LIKE").left("T1.NAME").right(new Value("test")).end();
        sqlQuery = sb.build();
        sqlQuery.output();
    }

    @Test
    public void testApp() throws Exception {
        SqlBuilder sb = SqlBuilder.createQuery().table("TABLE1", "T1").end();

        // 拼装查询一张表的语句
        Sql sqlQuery = sb.build();
        Assert.assertEquals("SELECT * FROM TABLE1 AS T1", sqlQuery.output());
        Assert.assertEquals(0, sqlQuery.getParameters().size());

        // 拼装两张表关联查询
        sb.select().column("T1.*").end().table()
                .innerJoin(new Table("TABLE2", "T2"))
                .joinCondition("T2.ID", "T1.ID")
                .end();
        sqlQuery = sb.build();
        Assert.assertEquals("SELECT T1.* FROM TABLE1 AS T1 INNER JOIN TABLE2 AS T2 ON (T2.ID = T1.ID)",
                sqlQuery.output());
        Assert.assertEquals(0, sqlQuery.getParameters().size());

        // 拼装两张表关联查询,带二元查询条件
        sb.condition().operator("=").left("T1.CATAGORY").right(new Value("TEST")).end();
        sqlQuery = sb.build();
        Assert.assertEquals("SELECT T1.* FROM TABLE1 AS T1 INNER JOIN TABLE2 AS T2 ON (T2.ID = T1.ID) WHERE (T1.CATAGORY = ?)",
                sqlQuery.output());
        Assert.assertEquals(1, sqlQuery.getParameters().size());
        Assert.assertEquals("TEST", sqlQuery.getParameters().get(0));

        // 拼装两张表关联查询,带二元查询条件,增加排序
        sb.order().column("T1.CREATE_DATE").desc().end();
        sqlQuery = sb.build();

        Assert.assertEquals(
                "SELECT T1.* FROM TABLE1 AS T1 INNER JOIN TABLE2 AS T2 ON (T2.ID = T1.ID) WHERE (T1.CATAGORY = ?) ORDER BY T1.CREATE_DATE DESC",
                sqlQuery.output());
        Assert.assertEquals(1, sqlQuery.getParameters().size());
        Assert.assertEquals("TEST", sqlQuery.getParameters().get(0));

        // 去掉排序
        sb.removeOrder();
        sqlQuery = sb.build();
        Assert.assertEquals("SELECT T1.* FROM TABLE1 AS T1 INNER JOIN TABLE2 AS T2 ON (T2.ID = T1.ID) WHERE (T1.CATAGORY = ?)",
                sqlQuery.output());
        Assert.assertEquals(1, sqlQuery.getParameters().size());
        Assert.assertEquals("TEST", sqlQuery.getParameters().get(0));
        // MYSQL LIMIT查询
        sb.firstResults(100).maxResults(400);
        sqlQuery = sb.build();
        Assert.assertEquals(
                "SELECT T1.* FROM TABLE1 AS T1 INNER JOIN TABLE2 AS T2 ON (T2.ID = T1.ID) WHERE (T1.CATAGORY = ?) LIMIT ?,?",
                sqlQuery.output());
        Assert.assertEquals(3, sqlQuery.getParameters().size());
        Assert.assertEquals("TEST", sqlQuery.getParameters().get(0));
        Assert.assertEquals(100, sqlQuery.getParameters().get(1));
        Assert.assertEquals(400, sqlQuery.getParameters().get(2));

        // 移除LIMIT
        sb.removeFirstResult().removeMaxResults();

        // COUNT查询
        sb.isCount();
        sqlQuery = sb.build();
        Assert.assertEquals(
                "SELECT COUNT(*) COUNT FROM TABLE1 AS T1 INNER JOIN TABLE2 AS T2 ON (T2.ID = T1.ID) WHERE (T1.CATAGORY = ?)",
                sqlQuery.output());
        Assert.assertEquals(1, sqlQuery.getParameters().size());
        Assert.assertEquals("TEST", sqlQuery.getParameters().get(0));

        // 去掉COUNT
        sb.isCount(false);
        sqlQuery = sb.build();
        Assert.assertEquals("SELECT T1.* FROM TABLE1 AS T1 INNER JOIN TABLE2 AS T2 ON (T2.ID = T1.ID) WHERE (T1.CATAGORY = ?)",
                sqlQuery.output());
        Assert.assertEquals(1, sqlQuery.getParameters().size());
        Assert.assertEquals("TEST", sqlQuery.getParameters().get(0));

        // 多列查询
        sb.select().column("T2", "ID", "ID2").column("T2.CODE", "CODE2").end();
        sqlQuery = sb.build();
        Assert.assertEquals(
                "SELECT T1.*,T2.ID AS ID2,T2.CODE AS CODE2 FROM TABLE1 AS T1 INNER JOIN TABLE2 AS T2 ON (T2.ID = T1.ID) WHERE (T1.CATAGORY = ?)",
                sqlQuery.output());
        Assert.assertEquals(1, sqlQuery.getParameters().size());
        Assert.assertEquals("TEST", sqlQuery.getParameters().get(0));

        // IS NULL查询条件
        sb.isNullCondition().column(new Column("T1.CREATOR")).end();
        sqlQuery = sb.build();
        Assert.assertEquals(
                "SELECT T1.*,T2.ID AS ID2,T2.CODE AS CODE2 FROM TABLE1 AS T1 INNER JOIN TABLE2 AS T2 ON (T2.ID = T1.ID) WHERE (T1.CATAGORY = ?) AND (T1.CREATOR IS NULL)",
                sqlQuery.output());
        Assert.assertEquals(1, sqlQuery.getParameters().size());
        Assert.assertEquals("TEST", sqlQuery.getParameters().get(0));

        // IS NOT NULL查询条件
        sb.isNotNullCondition().column(new Column("T2.ENABLED")).end();
        sqlQuery = sb.build();
        Assert.assertEquals(
                "SELECT T1.*,T2.ID AS ID2,T2.CODE AS CODE2 FROM TABLE1 AS T1 INNER JOIN TABLE2 AS T2 ON (T2.ID = T1.ID) WHERE (T1.CATAGORY = ?) AND (T1.CREATOR IS NULL) AND (T2.ENABLED IS NOT NULL)",
                sqlQuery.output());
        Assert.assertEquals(1, sqlQuery.getParameters().size());
        Assert.assertEquals("TEST", sqlQuery.getParameters().get(0));

        // BETWEEN查询条件
        sb.betweenCondition().column("T1.START_DATE").leftValue(new Value(2015, Integer.class))
                .rightValue(new Value(2018, Integer.class)).end();
        sqlQuery = sb.build();
        Assert.assertEquals(
                "SELECT T1.*,T2.ID AS ID2,T2.CODE AS CODE2 FROM TABLE1 AS T1 INNER JOIN TABLE2 AS T2 ON (T2.ID = T1.ID) WHERE (T1.CATAGORY = ?) AND (T1.CREATOR IS NULL) AND (T2.ENABLED IS NOT NULL) AND (T1.START_DATE BETWEEN ? AND ?)",
                sqlQuery.output());
        Assert.assertEquals(3, sqlQuery.getParameters().size());
        Assert.assertEquals("TEST", sqlQuery.getParameters().get(0));
        Assert.assertEquals(2015, sqlQuery.getParameters().get(1));
        Assert.assertEquals(2018, sqlQuery.getParameters().get(2));

        // IN查询条件
        sb.inCondition().column("T2.CODE").value(new Value("1")).value(new Value("2")).value(new Value("3"))
                .value(new Value("4")).value(new Value("5")).value(new Value("6")).end();
        sqlQuery = sb.build();
        Assert.assertEquals(
                "SELECT T1.*,T2.ID AS ID2,T2.CODE AS CODE2 FROM TABLE1 AS T1 INNER JOIN TABLE2 AS T2 ON (T2.ID = T1.ID) WHERE (T1.CATAGORY = ?) AND (T1.CREATOR IS NULL) AND (T2.ENABLED IS NOT NULL) AND (T1.START_DATE BETWEEN ? AND ?) AND (T2.CODE IN(?,?,?,?,?,?))",
                sqlQuery.output());
        Assert.assertEquals(9, sqlQuery.getParameters().size());
        Assert.assertEquals("TEST", sqlQuery.getParameters().get(0));
        Assert.assertEquals(2015, sqlQuery.getParameters().get(1));
        Assert.assertEquals(2018, sqlQuery.getParameters().get(2));
        Assert.assertEquals("1", sqlQuery.getParameters().get(3));
        Assert.assertEquals("2", sqlQuery.getParameters().get(4));
        Assert.assertEquals("3", sqlQuery.getParameters().get(5));
        Assert.assertEquals("4", sqlQuery.getParameters().get(6));
        Assert.assertEquals("5", sqlQuery.getParameters().get(7));
        Assert.assertEquals("6", sqlQuery.getParameters().get(8));

        // 大于1000个值的IN语句
        List<IValue> values = new ArrayList<>();
        for (int i = 0; i < 2235; i++) {
            values.add(new Value("test" + i));
        }

        sb.inCondition().column(new Column("T1.NAME")).value(values).end();
        sqlQuery = sb.build();
        Assert.assertEquals(
                "SELECT T1.*,T2.ID AS ID2,T2.CODE AS CODE2 FROM TABLE1 AS T1 INNER JOIN TABLE2 AS T2 ON (T2.ID = T1.ID) WHERE (T1.CATAGORY = ?) AND (T1.CREATOR IS NULL) AND (T2.ENABLED IS NOT NULL) AND (T1.START_DATE BETWEEN ? AND ?) AND (T2.CODE IN(?,?,?,?,?,?)) AND (T1.NAME IN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) OR T1.NAME IN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) OR T1.NAME IN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?))",
                sqlQuery.output());
        Assert.assertEquals(2244, sqlQuery.getParameters().size());
        Assert.assertEquals("TEST", sqlQuery.getParameters().get(0));
        Assert.assertEquals(2015, sqlQuery.getParameters().get(1));
        Assert.assertEquals(2018, sqlQuery.getParameters().get(2));
        Assert.assertEquals("1", sqlQuery.getParameters().get(3));
        Assert.assertEquals("2", sqlQuery.getParameters().get(4));
        Assert.assertEquals("3", sqlQuery.getParameters().get(5));
        Assert.assertEquals("4", sqlQuery.getParameters().get(6));
        Assert.assertEquals("5", sqlQuery.getParameters().get(7));
        Assert.assertEquals("6", sqlQuery.getParameters().get(8));

        for (int i = 0; i < 2235; i++) {
            Assert.assertEquals("test" + i, sqlQuery.getParameters().get(i + 9));
        }
    }
}
