package site.autzone.sqlbee.builder;

import java.util.*;

import site.autzone.configurer.AbstractConfigurerBuilder;
import site.autzone.configurer.Configurer;
import site.autzone.sqlbee.*;
import site.autzone.sqlbee.condition.AbstractCondition;
import site.autzone.sqlbee.condition.Condition;
import site.autzone.sqlbee.column.InsertColumn;
import site.autzone.sqlbee.sql.*;
import site.autzone.sqlbee.column.UpdateColumn;
import site.autzone.sqlbee.configurer.*;

public class SqlBuilder extends AbstractConfigurerBuilder<Sql> {
  private List<ITable> tables = new ArrayList<>();
  // 占位符对应的value
  private Map<String, IValue> parameters = new LinkedHashMap<String, IValue>();
  // 所有的value，占位符的下标对应数组下标
  private List<Object> values = new ArrayList<>();

  private List<IColumn> columns = new ArrayList<>();
  /**
   * 统计字段
   */
  private List<IColumn> countColumns = new ArrayList<>();
  private List<ICondition> conditions = new ArrayList<>();

  private List<InsertColumn> insertColumns = new ArrayList<>();
  private List<Condition> updateColumns = new ArrayList<>();
  // union
  private List<Union> unions = new ArrayList<>();
  // join
  private List<IJoin> IJoins = new ArrayList<>();

  //main table configurer
  private Configurer sqlTableConfigurer = null;

  // 数据集限制
  private IValue firstResults;
  private IValue maxResults;
  private SqlBuilderType type; // 0 query;1 insert;2 update;3 delete;
  private Order order;
  private Group group;
  private Having having;

  private boolean isSub = false;
  private boolean isCount = false;

  private SqlBuilder() {}

  private SqlBuilder(boolean isSub) {
    this.isSub = isSub;
  }

  public SqlBuilderType getType() {
    return type;
  }

  public static SqlConfigurer createQuery() {
    return new SqlBuilder().initMainTable(SqlBuilderType.QUERY);
  }

  public static SqlConfigurer createInsert() {
    return new SqlBuilder().initMainTable(SqlBuilderType.INSERT);
  }

  public static SqlConfigurer createUpdate() {
    return new SqlBuilder().initMainTable(SqlBuilderType.UPDATE);
  }

  public static SqlConfigurer createDelete() {
    return new SqlBuilder().initMainTable(SqlBuilderType.DELETE);
  }

  public static SqlConfigurer createSubQuery() {
    return new SqlBuilder(true).initMainTable(SqlBuilderType.QUERY);
  }

  public void addInsertColumns(List<InsertColumn> insertColumns) {
    this.insertColumns.addAll(insertColumns);
  }

  public void addUpdateColumns(List<UpdateColumn> updateColumns) {
    this.updateColumns.addAll(updateColumns);
  }

  public void setFirstResults(IValue firstResults) {
    this.firstResults = firstResults;
  }

  public void setMaxResults(IValue maxResults) {
    this.maxResults = maxResults;
  }

  public List<Union> getUnions() {
    return unions;
  }

  public void addUnion(Union union) {
    this.unions.add(union);
  }

  public UnionConfigurer union(ISql subSql) {
    UnionConfigurer unionConfigurer = new UnionConfigurer();
    unionConfigurer.init(this);
    this.add((Configurer)unionConfigurer);
    unionConfigurer.sql(subSql);
    return unionConfigurer;
  }

