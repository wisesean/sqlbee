package site.autzone.sqlbee.sql.insert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.autzone.sqlbee.insert.IInsert;
import site.autzone.sqlbee.model.IField;
import site.autzone.sqlbee.model.IQueryObject;
import site.autzone.sqlbee.model.IValue;
import site.autzone.sqlbee.query.AbstractQuery;
import site.autzone.sqlbee.sql.query.QueryUtils;

public class SqlInsert implements IInsert {
  final Logger LOG = LoggerFactory.getLogger(AbstractQuery.class);
  private String insertText;
  private IQueryObject insertObject;
  private List<InsertItem> insertItems = new ArrayList<>();

  private Map<String, IValue> valueMap = new HashMap<String, IValue>();
  private List<Object> values = new ArrayList<>();

  @Override
  public String toText() {
    this.build();
    LOG.debug("insert sql:\n{}\nparams:\n{}" , this.insertText, this.values.toString());
    return this.insertText;
  }

  @Override
  public void addInsertItem(InsertItem updateItem) {
    insertItems.add(updateItem);
  }

  @Override
  public List<Object> getValues() {
    return values;
  }

  @Override
  public String buildUpdateText() {
    Validate.notNull(this.insertObject);
    StringBuffer sb = new StringBuffer();
    sb.append("INSERT INTO ")
        .append(buildInsertObject())
        .append(buildItems())
        .append(buildValues());
    return sb.toString();
  }

  private Object buildItems() {
    Validate.isTrue(this.insertItems.size() > 0);
    List<IField> items = new ArrayList<>();
    for (InsertItem insertItem : this.insertItems) {
      items.add(insertItem.getField());
    }
    return "(" + QueryUtils.joinTextableWithStr(items, ",") + ")";
  }

  private Object buildValues() {
    Validate.isTrue(this.insertItems.size() > 0);
    List<IValue> values = new ArrayList<>();
    for (InsertItem insertItem : this.insertItems) {
      values.add(insertItem.getValue());
    }
    return " VALUES(" + QueryUtils.joinTextableWithStr(values, ",") + ")";
  }

  private Object buildInsertObject() {
    Validate.notNull(this.insertObject);
    return this.insertObject.toText();
  }

  @Override
  public void build() {
    String buildUpdateText = this.buildUpdateText();
    this.values.clear();
    List<String> placeHolders = new ArrayList<>();
    int startIdx = buildUpdateText.indexOf("${");
    while (startIdx != -1) {
      int nextIdx = startIdx + "${".length();
      int endIdx = buildUpdateText.indexOf("}", startIdx);
      if (endIdx != -1) {
        String valuePlaceHolder = buildUpdateText.substring(startIdx + "${".length(), endIdx);
        placeHolders.add("${" + valuePlaceHolder + "}");
        IValue value = valueMap.get(valuePlaceHolder);
        Validate.notNull(value);
        this.values.add(value.convert());
        nextIdx = endIdx + 1;
      }
      startIdx = buildUpdateText.indexOf("${", nextIdx);
    }
    for (String placeHolder : placeHolders) {
      buildUpdateText = buildUpdateText.replace(placeHolder, "?");
    }
    this.insertText = buildUpdateText;
  }

  public void insertObject(IQueryObject insertObject) {
    this.insertObject = insertObject;
  }

  public Map<String, IValue> getValueMap() {
    return valueMap;
  }

  public void setValueMap(Map<String, IValue> valueMap) {
    this.valueMap = valueMap;
  }

  public void setValues(List<Object> values) {
    this.values = values;
  }
}
