package site.autzone.sqlbee.sql.query.builder;

import java.util.List;

import site.autzone.sqlbee.model.AbstractCondition;
import site.autzone.sqlbee.model.Field;
import site.autzone.sqlbee.model.IField;
import site.autzone.sqlbee.model.IValue;
import site.autzone.sqlbee.model.InCondition;

public class InConditionConfigurer extends AbstractConditionConfigurer {
  public InConditionConfigurer field(String field) {
    super.add(0, new Field(field));
    return this;
  }

  public InConditionConfigurer field(IField field) {
    super.add(0, field);
    return this;
  }

  public InConditionConfigurer addValue(IValue value) {
    if (super.conditions.get(0) == null) {
      super.add(0, null);
    }
    super.add(value);
    return this;
  }

  public InConditionConfigurer addValue(List<IValue> values) {
    if (super.conditions.get(0) == null) {
      super.add(0, null);
    }
    for (IValue value : values) {
      super.add(value);
    }
    return this;
  }

  @Override
  public void doConditionConfigure(SqlQueryBuilder parent) {
    AbstractCondition inCondition = new InCondition();
    inCondition.addAll(super.conditions);
    parent.addCondition(inCondition);
  }
}
