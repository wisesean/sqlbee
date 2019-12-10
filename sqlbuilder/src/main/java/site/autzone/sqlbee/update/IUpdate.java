package site.autzone.sqlbee.update;

import site.autzone.sqlbee.model.ICondition;
import site.autzone.sqlbee.model.IHasValues;
import site.autzone.sqlbee.model.IQueryObject;
import site.autzone.sqlbee.sql.update.UpdateItem;

public interface IUpdate extends IHasValues {
  void updateObject(IQueryObject updateObject);

  void addUpdateItem(UpdateItem updateItem);

  void addCondition(ICondition condition);

  String buildUpdateText();

  void build();
}
