package site.autzone.sqlbee.sql.query.builder;

import java.util.List;
import site.autzone.sqlbee.builder.AbstractConfigableConfigurer;
import site.autzone.sqlbee.model.AbstractCondition;
import site.autzone.sqlbee.model.BinaryCondition;
import site.autzone.sqlbee.model.Field;
import site.autzone.sqlbee.model.ITextable;
import site.autzone.sqlbee.model.IValue;
import site.autzone.sqlbee.sql.query.RightJoin;
import site.autzone.sqlbee.sql.query.SqlQueryObject;

public class RightJoinConfigurer extends AbstractConfigableConfigurer<SqlQueryBuilder> {
  private RightJoin rightJoin = new RightJoin();

  public RightJoinConfigurer() {}

  public RightJoinConfigurer(SqlQueryBuilder parent) {
    init(parent);
  }

  public RightJoinConfigurer join(String name) {
    join(name, null);
    return this;
  }

  public RightJoinConfigurer idx(int idx) {
    this.rightJoin.idx(idx);
    return this;
  }

  public RightJoinConfigurer join(int idx, String name, String alias) {
    this.rightJoin.idx(idx);
    this.rightJoin.join(new SqlQueryObject(name, alias));
    return this;
  }

  public RightJoinConfigurer join(String name, String alias) {
    this.rightJoin.join(new SqlQueryObject(name, alias));
    return this;
  }

  public RightJoinConfigurer addBinaryCondition(
      String leftField, String connector, IValue rightValue) {
    BinaryCondition binaryCondition = new BinaryCondition(connector);
    binaryCondition.setLeftField(new Field(leftField));
    binaryCondition.setRightField(rightValue);
    rightJoin.addCondition(binaryCondition);

    return this;
  }

  public RightJoinConfigurer addBinaryCondition(String leftField, IValue rightValue) {
    BinaryCondition binaryCondition = new BinaryCondition("=");
    binaryCondition.setLeftField(new Field(leftField));
    binaryCondition.setRightField(rightValue);
    rightJoin.addCondition(binaryCondition);

    return this;
  }

  public RightJoinConfigurer addBinaryCondition(
      String leftField, String connector, String rightField) {
    BinaryCondition binaryCondition = new BinaryCondition(connector);
    binaryCondition.setLeftField(new Field(leftField));
    binaryCondition.setRightField(new Field(rightField));
    rightJoin.addCondition(binaryCondition);

    return this;
  }

  public RightJoinConfigurer addBinaryCondition(String leftField, String rightField) {
    BinaryCondition binaryCondition = new BinaryCondition("=");
    binaryCondition.setLeftField(new Field(leftField));
    binaryCondition.setRightField(new Field(rightField));
    rightJoin.addCondition(binaryCondition);

    return this;
  }

  @Override
  public void doConfigure(SqlQueryBuilder parent) {
    if (parent.getQueryObjects().size() <= 0) {
      throw new RuntimeException("right join must have queryObject.");
    }
    parent.getQueryObjects().get(rightJoin.getIdx()).rightJoin(rightJoin);
    // 若条件是值的需要纳入管理
    manageAllValue(parent, this.rightJoin.getConditions());
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
