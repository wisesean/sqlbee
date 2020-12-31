package site.autzone.sqlbee.configurer;

import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.condition.AbstractCondition;
import site.autzone.sqlbee.condition.Between;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.IValue;

public class BetweenConfigurer extends AbstractConditionConfigurer {
  public BetweenConfigurer column(String column) {
    super.add(0, new Column(column));
    return this;
  }

  public BetweenConfigurer column(IColumn column) {
    super.add(0, column);
    return this;
  }

  public BetweenConfigurer leftValue(IValue value) {
    super.add(1, value);
    return this;
  }

  public BetweenConfigurer rightValue(IValue value) {
    super.add(2, value);
    return this;
  }

  @Override
  public void doConditionConfigure(SqlBuilder parent) {
    AbstractCondition betweenCondition = new Between();
    betweenCondition.addAll(conditions);
    parent.addCondition(betweenCondition);
  }
}
