package site.autzone.sqlbee.executor;

import java.sql.SQLException;
import javax.sql.DataSource;

import site.autzone.sqlbee.update.IUpdate;

/**
 * 更新执行器
 *
 * @author wisesean
 */
public interface UpdateExecute {
  /**
   * 设置数据源
   *
   * @param dataSource
   */
  void setDataSource(DataSource dataSource);
  /**
   * 根据更新构造器更新
   *
   * @param update 构造器
   * @return
   * @throws SQLException
   */
  int update(IUpdate update);
  /**
   * 根据更新语句和参数更新
   *
   * @param updateSql 更新语句
   * @param params 参数
   * @return
   * @throws SQLException
   */
  int update(String updateSql, Object... params);
}
