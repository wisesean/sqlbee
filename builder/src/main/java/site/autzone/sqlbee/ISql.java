package site.autzone.sqlbee;

import site.autzone.sqlbee.column.InsertColumn;
import site.autzone.sqlbee.condition.Condition;

import java.util.List;

/**
 * 查询构造器接口
 *
 * @author wisesean
 */
public interface ISql extends IHasValues {
  /**
   * 添加查询列
   *
   * @param column
   */
  void column(IColumn column);
  /**
   * 添加查询条件
   *
   * @param condition
   */
  void condition(ICondition condition);
  /**
   * 设置排序
   *
   * @param order
   */
  void order(ITextAble order);

  String buildGroupByPart();

  String buildHavingPart();

  String buildOrderByPart();
  /**
   * 设置是否count
   *
   * @param isCount
   */
  void isCount(boolean isCount);
  /**
   * 获取是否count
   *
   * @return
   */
  boolean isCount();
  /**
   * 设置起始行
   *
   * @param firstResults
   */
  void firstResults(Integer firstResults);
  /**
   * 设置最大行
   *
   * @param maxResults
   */
  void maxResults(Integer maxResults);
  /**
   * 获取构造器管理的所有Value
   *
   * @return
   */
  List<Object> getParameters();
  /**
   * 构造查询文本
   *
   * @return
   */
  String buildQueryText();
  /** 构造查询 */
  void buildQuery();

  /**
   * 构造查询文本
   *
   * @return
   */
  String buildInsertText();
  
  void addInsertColumnAll(List<InsertColumn> insertColumns);
  /** 构造查询 */
  void buildInsert();

  /**
   * 构造查询文本
   *
   * @return
   */
  String buildUpdateText();
  /** 构造查询 */
  void buildUpdate();
  void addUpdateColumnAll(List<Condition> updateColumns);
  /**
   * 构造查询文本
   *
   * @return
   */
  String buildDeleteText();
  /** 构造查询 */
  void buildDelete();
}
