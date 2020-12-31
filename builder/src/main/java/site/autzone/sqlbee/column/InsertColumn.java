package site.autzone.sqlbee.column;

import org.apache.commons.lang3.Validate;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.ITextAble;
import site.autzone.sqlbee.IValue;
import site.autzone.sqlbee.value.Value;

public class InsertColumn implements ITextAble {
  private IColumn column;
  private IValue value;

  public InsertColumn(IColumn column) {
    this.column = column;
  }

  public InsertColumn(IColumn column, IValue value) {
    this.column = column;
    this.value = value;
  }

  public InsertColumn(String column, IValue value) {
    this.column = new Column(column);
    this.value = value;
  }

  public InsertColumn(String column, String value) {
    this.column = new Column(column);
    this.value = new Value(value);
  }

  @Override
  public String output() {
    Validate.notNull(this.column);
    Validate.notNull(this.value);
    return this.column.output() + ":" + this.value.output();
  }

  public IColumn getField() {
    return column;
  }

  public void setField(IColumn column) {
    this.column = column;
  }

  public IValue getValue() {
    return value;
  }

  public void setValue(IValue value) {
    this.value = value;
  }
}
