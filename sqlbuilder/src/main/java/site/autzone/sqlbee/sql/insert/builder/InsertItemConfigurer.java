package site.autzone.sqlbee.sql.insert.builder;

import java.util.ArrayList;
import java.util.List;

import site.autzone.sqlbee.builder.AbstractConfigurer;
import site.autzone.sqlbee.model.Field;
import site.autzone.sqlbee.model.IField;
import site.autzone.sqlbee.model.IValue;
import site.autzone.sqlbee.model.Value;
import site.autzone.sqlbee.sql.insert.InsertItem;

public class InsertItemConfigurer extends AbstractConfigurer<SqlInsertBuilder> {
  private List<InsertItem> isnertItems = new ArrayList<>();

  public InsertItemConfigurer item(IField field, IValue value) {
    isnertItems.add(new InsertItem(field, value));
    return this;
  }

  public InsertItemConfigurer item(String field, IValue value) {
    isnertItems.add(new InsertItem(new Field(field), value));
    return this;
  }

  public InsertItemConfigurer item(String field, String value) {
    isnertItems.add(new InsertItem(new Field(field), new Value(value)));
    return this;
  }

  public InsertItemConfigurer item(IField field) {
    isnertItems.add(new InsertItem(field));
    return this;
  }
  
  public InsertItemConfigurer item(String field) {
    isnertItems.add(new InsertItem(new Field(field)));
    return this;
  }

  @Override
  public void configure(SqlInsertBuilder parent) {
    parent.addInsertItems(this.isnertItems);
    for (InsertItem child : this.isnertItems) {
      parent.manageValue(child.getValue());
    }
  }
}
