package site.autzone.sqlbee.sql;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.autzone.sqlbee.condition.Condition;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.ICondition;
import site.autzone.sqlbee.ISql;
import site.autzone.sqlbee.ITable;
import site.autzone.sqlbee.ITextAble;
import site.autzone.sqlbee.IValue;
import site.autzone.sqlbee.column.InsertColumn;
import site.autzone.sqlbee.IJoin;
import site.autzone.sqlbee.builder.SqlBuilderType;
import site.autzone.sqlbee.value.Value;

/**
 * 查询构建器抽象类
 *
 * @author xiaowj
 */
public class Sql implements ISql {
    final Logger LOG = LoggerFactory.getLogger("sql");
    private boolean isCount = false;
    // 数据集限制
    private IValue firstResults;
    private IValue maxResults;
    private SqlBuilderType type;
    //是否子查询
    private boolean isSub = false;

    private List<ITable> tables = new ArrayList<>();
    private List<IColumn> columns = new ArrayList<>();
    private List<ICondition> conditions = new ArrayList<>();
    private ITextAble order;
    private ITextAble group;
    private ITextAble having;

    private Map<String, IValue> parametersMap = new LinkedHashMap<String, IValue>();
    private String prepareSql;
    private List<Object> parameters = new ArrayList<>();
    private List<InsertColumn> insertColumns = new ArrayList<>();
    private List<Condition> updateColumns = new ArrayList<>();
    private List<IJoin> IJoins = new ArrayList<>();

    public SqlBuilderType getType() {
        return this.type;
    }

    public void setType(SqlBuilderType type) {
        this.type = type;
    }

    public Map valueMap() {
        return this.parametersMap;
    }

    public void isSubSql(boolean isSub) {
        this.isSub = isSub;
    }

    public boolean isSubSql() {
        return this.isSub;
    }

    public String buildColumns() {
        if (this.isCount()) {
            return "COUNT(*) COUNT";
        }

        if (this.getColumns().size() == 0) {
            return "*";
        }
        return TextAbleJoin.joinWithSkip(this.getColumns(), ",");
    }

