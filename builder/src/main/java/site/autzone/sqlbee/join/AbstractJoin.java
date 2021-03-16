package site.autzone.sqlbee.join;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.Validate;
import site.autzone.sqlbee.ICondition;
import site.autzone.sqlbee.IJoin;
import site.autzone.sqlbee.ITable;
import site.autzone.sqlbee.ITextAble;
import site.autzone.sqlbee.injection.SqlCheck;
import site.autzone.sqlbee.sql.TextAbleJoin;

/**
 * 连接抽象类 inner join/left join/right join
 *
 * @author xiaowj
 */
public abstract class AbstractJoin implements IJoin {
  private String joinName;
  private ITable mainTable;
  private ITable joinTable;
  private List<ITextAble> conditions = new ArrayList<>();

  public AbstractJoin(String joinName, ITable mainTable, ITable joinTable) {
    this.joinName = joinName;
    this.mainTable = mainTable;
    this.joinTable = joinTable;
  }

  @Override
  public ITable mainTable() {
    return this.mainTable;
  }

  @Override
  public void join(ITable table) {
    this.joinTable = table;
  }

  @Override
  public void condition(ICondition condition) {
    this.conditions.add(condition);
  }

  @Override
  public String output() {
    Validate.notBlank(this.joinName);
    Validate.notNull(this.mainTable);
    Validate.notNull(this.joinTable);
    StringBuffer sb = new StringBuffer(" ");
    sb.append(this.joinName).append(" ");
    sb.append(joinTable.output()).append(" ON ");
    sb.append(TextAbleJoin.joinWithSkip(SqlCheck.MODE.STRICT, conditions, " AND "));
    return sb.toString();
  }
  
  @Override
  public List<ITextAble> conditions() {
    return this.conditions;
  }

  public String getJoinName() {
    return joinName;
  }

  public void setJoinName(String joinName) {
    this.joinName = joinName;
  }

  public ITable getMainTable() {
    return mainTable;
  }

  public void setMainTable(ITable mainTable) {
    this.mainTable = mainTable;
  }

  public ITable getJoinTable() {
    return joinTable;
  }

  public void setJoinTable(ITable joinTable) {
    this.joinTable = joinTable;
  }

  public List<ITextAble> getConditions() {
    return conditions;
  }

  public void setConditions(List<ITextAble> conditions) {
    this.conditions = conditions;
  }
}
