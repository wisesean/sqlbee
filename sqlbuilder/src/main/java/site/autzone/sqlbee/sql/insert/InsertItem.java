package site.autzone.sqlbee.sql.insert;

import org.apache.commons.lang3.Validate;

import site.autzone.sqlbee.model.Field;
import site.autzone.sqlbee.model.IField;
import site.autzone.sqlbee.model.ITextable;
import site.autzone.sqlbee.model.IValue;
import site.autzone.sqlbee.model.Value;

public class InsertItem implements ITextable {
  private IField field;
  private IValue value;

  public InsertItem(IField field) {
    this.field = field;
  }

  public InsertItem(IField field, IValue value) {
    this.field = field;
    this.value = value;
  }

  public InsertItem(String field, IValue value) {
    this.field = new Field(field);
    this.value = value;
  }

  public InsertItem(String field, String value) {
    this.field = new Field(field);
    this.value = new Value(value);
  }

  @Override
  public String toText() {
    Validate.notNull(this.field);
    Validate.notNull(this.value);
    return this.field.toText() + ":" + this.value.toText();
  }

  public IField getField() {
    return field;
  }

  public void setField(IField field) {
    this.field = field;
  }

  public IValue getValue() {
    return value;
  }

  public void setValue(IValue value) {
    this.value = value;
  }
}
