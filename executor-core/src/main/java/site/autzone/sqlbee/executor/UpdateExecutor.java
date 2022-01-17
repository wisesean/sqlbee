package site.autzone.sqlbee.executor;

import site.autzone.sqlbee.ISql;

import java.sql.SQLException;
import java.util.concurrent.Future;

/**
 * 更新执行器
 *
 * @author wisesean
 */
public interface UpdateExecutor {
  /**
   * 根据更新构造器更新
   *
   * @param sql 构造器
   * @return
   * @throws SQLException
   */
  int update(ISql sql);
  /**
   * 根据更新语句和参数更新
   *
   * @param updateSql 更新语句
   * @param params 参数
   * @return
   * @throws SQLException
   */
  int update(String updateSql, Object... params);
  
  /**
   * 批量更新
   * @param updateSql 更新语句
   * @param params 参数二维数组
   * @return
   */
  int[] batch(String updateSql, Object[][] params);
  
  /**
   * 异步更新
   * @param updateSql 更新语句
   * @param params 参数
   * @return
   */
  Future<Integer> updateAsync(String updateSql, Object... params);
  
  /**
   * 异步批量更新
   * @param updateSql 更新语句
   * @param params 参数
   * @return
   */
  Future<int[]> batchAsync(String updateSql, Object[][] params);
}
