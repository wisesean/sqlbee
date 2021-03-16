package site.autzone.sqlbee.sql;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import site.autzone.sqlbee.sql.delete.SqlDeleteBuilderTest;
import site.autzone.sqlbee.sql.insert.SqlInsertBuilderTest;
import site.autzone.sqlbee.sql.query.SqlQueryBuilderTest;
import site.autzone.sqlbee.sql.update.SqlUpdateBuilderTest;

/**
 * 测试组,用于测试所有sql的构建
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({SqlDeleteBuilderTest.class, SqlInsertBuilderTest.class,
        SqlQueryBuilderTest.class, SqlUpdateBuilderTest.class})
public class SqlTestSuite {
}