  public UnionConfigurer unionAll(ISql subSql) {
    UnionConfigurer unionConfigurer = new UnionConfigurer();
    unionConfigurer.init(this);
    this.add((Configurer)unionConfigurer);
    unionConfigurer.all();
    unionConfigurer.sql(subSql);
    return unionConfigurer;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public InsertColumnConfigurer insert() {
    Configurer insertColumnConfigurer = new InsertColumnConfigurer();
    insertColumnConfigurer.init(this);
    this.add(insertColumnConfigurer);
    return (InsertColumnConfigurer) insertColumnConfigurer;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public UpdateColumnConfigurer update() {
    Configurer updateColumnConfigurer = new UpdateColumnConfigurer();
    updateColumnConfigurer.init(this);
    this.add(updateColumnConfigurer);
    return (UpdateColumnConfigurer) updateColumnConfigurer;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public SqlConfigurer table() {
    if(sqlTableConfigurer == null) {
      this.sqlTableConfigurer = new SqlConfigurer();
      sqlTableConfigurer.init(this);
      this.add(sqlTableConfigurer);
    }
    return (SqlConfigurer) sqlTableConfigurer;
  }

  public synchronized SqlConfigurer initMainTable(SqlBuilderType sqlBuilderType) {
    type = sqlBuilderType;
    sqlTableConfigurer = null;
    return this.table();
  }

  public void addAllColumn(List<IColumn> columns) {
    this.columns.addAll(columns);
  }

  /**
   * 添加统计字段
   * @param columns
   */
  public void addAllCountColumn(List<IColumn> columns) {
    this.countColumns.addAll(columns);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public ColumnConfigurer select() {
    Configurer columnConfigurer = new ColumnConfigurer();
    columnConfigurer.init(this);
    this.add(columnConfigurer);
    return (ColumnConfigurer) columnConfigurer;
  }

  public void addCondition(AbstractCondition condition) {
    this.conditions.add(condition);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public ConditionConfigurer condition() {
    Configurer binaryConditionConfigurer = new ConditionConfigurer();
    binaryConditionConfigurer.init(this);
    this.add(binaryConditionConfigurer);
    return (ConditionConfigurer) binaryConditionConfigurer;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public ConditionConfigurer condition(String operator) {
    Configurer binaryConditionConfigurer = new ConditionConfigurer(operator);
    binaryConditionConfigurer.init(this);
    this.add(binaryConditionConfigurer);
    return (ConditionConfigurer) binaryConditionConfigurer;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public ExistsConfigurer exists() {
    Configurer existsConfigurer = new ExistsConfigurer();
    existsConfigurer.init(this);
    this.add(existsConfigurer);
    return (ExistsConfigurer) existsConfigurer;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public NotExistsConfigurer notExists() {
    Configurer notExistsConfigurer = new NotExistsConfigurer();
    notExistsConfigurer.init(this);
    this.add(notExistsConfigurer);
    return (NotExistsConfigurer) notExistsConfigurer;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public BetweenConfigurer betweenCondition() {
    Configurer betweenConditionConfigurer = new BetweenConfigurer();
    betweenConditionConfigurer.init(this);
    this.add(betweenConditionConfigurer);
    return (BetweenConfigurer) betweenConditionConfigurer;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public NotBetweenConfigurer notBetweenCondition() {
    Configurer betweenConditionConfigurer = new NotBetweenConfigurer();
    betweenConditionConfigurer.init(this);
    this.add(betweenConditionConfigurer);
    return (NotBetweenConfigurer) betweenConditionConfigurer;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public MulConditionConfigurer mulCondition(String operator) {
    Configurer mulConditionConfigurer = new MulConditionConfigurer(operator);
    mulConditionConfigurer.init(this);
    this.add(mulConditionConfigurer);
    return (MulConditionConfigurer) mulConditionConfigurer;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public InConfigurer inCondition() {
    Configurer inConditionConfigurer = new InConfigurer();
    inConditionConfigurer.init(this);
    this.add(inConditionConfigurer);
    return (InConfigurer) inConditionConfigurer;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public IsNullConfigurer isNullCondition() {
    Configurer isNullConditionConfigurer = new IsNullConfigurer();
    isNullConditionConfigurer.init(this);
    this.add(isNullConditionConfigurer);
    return (IsNullConfigurer) isNullConditionConfigurer;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public IsNotNullConfigurer isNotNullCondition() {
    Configurer isNotNullConditionConfigurer = new IsNotNullConfigurer();
    isNotNullConditionConfigurer.init(this);
    this.add(isNotNullConditionConfigurer);
    return (IsNotNullConfigurer) isNotNullConditionConfigurer;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public SqlBuilder firstResults(Integer firstResults) {
    Configurer firstResultConfigurer = new FirstResultsConfigurer();
    FirstResultsConfigurer first = (FirstResultsConfigurer) firstResultConfigurer;
    first.firstResult(firstResults);
    firstResultConfigurer.init(this);
    this.add(firstResultConfigurer);
    return this;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public SqlBuilder maxResults(Integer maxResults) {
    Configurer maxResultsConfigurer = new MaxResultsConfigurer();
    MaxResultsConfigurer max = (MaxResultsConfigurer) maxResultsConfigurer;
    max.maxResults(maxResults);
    maxResultsConfigurer.init(this);
    this.add(maxResultsConfigurer);
    return this;
  }

  public void order(Order order) {
    this.order = order;
  }

  public void group(Group group) {
    this.group = group;
  }
  public void having(Having having) {
    this.having = having;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public OrderConfigurer order() {
    Configurer orderByConfigurer = new OrderConfigurer();
    orderByConfigurer.init(this);
    this.add(orderByConfigurer);
    return (OrderConfigurer) orderByConfigurer;
  }

  public GroupByConfigurer groupBy() {
    Configurer groupByConfigurer = new GroupByConfigurer();
    groupByConfigurer.init(this);
    this.add(groupByConfigurer);
    return (GroupByConfigurer) groupByConfigurer;
  }

  public SqlBuilder isCount() {
    this.isCount = true;
    return this;
  }

  public SqlBuilder isCount(boolean isCount) {
    this.isCount = isCount;
    return this;
  }

  public SqlBuilder removeOrder() {
    this.configurers.remove(OrderConfigurer.class);
    this.order = null;
    return this;
  }

  public SqlBuilder removeMaxResults() {
    this.configurers.remove(MaxResultsConfigurer.class);
    this.getValues().remove(this.getParameters().get(this.maxResults));
    this.getParameters().remove(this.maxResults);
    this.maxResults = null;
    return this;
  }

  public SqlBuilder removeFirstResult() {
    this.configurers.remove(FirstResultsConfigurer.class);
    this.getValues().remove(this.getParameters().get(this.firstResults));
    this.getParameters().remove(this.firstResults);
    this.firstResults = null;
    return this;
  }

  /**
   * 获取sql对象
   */
  public Sql sql() {
    Sql goSql = super.build();
    goSql.output();
    return goSql;
  }

  @Override
  public Sql performBuild() {
    Sql sql = new Sql();
    sql.isSubSql(this.isSub);
    sql.tableAll(this.getTables());
    sql.columnAll(this.columns);
    sql.countColumnAll(this.countColumns);
    sql.conditionAll(this.conditions);
    sql.joinAll(this.IJoins);
    sql.addInsertColumnAll(this.insertColumns);
    sql.addUpdateColumnAll(this.updateColumns);
    if (!this.getParameters().isEmpty()) {
      sql.setParametersMap(this.getParameters());
      sql.setParameters(this.getValues());
    }

    sql.firstResults(this.firstResults);
    sql.maxResults(this.maxResults);
    sql.isCount(this.isCount);
    sql.order(this.order);
    sql.group(this.group);
    sql.having(this.having);
    sql.setType(type);
    sql.unions(this.unions);
    return sql;
  }

  @Override
  public void afterBuild() {
    this.getTables().clear();
    this.columns.clear();
    this.countColumns.clear();
    this.conditions.clear();
    this.insertColumns.clear();
    this.updateColumns.clear();
    this.IJoins.clear();
    this.getValues().clear();
    this.unions.clear();
  }

  public List<Condition> getUpdateColumns() {
    return updateColumns;
  }

  // 受到builder管理的value
  public IValue manageValue(IValue value) {
    if (value.getIdx() == null) {
      int idx = values.size();
      // 同一个value对应多个idx时values未变化，需要后移idx
      while (parameters.get("" + idx) != null) {
        idx++;
      }
      value.setIdx(idx);
      values.add(value);
      parameters.put("" + idx, value);
    } else {
      int idx = value.getIdx();
      parameters.put("" + idx, value);
    }
    return value;
  }

  public void manageValue(HasChildren hasChildren) {
    this.manageAllValue(hasChildren.getChildren());
  }

  public void manageAllValue(List<ITextAble> children) {
    for (ITextAble child : children) {
      if (child instanceof IValue) {
        this.manageValue((IValue) child);
      } else if (child instanceof HasChildren) {
        HasChildren hasChildren = (HasChildren) child;
        manageAllValue(hasChildren.getChildren());
      }else if(child instanceof Sql) {
        Sql sql = (Sql) child;
        manageAllSubValue(sql, new ArrayList<>(sql.getConditions()));
      }
    }
  }

  public void manageAllSubValue(Sql subSql, List<ITextAble> children) {
    for (ITextAble child : children) {
      if (child instanceof IValue) {
        IValue subValue = (IValue) child;
        subValue.setIdx(null);
        subValue = this.manageValue(subValue);
        subSql.valueMap().put(""+subValue.getIdx(), subValue);
      } else if (child instanceof AbstractCondition) {
        AbstractCondition condition = (AbstractCondition) child;
        manageAllSubValue(subSql, condition.getChildren());
      }else if(child instanceof Sql) {
        Sql sql = (Sql) child;
        manageAllSubValue(subSql, new ArrayList<>(sql.getConditions()));
      }
    }
  }

  // 移除被管理的value
  public void removeManagedValue(IValue value) {
    if (value.getIdx() != null) {
      values.set(value.getIdx(), null);
      parameters.remove("?" + value.getIdx());
    }
  }

  public void addAllTable(List<ITable> sqlTables) {
    tables.addAll(sqlTables);
  }

  public Map<String, IValue> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, IValue> parameters) {
    this.parameters = parameters;
  }

  public List<Object> getValues() {
    return values;
  }

  public void setValues(List<Object> values) {
    this.values = values;
  }

  public List<ITable> getTables() {
    return tables;
  }

  public void setTables(List<ITable> tables) {
    this.tables = tables;
  }

  public List<IJoin> getJoins() {
    return IJoins;
  }

  public void setJoins(List<IJoin> IJoins) {
    this.IJoins = IJoins;
  }

  public void addJoins(List<IJoin> IJoins) {
    this.IJoins.addAll(IJoins);
  }
}
