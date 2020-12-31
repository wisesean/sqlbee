package site.autzone.sqlbee.configurer;

import site.autzone.configurer.AbstractConfigAbleConfigurer;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.condition.Condition;
import site.autzone.sqlbee.ITable;
import site.autzone.sqlbee.IValue;
import site.autzone.sqlbee.join.Right;

public class RightJoinConfigurer extends AbstractConfigAbleConfigurer<SqlBuilder> {
  private Right rightJoin;

  public RightJoinConfigurer() {}

  public RightJoinConfigurer(SqlBuilder parent) {
    init(parent);
  }

  public RightJoinConfigurer join(ITable mainTable, ITable joinTable) {
    this.rightJoin = new Right(mainTable, joinTable);
    return this;
  }

  public RightJoinConfigurer addCondition(String leftField, String operator, IValue rightValue) {
    Condition condition = new Condition(operator);
    condition.setLeftField(new Column(leftField));
    condition.setRightField(rightValue);
    rightJoin.condition(condition);

    return this;
  }

  public RightJoinConfigurer addCondition(String leftField, IValue rightValue) {
    Condition condition = new Condition("=");
    condition.setLeftField(new Column(leftField));
    condition.setRightField(rightValue);
    rightJoin.condition(condition);

    return this;
  }

  public RightJoinConfigurer addCondition(String leftField, String operator, String rightField) {
    Condition condition = new Condition(operator);
    condition.setLeftField(new Column(leftField));
    condition.setRightField(new Column(rightField));
    rightJoin.condition(condition);

    return this;
  }

  public RightJoinConfigurer addCondition(String leftField, String rightField) {
    Condition condition = new Condition("=");
    condition.setLeftField(new Column(leftField));
    condition.setRightField(new Column(rightField));
    rightJoin.condition(condition);

    return this;
  }

  @Override
  public void doConfigure(SqlBuilder parent) {
    // 若条件是值的需要纳入管理
    parent.manageAllValue(this.rightJoin.conditions());
  }
}
