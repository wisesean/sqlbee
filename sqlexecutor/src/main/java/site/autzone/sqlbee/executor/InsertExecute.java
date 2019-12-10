package site.autzone.sqlbee.executor;

import java.sql.SQLException;
import javax.sql.DataSource;

import site.autzone.sqlbee.insert.IInsert;

/**
 * 插入数据执行器
 *
 * @author wisesean
 */
public interface InsertExecute {
  /**
   * 设置数据源
   *
   * @param dataSource
   */
  void setDataSource(DataSource dataSource);
  /**
   * 根据构建类插入数据
   *
   * @param insert
   * @return
   */
  int insert(IInsert insert);
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
