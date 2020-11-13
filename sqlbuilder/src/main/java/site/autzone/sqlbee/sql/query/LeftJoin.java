package site.autzone.sqlbee.sql.query;

import java.util.ArrayList;
import java.util.List;
import site.autzone.sqlbee.model.ICondition;
import site.autzone.sqlbee.model.ILeftJoin;
import site.autzone.sqlbee.model.IQueryObject;
import site.autzone.sqlbee.model.ITextable;

public class LeftJoin implements ILeftJoin {
  private int idx;
  private IQueryObject joinQueryObj;
  private List<ITextable> conditions = new ArrayList<>();

  @Override
  public String toText() {
    if (joinQueryObj == null || conditions.size() == 0) {
      return "";
    }
    StringBuffer sb = new StringBuffer(" LEFT JOIN ");
    sb.append(joinQueryObj.toText()).append(" ON ");
    sb.append(QueryUtils.joinTextableWithStr(conditions, " AND "));
    return sb.toString();
  }

  @Override
  public void addCondition(ICondition condition) {
    this.conditions.add(condition);
  }

  @Override
  public void join(IQueryObject joinQueryObj) {
    this.joinQueryObj = joinQueryObj;
  }

  @Override
  public void idx(int idx) {
    this.idx = idx;
  }

  public int getIdx() {
    return idx;
  }

  public void setIdx(int idx) {
    this.idx = idx;
  }

  public List<ITextable> getConditions() {
    return conditions;
  }

  public void setConditions(List<ITextable> conditions) {
    this.conditions = conditions;
  }
}
