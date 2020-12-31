package site.autzone.sqlbee.configurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import site.autzone.configurer.AbstractConfigurer;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.IValue;
import site.autzone.sqlbee.column.InsertColumn;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.value.Value;

public class InsertColumnConfigurer extends AbstractConfigurer<SqlBuilder> {
  private List<InsertColumn> insertColumns = new ArrayList<>();

  public InsertColumnConfigurer insertWithValues(Map<String, IValue> data) {
    data.forEach((colName, value)-> {insertColumns.add(new InsertColumn(new Column(colName), value));});
    return this;
  }
  public InsertColumnConfigurer inserts(Map<String, String> data) {
    data.forEach((colName, value)-> {insertColumns.add(new InsertColumn(new Column(colName), new Value(value)));});
    return this;
  }

  public InsertColumnConfigurer insertColumn(IColumn column, IValue value) {
    insertColumns.add(new InsertColumn(column, value));
    return this;
  }

  public InsertColumnConfigurer insertColumn(String column, IValue value) {
    insertColumns.add(new InsertColumn(new Column(column), value));
    return this;
  }

  public InsertColumnConfigurer insertColumn(String column, String value) {
    insertColumns.add(new InsertColumn(new Column(column), new Value(value)));
    return this;
  }

  public InsertColumnConfigurer insertColumn(IColumn column) {
    insertColumns.add(new InsertColumn(column));
    return this;
  }
  
  public InsertColumnConfigurer column(String column) {
    insertColumns.add(new InsertColumn(new Column(column)));
    return this;
  }

  @Override
  public void configure(SqlBuilder parent) {
    parent.addInsertColumns(this.insertColumns);
    for (InsertColumn child : this.insertColumns) {
      parent.manageValue(child.getValue());
    }
  }
}
