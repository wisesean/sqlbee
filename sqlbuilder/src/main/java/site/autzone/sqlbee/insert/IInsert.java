package site.autzone.sqlbee.insert;

import site.autzone.sqlbee.model.IHasValues;
import site.autzone.sqlbee.sql.insert.InsertItem;

/**
 * Insert执行器
 * @author xiaowj
 *
 */
public interface IInsert extends IHasValues {
  void addInsertItem(InsertItem updateItem);

  String buildUpdateText();

  void build();
}
