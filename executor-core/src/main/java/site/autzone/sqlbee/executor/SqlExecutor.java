package site.autzone.sqlbee.executor;

/**
 * sql执行器
 */
public interface SqlExecutor extends BatchExecutor, DeleteExecutor, InsertExecutor,
        QueryExecutor, RouteDs, UpdateExecutor {
    int execute(String sql, Object... params);
}
