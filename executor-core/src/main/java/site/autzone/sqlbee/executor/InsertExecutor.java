package site.autzone.sqlbee.executor;

import site.autzone.sqlbee.ISql;

import java.sql.SQLException;

/**
 * 插入数据执行器
 *
 * @author wisesean
 */
public interface InsertExecutor {
  /**
   * 根据构建类插入数据
   *
   * @param sql
   * @return
   */
  int insert(ISql sql);

  /**
   * 根据构建器插入数据,返回插入成功后的对象
   * @param sql 构建器
   * @param t 返回的对象类型
   * @param <T>
   * @return
   */
  <T> T insert(ISql sql, Class<T> t);

  /**
   * 根据语句和参数插入数据，并返回插入成功后的数据对象
   *
   * @param sql 插入语句
   * @param t 返回的数据对象类型
   * @param params 语句参数
   * @return 插入成功的对象
   * @throws SQLException
   */
  <T> T insert(String sql, Class<T> t, Object... params);
  /**
   * 根据语句和参数插入数据
   *
   * @param sql 插入语句
   * @param params 语句参数
   * @return The number of rows updated.
   * @throws SQLException
   */
  int insert(String sql, Object... params);
}
