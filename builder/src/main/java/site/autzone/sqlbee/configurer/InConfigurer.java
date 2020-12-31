package site.autzone.sqlbee.configurer;

import java.util.List;

import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.condition.AbstractCondition;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.IValue;
import site.autzone.sqlbee.condition.In;

public class InConfigurer extends AbstractConditionConfigurer {
  public InConfigurer column(String column) {
    super.add(0, new Column(column));
    return this;
  }

  public InConfigurer column(IColumn column) {
    super.add(0, column);
    return this;
  }

  public InConfigurer value(IValue value) {
    if (super.conditions.get(0) == null) {
      super.add(0, null);
    }
    super.add(value);
    return this;
  }

  public InConfigurer value(List<IValue> values) {
    if (super.conditions.get(0) == null) {
      super.add(0, null);
    }
    for (IValue value : values) {
      super.add(value);
    }
    return this;
  }

  @Override
  public void doConditionConfigure(SqlBuilder parent) {
    AbstractCondition inCondition = new In();
    inCondition.addAll(super.conditions);
    parent.addCondition(inCondition);
  }
}
