package site.autzone.sqlbee.configurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import site.autzone.configurer.AbstractConfigurer;
import site.autzone.configurer.Configurer;
import site.autzone.sqlbee.*;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.column.InsertColumn;
import site.autzone.sqlbee.column.UpdateColumn;
import site.autzone.sqlbee.condition.Condition;
import site.autzone.sqlbee.join.Inner;
import site.autzone.sqlbee.join.Left;
import site.autzone.sqlbee.join.Right;
import site.autzone.sqlbee.sql.*;
import site.autzone.sqlbee.value.Value;

/**
 * SQL配置器
 * createQuery()
 * @author xiaowj
 */
public class SqlConfigurer extends AbstractConfigurer<SqlBuilder> {
  // table
  private List<ITable> tables = new ArrayList<>();

  // join
  private List<IJoin> IJoins = new ArrayList<>();

  // columns query/delete
  private List<IColumn> columns = new ArrayList<>();

  // columns update
  private List<UpdateColumn> updateColumns = new ArrayList<>();

  // columns insert
  private List<InsertColumn> insertColumns = new ArrayList<>();

  // condition

  private Order order = new Order();
  private Group group = new Group();
  private Having having = new Having();

  // start configurer table
  /**
   * 根据table name配置主表
   *
   * @param name
   * @return
   */
  public SqlConfigurer table(String name) {
    table(name, null);
    return this;
  }

  /**
   * 根据表明和别名配置主表
   *
   * @param name
   * @param alias
   * @return
   */
  public SqlConfigurer table(String name, String alias) {
    Validate.isTrue(tables.size() == 0);
    tables.add(new Table(tables.size(), name, alias));
    return this;
  }

  /**
   * 根据子查询配置主表
   *
   * @param subSql
   * @return
   */
  public SqlConfigurer table(Sql subSql) {
    this.table(subSql, null);
    return this;
  }

  /**
   * 根据子查询和别名配置主表
   *
   * @param subSql
   * @param alias
   * @return
   */
  public SqlConfigurer table(Sql subSql, String alias) {
    Validate.isTrue(tables.size() == 0);
    tables.add(new Table(tables.size(), subSql, alias));
    return this;
  }

  // end configurer table.

  // start configurer join

  /**
   * 连表查询 inner join
   *
   * @param mainTable 主表
   * @param joinTable 被连接的表
   * @return
   */
  public SqlConfigurer innerJoin(ITable mainTable, ITable joinTable) {
    Validate.isTrue(this.tables
            .stream()
            .filter(table -> mainTable.uniqueName().equals(table.uniqueName()))
            .findAny()
            .isPresent());
    IJoins.add(new Inner(mainTable, joinTable));
    this.tables.add(joinTable);
    return this;
  }

  public SqlConfigurer innerJoinWithAlias(String mainTableAlias, ITable joinTable) {
    Optional<ITable> mainTable =
        this.tables.stream().filter(table -> mainTableAlias.equals(table.uniqueName())).findAny();
    Validate.isTrue(mainTable.isPresent());
    innerJoin(mainTable.get(), joinTable);
    return this;
  }

  public SqlConfigurer innerJoin(ITable joinTable) {
    Validate.noNullElements(this.tables);
    innerJoin(this.tables.get(0), joinTable);
    return this;
  }

  /**
   * 连表查询 left join
   *
   * @param mainTable 主表
   * @param joinTable 被连接的表
   * @return
   */
  public SqlConfigurer leftJoin(ITable mainTable, ITable joinTable) {
    IJoins.add(new Left(mainTable, joinTable));
    return this;
  }

  public SqlConfigurer leftJoinWithAlias(String mainTableAlias, ITable joinTable) {
    Optional<ITable> mainTable =
        this.tables.stream().filter(table -> mainTableAlias.equals(table.uniqueName())).findAny();
    Validate.isTrue(mainTable.isPresent());
    leftJoin(mainTable.get(), joinTable);
    return this;
  }

  public SqlConfigurer leftJoin(ITable joinTable) {
    Validate.noNullElements(this.tables);
    leftJoin(this.tables.get(0), joinTable);
    return this;
  }

  /**
   * 连表查询 right join
   *
   * @param mainTable 主表
   * @param joinTable 被连接的表
   * @return
   */
  public SqlConfigurer rightJoin(ITable mainTable, ITable joinTable) {
    IJoins.add(new Right(mainTable, joinTable));
    return this;
  }

