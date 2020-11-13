package site.autzone.sqlbee.sql.query;

import java.util.ArrayList;
import java.util.List;
import site.autzone.sqlbee.model.ILeftJoin;
import site.autzone.sqlbee.model.IQueryObject;
import site.autzone.sqlbee.model.IRightJoin;

public class SqlQueryObject implements IQueryObject {
  private String name;
  private String alias;
  private List<ILeftJoin> leftJoins = new ArrayList<>();
  private List<IRightJoin> rightJoins = new ArrayList<>();

  public SqlQueryObject() {}

  public SqlQueryObject(String name) {
    this.name = name;
  }

  public SqlQueryObject(String name, String alias) {
    this.name = name;
    this.alias = alias;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  @Override
  public String toText() {
    StringBuffer queryObjectText = new StringBuffer(this.name);
    if (this.alias != null) {
      queryObjectText.append(" ").append(this.alias);
    }
    if(this.leftJoins.size() > 0) {
      this.leftJoins.forEach(leftJoin -> {
        queryObjectText.append(leftJoin.toText());
      });
    }
    if(this.rightJoins.size() > 0) {
      this.rightJoins.forEach(rightJoin -> {
        queryObjectText.append(rightJoin.toText());
      });
    }
    return queryObjectText.toString();
  }

  @Override
  public void leftJoin(ILeftJoin leftJoin) {
    this.leftJoins.add(leftJoin);
  }

  @Override
  public void rightJoin(IRightJoin rightJoin) {
    this.rightJoins.add(rightJoin);
  }
}
