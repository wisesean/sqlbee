package site.autzone.sqlbee.sql;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import site.autzone.sqlbee.sql.delete.DeleteTest;
import site.autzone.sqlbee.sql.insert.InsertTest;
import site.autzone.sqlbee.sql.query.QueryTest;
import site.autzone.sqlbee.sql.update.UpdateTest;

/**
 * 测试组,用于测试所有sql的构建
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({DeleteTest.class, InsertTest.class,
        QueryTest.class, UpdateTest.class})
public class ExecutorTestSuite {
}
