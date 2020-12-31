package site.autzone.sqlbee.configurer;

import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.IValue;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.condition.AbstractCondition;
import site.autzone.sqlbee.condition.NotBetween;

public class NotBetweenConfigurer extends AbstractConditionConfigurer {
  public NotBetweenConfigurer column(String column) {
    super.add(0, new Column(column));
    return this;
  }

  public NotBetweenConfigurer column(IColumn column) {
    super.add(0, column);
    return this;
  }

  public NotBetweenConfigurer leftValue(IValue value) {
    super.add(1, value);
    return this;
  }

  public NotBetweenConfigurer rightValue(IValue value) {
    super.add(2, value);
    return this;
  }

  @Override
  public void doConditionConfigure(SqlBuilder parent) {
    AbstractCondition betweenCondition = new NotBetween();
    betweenCondition.addAll(conditions);
    parent.addCondition(betweenCondition);
  }
}
