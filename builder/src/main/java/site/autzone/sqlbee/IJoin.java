package site.autzone.sqlbee;

import java.util.List;

/**
 * 连接
 * @author xiaowj
 *
 */
public interface IJoin extends ITextAble {
  /**
   * 主表
   * @return
   */
  ITable mainTable();
  /**
   * 被连接的表
   * @param table
   */
  void join(ITable table);
  /**
   * 连接条件
   * @param condition
   */
  void condition(ICondition condition);
  
  /**
   * 所有条件
   * @return
   */
  List<ITextAble> conditions();
}
