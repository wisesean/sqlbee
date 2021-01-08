package site.autzone.sqlbee.sql;

import org.apache.commons.lang3.StringUtils;
import site.autzone.sqlbee.ITable;
import site.autzone.sqlbee.sql.Sql;

/**
 * 表
 */
public class Table implements ITable {
  private Sql subSql;
  private String name;
  private String alias;
  private Integer uniqueInteger;

  public Table(Integer uniqueInteger, String name) {
    this(uniqueInteger, name, null);
  }

  public Table(String name, String alias) {
    this.name = name;
    this.alias = alias;
  }

  public Table(Integer uniqueInteger, String name, String alias) {
    this.name = name;
    this.alias = alias;
    this.uniqueInteger = uniqueInteger;
  }

  public Table(Integer uniqueInteger, Sql subSql) {
    this(uniqueInteger, subSql, null);
  }

  public Table(Integer uniqueInteger, Sql subSql, String alias) {
    this.subSql = subSql;
    this.alias = alias;
    this.uniqueInteger = uniqueInteger;
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
  public String output() {
    StringBuffer tableText = null;
    if (this.subSql == null) {
      tableText = new StringBuffer(this.name);
    } else {
      tableText = new StringBuffer("(").append(this.subSql.output()).append(")");
    }
    if (this.alias != null) {
      tableText.append(" AS ").append(this.uniqueName());
    }
    return tableText.toString();
  }

  @Override
  public void subSqlAsTable(Sql subSql) {
    this.subSql = subSql;
  }

  @Override
  public String uniqueName() {
    return (StringUtils.isBlank(this.alias) ? "TABLE_" + this.uniqueInteger : this.alias);
  }
}