    public String buildTables() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.tables.get(0).output());
        for (IJoin IJoin : this.IJoins) {
            sb.append(IJoin.output());
        }
        return sb.toString();
    }

    public String buildConditions() {
        String condition = TextAbleJoin.joinWithSkip(this.getConditions(), " AND ");
        if (condition.length() > 0) {
            return " WHERE " + condition;
        } else {
            return condition;
        }
    }

    @Override
    public String buildQueryText() {
        Validate.isTrue(this.tables.size() > 0);
        return new StringBuffer("SELECT ")
                .append(buildColumns())
                .append(" FROM ")
                .append(buildTables())
                .append(buildConditions())
                .append(buildGroupByPart())
                .append(buildHavingPart())
                .append(buildOrderByPart())
                .append(buildLimit())
                .toString();
    }

    private String buildLimit() {
        if(this.getMaxResults() != null && this.getFirstResults() != null) {
            return String.format(" LIMIT %s,%s", this.getFirstResults().output(), this.getMaxResults().output());
        }
        if(this.getMaxResults() != null) {
            //默认添加firstResults
            this.firstResults = new Value(0);
            this.getParameters().add(0);
            this.firstResults.setIdx(this.getParameters().size());
            this.valueMap().put(""+this.firstResults.getIdx(), this.firstResults);
            return String.format(" LIMIT %s, %s", this.getFirstResults().output(), this.getMaxResults().output());
        }
        return "";
    }

    @Override
    public void addUpdateColumnAll(List<Condition> updateItems) {
        updateColumns.addAll(updateItems);
    }

    @Override
    public void addInsertColumnAll(List<InsertColumn> insertColumns) {
        this.insertColumns.addAll(insertColumns);
    }

    @Override
    public String buildUpdateText() {
        Validate.isTrue(!this.getTables().isEmpty());
        Validate.isTrue(!this.isCount);
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE ").append(buildTables()).append(buildUpdateItems()).append(buildConditions());
        return sb.toString();
    }

    @Override
    public void buildUpdate() {
        String buildUpdateText = this.buildUpdateText();
        this.placeHolderVariables(buildUpdateText);
    }

    private void placeHolderVariables(String prepareSql) {
        this.parameters.clear();
        List<String> placeHolders = new ArrayList<>();
        int startIdx = prepareSql.indexOf("${");
        while (startIdx != -1) {
            int nextIdx = startIdx + "${".length();
            int endIdx = prepareSql.indexOf("}", startIdx);
            if (endIdx != -1) {
                String valuePlaceHolder = prepareSql.substring(startIdx + "${".length(), endIdx);
                placeHolders.add("${" + valuePlaceHolder + "}");
                IValue value = parametersMap.get(valuePlaceHolder);
                Validate.notNull(value);
                this.parameters.add(value.convert());
                nextIdx = endIdx + 1;
            }
            startIdx = prepareSql.indexOf("${", nextIdx);
        }
        if (!this.isSub) {
            for (String placeHolder : placeHolders) {
                prepareSql = prepareSql.replace(placeHolder, "?");
            }
        }
        this.prepareSql = prepareSql;
    }

    @Override
    public String buildDeleteText() {
        Validate.isTrue(!this.tables.isEmpty());
        Validate.isTrue(!this.isCount);
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE")
                .append(" FROM ")
                .append(buildTables())
                .append(buildConditions());
        return sb.toString();
    }

    @Override
    public void buildDelete() {
        String buildDeleteText = this.buildDeleteText();
        this.placeHolderVariables(buildDeleteText);
    }

    private Object buildUpdateItems() {
        Validate.isTrue(this.updateColumns.size() > 0);
        return " SET " + TextAbleJoin.joinWithSkip(this.updateColumns, ",");
    }

    public void isCount(boolean isCount) {
        this.isCount = isCount;
    }

    public boolean isCount() {
        return this.isCount;
    }

    public void tableAll(List<ITable> tables) {
        this.tables.addAll(tables);
    }

    public void column(IColumn column) {
        columns.add(column);
    }

    public void columnAll(List<IColumn> columns) {
        this.columns.addAll(columns);
    }


    public void condition(ICondition condition) {
        conditions.add(condition);
    }

    public void conditionAll(List<ICondition> conditions) {
        this.conditions.addAll(conditions);
    }

    public void join(IJoin IJoin) {
        this.IJoins.add(IJoin);
    }

    public void joinAll(List<IJoin> IJoins) {
        this.IJoins.addAll(IJoins);
    }

    public void order(ITextAble order) {
        this.order = order;
    }

    public void group(ITextAble group) {
        this.group = group;
    }

    public void having(ITextAble having) {
        this.having = having;
    }

    public List<ITable> getTables() {
        return tables;
    }

    public List<IColumn> getColumns() {
        return columns;
    }

    public List<ICondition> getConditions() {
        return conditions;
    }

    public ITextAble getOrderBy() {
        return order;
    }

    public String prepareSql() {
        return prepareSql;
    }

    protected void setCount(boolean isCount) {
        this.isCount = isCount;
    }

    protected void setTables(List<ITable> tables) {
        this.tables = tables;
    }

    protected void setColumns(List<IColumn> columns) {
        this.columns = columns;
    }

    protected void setConditions(List<ICondition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public String output() {
        switch (this.getType()) {
            case INSERT:
                this.buildInsert();
                break;
            case UPDATE:
                this.buildUpdate();
                break;
            case DELETE:
                this.buildDelete();
                break;
            default:
                this.buildQuery();
                break;
        }

        LOG.trace("==> Preparing: {};  ==>Parameters: {}", this.prepareSql, this.getParameters().stream()
                .map(v -> String.valueOf(v) + "(" + ((v==null)?"NULL":v.getClass().getSimpleName()) + ")")
                .collect(Collectors.joining(", ")));

        LOG.info("==> sql: {}", this.preparing2sql());
        return this.prepareSql;
    }

    private String preparing2sql() {
        String psql = this.prepareSql;
        for(Object v : this.getParameters()) {
            if(v == null || v instanceof Number || v instanceof Boolean) {
                psql = psql.replaceFirst("\\?", String.valueOf(v));
            }else {
                psql = psql.replaceFirst("\\?", "'" + String.valueOf(v) + "'");
            }
        }
        return psql;
    }

    @Override
    public void buildQuery() {
        String buildQueryText = this.buildQueryText();
        this.placeHolderVariables(buildQueryText);
    }

    @Override
    public String buildInsertText() {
        Validate.notNull(this.getTables().get(0));
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO ")
                .append(buildTables())
                .append(buildInsertItems())
                .append(buildInsertValues());
        return sb.toString();
    }

    private Object buildInsertValues() {
        Validate.isTrue(this.insertColumns.size() > 0);
        List<IValue> values = new ArrayList<>();
        for (InsertColumn insertItem : this.insertColumns) {
            values.add(insertItem.getValue());
        }
        return " VALUES(" + TextAbleJoin.joinWithSkip(values, ",") + ")";
    }

    private Object buildInsertItems() {
        Validate.isTrue(this.insertColumns.size() > 0);
        List<IColumn> items = new ArrayList<>();
        for (InsertColumn insertItem : this.insertColumns) {
            items.add(insertItem.getField());
        }
        return "(" + TextAbleJoin.joinWithSkip(items, ",") + ")";
    }

    @Override
    public void buildInsert() {
        String buildInsertText = this.buildInsertText();
        this.placeHolderVariables(buildInsertText);
    }

    @Override
    public String buildGroupByPart() {
        return this.group != null ? this.group.output() : "";
    }

    @Override
    public String buildHavingPart() {
        return this.having != null ? this.having.output() : "";
    }

    @Override
    public String buildOrderByPart() {
        return this.order != null ? this.order.output() : "";
    }

    @Override
    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public void setParametersMap(Map<String, IValue> parametersMap) {
        this.parametersMap = parametersMap;
    }

    public IValue getFirstResults() {
        return firstResults;
    }

    @Override
    public void firstResults(IValue firstResults) {
        this.firstResults = firstResults;
    }

    public IValue getMaxResults() {
        return maxResults;
    }

    @Override
    public void maxResults(IValue maxResults) {
        this.maxResults = maxResults;
    }
}
