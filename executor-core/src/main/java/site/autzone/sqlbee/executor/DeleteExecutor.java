package site.autzone.sqlbee.executor;

import site.autzone.sqlbee.ISql;

import java.sql.SQLException;

/**
 * 数据删除执行器
 *
 * @author wisesean
 */
public interface DeleteExecutor extends RouteDs {
  /**
   * 根据语句删除
   *
   * @param sql sql语句
   * @param params 参数
   * @return The number of rows updated.
   * @throws SQLException
   */
  int delete(String sql, Object... params);

  /**
   * 删除
   * @param sql 根据构建类删除
   * @return
   */
  int delete(ISql sql);
}
