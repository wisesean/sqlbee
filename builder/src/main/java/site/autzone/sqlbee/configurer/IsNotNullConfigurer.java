package site.autzone.sqlbee.configurer;

import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.condition.AbstractCondition;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.condition.IsNotNull;

public class IsNotNullConfigurer extends AbstractConditionConfigurer {
  public IsNotNullConfigurer column(String column) {
    super.add(0, new Column(column));
    return this;
  }

  public IsNotNullConfigurer column(IColumn column) {
    super.add(0, column);
    return this;
  }

  @Override
  public void doConditionConfigure(SqlBuilder parent) {
    AbstractCondition isNotNullCondition = new IsNotNull();
    isNotNullCondition.addAll(super.conditions);
    parent.addCondition(isNotNullCondition);
  }
}
