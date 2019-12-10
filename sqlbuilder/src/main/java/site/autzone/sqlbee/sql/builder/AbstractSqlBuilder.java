package site.autzone.sqlbee.sql.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import site.autzone.sqlbee.builder.AbstractConfiguredBuilder;
import site.autzone.sqlbee.model.IQueryObject;
import site.autzone.sqlbee.model.IValue;

public abstract class AbstractSqlBuilder<O> extends AbstractConfiguredBuilder<O> {
	private List<IQueryObject> queryObjects = new ArrayList<>();
	//占位符对应的value
	private Map<String, IValue> valueMap = new HashMap<String, IValue>();
	//所有的value，占位符的下标对应数组下标
	private List<Object> values = new ArrayList<>();

	//受到builder管理的value
	public IValue manageValue(IValue value) {
		if(value.getIdx() == null) {
			int idx = values.size();
			//同一个value对应多个idx时values未变化，需要后移idx
			while(valueMap.get(""+idx) != null) {
				idx++;
			}
			value.setIdx(idx);
			values.add(value);
			valueMap.put(""+idx, value);
		}else {
			int idx = value.getIdx();
			valueMap.put(""+idx, value);
		}
		return value;
	}
	
	//移除被管理的value
	public void removeManagedValue(IValue value) {
		if(value.getIdx() != null) {
			values.set(value.getIdx(), null);
			valueMap.remove("?"+value.getIdx());
		}
	}
	
	protected void addAllQueryObject(List<IQueryObject> sqlQueryObjects) {
		queryObjects.addAll(sqlQueryObjects);
	}

	public Map<String, IValue> getValueMap() {
		return valueMap;
	}

	public void setValueMap(Map<String, IValue> valueMap) {
		this.valueMap = valueMap;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

	public List<IQueryObject> getQueryObjects() {
		return queryObjects;
	}

	public void setQueryObjects(List<IQueryObject> queryObjects) {
		this.queryObjects = queryObjects;
	}
}
