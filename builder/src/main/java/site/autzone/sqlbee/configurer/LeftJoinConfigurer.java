package site.autzone.sqlbee.configurer;

import site.autzone.configurer.AbstractConfigAbleConfigurer;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.condition.Condition;
import site.autzone.sqlbee.ITable;
import site.autzone.sqlbee.IValue;
import site.autzone.sqlbee.join.Left;

public class LeftJoinConfigurer extends AbstractConfigAbleConfigurer<SqlBuilder> {
  private Left leftJoin;

  public LeftJoinConfigurer() {}

  public LeftJoinConfigurer(SqlBuilder parent) {
    init(parent);
  }

  public LeftJoinConfigurer join(ITable mainTable, ITable joinTable) {
    this.leftJoin = new Left(mainTable, joinTable);
    return this;
  }

  public LeftJoinConfigurer addCondition(String leftField, String operator, IValue rightValue) {
    Condition condition = new Condition(operator);
    condition.setLeftField(new Column(leftField));
    condition.setRightField(rightValue);
    leftJoin.condition(condition);
    return this;
  }

  public LeftJoinConfigurer addCondition(String leftField, IValue rightValue) {
    Condition condition = new Condition("=");
    condition.setLeftField(new Column(leftField));
    condition.setRightField(rightValue);
    leftJoin.condition(condition);
    return this;
  }

  public LeftJoinConfigurer addCondition(String leftField, String operator, String rightField) {
    Condition condition = new Condition(operator);
    condition.setLeftField(new Column(leftField));
    condition.setRightField(new Column(rightField));
    leftJoin.condition(condition);
    return this;
  }

  public LeftJoinConfigurer addCondition(String leftField, String rightField) {
    Condition condition = new Condition("=");
    condition.setLeftField(new Column(leftField));
    condition.setRightField(new Column(rightField));
    leftJoin.condition(condition);
    return this;
  }

  @Override
  public void doConfigure(SqlBuilder parent) {
    // 若条件是值的需要纳入管理
    parent.manageAllValue(this.leftJoin.getConditions());
  }
}
