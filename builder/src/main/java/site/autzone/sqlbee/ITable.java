package site.autzone.sqlbee;

import site.autzone.sqlbee.sql.Sql;

/**
 * sql table
 *
 * @author xiaowj
 */
public interface ITable extends ITextAble, IUnique {


  /**
   * 设置表名称
   *
   * @param name
   */
  void setName(String name);

  /**
   * 设置表别名
   *
   * @param alias
   */
  void setAlias(String alias);
  
  String getAlias();

  /**
   * 子查询作为表
   * @param subSql
   */
  void subSqlAsTable(Sql subSql);

  Integer getUniqueInteger();

  void setUniqueInteger(Integer unique);
}
