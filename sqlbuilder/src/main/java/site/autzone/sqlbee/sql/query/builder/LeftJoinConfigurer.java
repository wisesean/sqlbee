package site.autzone.sqlbee.sql.query.builder;

import java.util.List;
import site.autzone.sqlbee.builder.AbstractConfigableConfigurer;
import site.autzone.sqlbee.model.AbstractCondition;
import site.autzone.sqlbee.model.BinaryCondition;
import site.autzone.sqlbee.model.Field;
import site.autzone.sqlbee.model.ITextable;
import site.autzone.sqlbee.model.IValue;
import site.autzone.sqlbee.sql.query.LeftJoin;
import site.autzone.sqlbee.sql.query.SqlQueryObject;

public class LeftJoinConfigurer extends AbstractConfigableConfigurer<SqlQueryBuilder> {
  private LeftJoin leftJoin = new LeftJoin();

  public LeftJoinConfigurer() {}

  public LeftJoinConfigurer(SqlQueryBuilder parent) {
    init(parent);
  }

  public LeftJoinConfigurer join(String name) {
    join(name, null);
    return this;
  }

  public LeftJoinConfigurer idx(int idx) {
    this.leftJoin.idx(idx);
    return this;
  }

  public LeftJoinConfigurer join(int idx, String name, String alias) {
    this.leftJoin.idx(idx);
    this.leftJoin.join(new SqlQueryObject(name, alias));
    return this;
  }

  public LeftJoinConfigurer join(String name, String alias) {
    this.leftJoin.join(new SqlQueryObject(name, alias));
    return this;
  }

  public LeftJoinConfigurer addBinaryCondition(
      String leftField, String connector, IValue rightValue) {
    BinaryCondition binaryCondition = new BinaryCondition(connector);
    binaryCondition.setLeftField(new Field(leftField));
    binaryCondition.setRightField(rightValue);
    leftJoin.addCondition(binaryCondition);

    return this;
  }

  public LeftJoinConfigurer addBinaryCondition(String leftField, IValue rightValue) {
    BinaryCondition binaryCondition = new BinaryCondition("=");
    binaryCondition.setLeftField(new Field(leftField));
    binaryCondition.setRightField(rightValue);
    leftJoin.addCondition(binaryCondition);

    return this;
  }

  public LeftJoinConfigurer addBinaryCondition(
      String leftField, String connector, String rightField) {
    BinaryCondition binaryCondition = new BinaryCondition(connector);
    binaryCondition.setLeftField(new Field(leftField));
    binaryCondition.setRightField(new Field(rightField));
    leftJoin.addCondition(binaryCondition);

    return this;
  }

  public LeftJoinConfigurer addBinaryCondition(String leftField, String rightField) {
    BinaryCondition binaryCondition = new BinaryCondition("=");
    binaryCondition.setLeftField(new Field(leftField));
    binaryCondition.setRightField(new Field(rightField));
    leftJoin.addCondition(binaryCondition);

    return this;
  }

  @Override
  public void doConfigure(SqlQueryBuilder parent) {
    if (parent.getQueryObjects().size() <= 0) {
      throw new RuntimeException("left join must have queryObject.");
    }
    parent.getQueryObjects().get(leftJoin.getIdx()).leftJoin(leftJoin);
    // 若条件是值的需要纳入管理
    manageAllValue(parent, this.leftJoin.getConditions());
  }

  private void manageAllValue(SqlQueryBuilder parent, List<ITextable> children) {
    for (ITextable child : children) {
      if (child instanceof IValue) {
        parent.manageValue((IValue) child);
      } else if (child instanceof AbstractCondition) {
        AbstractCondition condition = (AbstractCondition) child;
        manageAllValue(parent, condition.getChildren());
      }
    }
  }
}
