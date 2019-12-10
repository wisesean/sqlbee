package site.autzone.sqlbee.executor;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import site.autzone.sqlbee.model.ITextable;

/**
 * 执行器 批量执行,插入,更新,删除语句,无参数返回的存过
 *
 * @author xiaowj
 */
public interface BatchExecute {
  /**
   * 设置数据源
   *
   * @param dataSource
   */
  void setDataSource(DataSource dataSource);
  /**
   * 根据构建器,批量执行
   *
   * @param batch
   * @return
   */
  int[] batch(ITextable batch, Object[][] data);

  /**
   * 根据sql语句,批量执行
   *
   * @param sql sql语句,只能为update, insert, delete语句
   * @param data 参数
   * @return
   */
  int[] batch(String sql, Object[][] data);

  /**
   * 调用无返回的存过
   *
   * @param sql
   * @param args
   * @return
   */
  int callProcedure(String sql, Object... args);

  /**
   * 调存过返回一条记录
   *
   * @param <T> 返回数据类型
   * @param sql
   * @param params
   * @return
   */
  <T> T queryProcBean(String sql, Class<T> t, Object... params);

  /**
   * 调存过返回多条数据
   *
   * @param <T>
   * @param sql
   * @param t
   * @param params
   * @return
   */
  <T> List<T> queryProcBeans(String sql, Class<T> t, Object... params);

  /**
   * 调存过返回多条数据
   *
   * @param sql 存过调用语句
   * @param 参数
   * @return List<Map<String, Object>>
   */
  List<Map<String, Object>> queryResults(String sql, Object... params);

  /**
   * 调存过返回一条记录
   *
   * @param sql 存过调用语句
   * @param 参数
   * @return Map<String, Object>
   */
  Map<String, Object> queryResult(String sql, Object... params);
}