  public SqlConfigurer rightJoinWithAlias(String mainTableAlias, ITable joinTable) {
    Optional<ITable> mainTable =
        this.tables.stream().filter(table -> mainTableAlias.equals(table.uniqueName())).findAny();
    Validate.isTrue(mainTable.isPresent());
    rightJoin(mainTable.get(), joinTable);
    return this;
  }

  public SqlConfigurer rightJoin(ITable joinTable) {
    Validate.noNullElements(this.tables);
    rightJoin(this.tables.get(0), joinTable);
    return this;
  }

  /**
   * join 的条件 校验是否已经配置了表和连接
   *
   * @param column 列
   * @param operator 操作符
   * @param rightValue 值
   * @return
   */
  public SqlConfigurer joinCondition(IColumn column, String operator, ITextAble rightValue) {
    Validate.noNullElements(this.tables);
    Validate.noNullElements(this.IJoins);
    Condition condition = new Condition(operator);
    condition.setLeftField(column);
    condition.setRightField(rightValue);
    this.IJoins.stream().reduce((f, s) -> s).get().condition(condition);
    return this;
  }

  public SqlConfigurer joinCondition(IColumn column, String operator, String rightValue) {
    Validate.noNullElements(this.tables);
    Validate.noNullElements(this.IJoins);
    Condition condition = new Condition(operator);
    condition.setLeftField(column);
    condition.setRightField(new Column(rightValue));
    this.IJoins.stream().reduce((f, s) -> s).get().condition(condition);
    return this;
  }

  /**
   * join 的条件
   *
   * @param column
   * @param operator
   * @param rightValue
   * @return
   */
  public SqlConfigurer joinCondition(String column, String operator, ITextAble rightValue) {
    this.joinCondition(new Column(column), operator, rightValue);
    return this;
  }

  public SqlConfigurer joinCondition(String column, String operator, String rightValue) {
    this.joinCondition(new Column(column), operator, rightValue);
    return this;
  }
  
  public SqlConfigurer joinCondition(String column, ITextAble rightValue) {
    this.joinCondition(new Column(column), "=", rightValue);
    return this;
  }
  
  public SqlConfigurer joinCondition(String column, String rightValue) {
    this.joinCondition(new Column(column), "=", rightValue);
    return this;
  }

  // end configurer join.

  // start configurer column
  public SqlConfigurer column(IColumn column, IValue value) {
    switch (this.getParent().getType()) {
      case INSERT:
        this.insertColumns.add(new InsertColumn(column, value));
        break;
      case UPDATE:
        this.updateColumns.add(new UpdateColumn(column, value));
        break;
      default:
        this.columns.add(column);
        break;
    }
    return this;
  }

  /**
   * 更新，插入，删除的字段
   * @param data
   * @return
   */
  public SqlConfigurer columns(Map<String, IValue> data) {
    data.forEach((colName, value)-> {this.column(new Column(colName), value);});
    return this;
  }

  /**
   * 更新，插入，删除的字段，值是字符串类型
   * @param data
   * @return
   */
  public SqlConfigurer strColumns(Map<String, String> data) {
    data.forEach((colName, value)-> {this.column(new Column(colName), new Value(value));});
    return this;
  }

  /**
   * 更新，插入，删除的字段
   * @param column
   * @param value
   * @return
   */
  public SqlConfigurer column(String column, IValue value) {
    this.column(new Column(column), value);
    return this;
  }

  public SqlConfigurer column(String column, String value) {
    this.column(new Column(column), new Value(value));
    return this;
  }

  public SqlConfigurer column(IColumn column, String value) {
    this.column(column, new Value(value));
    return this;
  }

  public SqlConfigurer column(IColumn column) {
    this.column(column, "null");
    return this;
  }

  /**
   * 查询的字段
   * @param column
   * @return
   */
  public SqlConfigurer column(String column) {
    this.column(new Column(column), "null");
    return this;
  }

  /**
   * 带别名的查询字段
   * @param name
   * @param alias
   * @return
   */
  public SqlConfigurer aliasColumn(String name, String alias) {
    columns.add(new Column(null, name, alias));
    return this;
  }

  /**
   * 带前缀和别名的查询字段
   * @param prefix
   * @param name
   * @param alias
   * @return
   */
  public SqlConfigurer aliasColumn(String prefix, String name, String alias) {
    columns.add(new Column(prefix, name, alias));
    return this;
  }

