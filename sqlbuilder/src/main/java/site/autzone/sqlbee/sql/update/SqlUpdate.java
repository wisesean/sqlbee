package site.autzone.sqlbee.sql.update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.autzone.sqlbee.model.BinaryCondition;
import site.autzone.sqlbee.model.ICondition;
import site.autzone.sqlbee.model.IQueryObject;
import site.autzone.sqlbee.model.IValue;
import site.autzone.sqlbee.query.AbstractQuery;
import site.autzone.sqlbee.sql.query.QueryUtils;
import site.autzone.sqlbee.update.IUpdate;

public class SqlUpdate implements IUpdate {
  final Logger LOG = LoggerFactory.getLogger(AbstractQuery.class);
  private String updateText;
  private IQueryObject updateObject;
  private List<BinaryCondition> updateItems = new ArrayList<>();
  private List<ICondition> conditions = new ArrayList<>();

  private Map<String, IValue> valueMap = new HashMap<String, IValue>();
  private List<Object> values = new ArrayList<>();

  @Override
  public String toText() {
    this.build();
    LOG.debug("update sql:\n{}\n" + "params:\n{}",this.updateText, this.values.toString());
    return this.updateText;
  }

  @Override
  public void addUpdateItem(UpdateItem updateItem) {
    updateItems.add(updateItem);
  }

  @Override
  public void addCondition(ICondition condition) {
    conditions.add(condition);
  }

  @Override
  public List<Object> getValues() {
    return values;
  }

  @Override
  public String buildUpdateText() {
    Validate.notNull(this.updateObject);
    StringBuffer sb = new StringBuffer();
    sb.append("UPDATE ")
        .append(buildUpdateObject())
        .append(buildUpdateItems())
        .append(buildConditions());
    return sb.toString();
  }

  private Object buildConditions() {
    String condition = QueryUtils.joinTextableWithStr(this.conditions, " AND ");
    if (condition.length() > 0) {
      return " WHERE " + condition;
    } else {
      return condition;
    }
  }

  private Object buildUpdateItems() {
    Validate.isTrue(this.updateItems.size() > 0);
    return " SET " + QueryUtils.joinTextableWithStr(this.updateItems, ",");
  }

  private Object buildUpdateObject() {
    Validate.notNull(this.updateObject);
    return this.updateObject.toText();
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
    this.updateText = buildUpdateText;
  }

  @Override
  public void updateObject(IQueryObject updateObject) {
    this.updateObject = updateObject;
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
