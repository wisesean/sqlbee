package site.autzone.sqlbee.executor;

import org.apache.commons.dbutils.AsyncQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import site.autzone.sqlbee.runner.ProcRunner;

/**
 * sql执行器
 */
public interface SqlExecutor extends BatchExecutor, DeleteExecutor, InsertExecutor,
        QueryExecutor, RouteDs, UpdateExecutor {
    QueryRunner getQueryRunner();
    AsyncQueryRunner getAsyncQueryRunner();
    ProcRunner getProcRunner();
    int execute(String sql, Object... params);
}