  // end column configurer.

  // start order configurer
  public SqlConfigurer asc() {
    this.order.order("ASC");
    return this;
  }

  public SqlConfigurer desc() {
    this.order.order("DESC");
    return this;
  }
  public SqlConfigurer order(String column) {
    this.order.add(new Column(column));
    return this;
  }

  public SqlConfigurer order(String... columns) {
    for (String column : columns) {
      this.order(column);
    }
    return this;
  }

  public SqlConfigurer order(Column column) {
    this.order.add(column);
    return this;
  }

  public SqlConfigurer order(List<IColumn> columns) {
    this.order.addAll(columns);
    return this;
  }
  // end order configurer.

  // start group configurer
  public SqlConfigurer groupBy(String column) {
    this.group.add(new Column(column));
    return this;
  }

  public SqlConfigurer groupBy(String... columns) {
    for (String column : columns) {
      this.groupBy(column);
    }
    return this;
  }

  public SqlConfigurer groupBy(Column column) {
    this.group.add(column);
    return this;
  }

  public SqlConfigurer groupBy(List<IColumn> columns) {
    this.group.addAll(columns);
    return this;
  }
  // end group configurer.

  // start having configurer

  public SqlConfigurer having(ICondition condition) {
    this.having.add(condition);
    return this;
  }

  public SqlConfigurer having(List<ICondition> conditions) {
    this.having.addAll(conditions);
    return this;
  }

  // end having configurer.

  // start firstResults configurer
  public SqlConfigurer firstResults(Integer firstResults) {
    Configurer firstResultConfigurer = new FirstResultsConfigurer();
    FirstResultsConfigurer first = (FirstResultsConfigurer) firstResultConfigurer;
    first.firstResult(firstResults);
    firstResultConfigurer.init(this);
    this.getParent().add(firstResultConfigurer);
    return this;
  }

  public SqlConfigurer maxResults(Integer maxResults) {
    Configurer maxResultsConfigurer = new MaxResultsConfigurer();
    MaxResultsConfigurer max = (MaxResultsConfigurer) maxResultsConfigurer;
    max.maxResults(maxResults);
    maxResultsConfigurer.init(this);
    this.getParent().add(maxResultsConfigurer);
    return this;
  }
  // end firstResults configurer.

  // start condition configurer
  @SuppressWarnings({"rawtypes", "unchecked"})
  public ConditionConfigurer condition() {
    Configurer binaryConditionConfigurer = new ConditionConfigurer();
    binaryConditionConfigurer.init(this.getParent());
    this.getParent().add(binaryConditionConfigurer);
    return (ConditionConfigurer) binaryConditionConfigurer;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public ConditionConfigurer condition(String operator) {
    Configurer binaryConditionConfigurer = new ConditionConfigurer(operator);
    binaryConditionConfigurer.init(this.getParent());
    this.getParent().add(binaryConditionConfigurer);
    return (ConditionConfigurer) binaryConditionConfigurer;
  }
  // end condition configurer.

  public Sql build() {
    return this.getParent().build();
  }

  /**
   * 构建Sql
   */
  public Sql sql() {
    Sql goSql = this.getParent().build();
    goSql.output();
    return goSql;
  }

  @Override
  public void configure(SqlBuilder parent) {
    parent.addAllTable(this.tables);
    parent.addAllColumn(this.columns);
    parent.addInsertColumns(this.insertColumns);
    parent.addUpdateColumns(this.updateColumns);
    parent.addJoins(this.IJoins);

    for (UpdateColumn child : this.updateColumns) {
      for (ITextAble itemField : child.getChildren()) {
        if (itemField instanceof IValue) {
          parent.manageValue((IValue) itemField);
        }
      }
    }

    for (InsertColumn child : this.insertColumns) {
      parent.manageValue(child.getValue());
    }

    for (IJoin IJoin : this.IJoins) {
      // 若条件是值的需要纳入管理
      parent.manageAllValue(IJoin.conditions());
    }

    if(this.order.columns() != null && this.order.columns().size() > 0) {
      parent.order(this.order);
    }

    if(this.group.columns() != null && this.group.columns().size() > 0) {
      parent.group(this.group);
    }

    if(this.having.conditions() != null && this.having.conditions().size() > 0) {
      parent.having(this.having);
      parent.manageAllValue(new ArrayList<>(this.having.conditions()));
    }
  }
}
