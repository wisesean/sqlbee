package site.autzone.sqlbee.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import site.autzone.sqlbee.model.ICondition;
import site.autzone.sqlbee.model.IField;
import site.autzone.sqlbee.model.IQueryObject;
import site.autzone.sqlbee.model.ITextable;
import site.autzone.sqlbee.model.IValue;

public abstract class AbstractQuery implements IQuery {
	private boolean isCount = false;
	//数据集限制
	private String firstResultKey;
	private String maxResultsKey;
	
	private List<IQueryObject> queryObjects = new ArrayList<>();
	private List<IField> cloumns = new ArrayList<>();
	private List<ICondition> conditions = new ArrayList<>();
	private ITextable orderBy;
	
	private Map<String, IValue> valueMap = new HashMap<String, IValue>();
	private String queryText;
	private List<Object> values = new ArrayList<>();
	
	public void isCount(boolean isCount) {
		this.isCount = isCount;
	}
	
	public boolean isCount() {
		return this.isCount;
	}
	
	public void addQueryObject(IQueryObject queryObject) {
		queryObjects.add(queryObject);
	}
	
	public void addCloumn(IField field) {
		cloumns.add(field);
	}
	
	public void addCondition(ICondition condition) {
		conditions.add(condition);
	}
	
	public void setOrderBy(ITextable orderBy) {
		this.orderBy = orderBy;
	}

	public List<IQueryObject> getQueryObjects() {
		return queryObjects;
	}

	public List<IField> getCloumns() {
		return cloumns;
	}

	public List<ICondition> getConditions() {
		return conditions;
	}

	public ITextable getOrderBy() {
		return orderBy;
	}

	public String getQueryText() {
		return queryText;
	}

	protected void setCount(boolean isCount) {
		this.isCount = isCount;
	}
	protected void setQueryObjects(List<IQueryObject> queryObjects) {
		this.queryObjects = queryObjects;
	}

	protected void setCloumns(List<IField> cloumns) {
		this.cloumns = cloumns;
	}

	protected void setConditions(List<ICondition> conditions) {
		this.conditions = conditions;
	}

	@Override
	public String toText() {
		this.build();
		System.out.println("sql:\n"+this.queryText+"\n"+"params:\n"+this.values.toString());
		return this.queryText;
	}
	
	@Override
	public String buildQueryText() {
		Validate.isTrue(this.queryObjects.size() > 0);
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ")
		  .append(buildCloumns())
		  .append(" FROM ")
		  .append(buildQueryObjects())
		  .append(buildConditions());
		if(!this.isCount) {
			sb.append(buildOrderByPart());
		}
		return sb.toString();
	}
	
	@Override
	public void build() {
		String buildQueryText = this.buildQueryText();
		this.values.clear();
		List<String> placeHolders = new ArrayList<>();
		int startIdx = buildQueryText.indexOf("${");
		while(startIdx != -1) {
			int nextIdx = startIdx + "${".length();
			int endIdx = buildQueryText.indexOf("}", startIdx);
			if(endIdx != -1) {
				String valuePlaceHolder = buildQueryText.substring(startIdx+"${".length(), endIdx);
				placeHolders.add("${"+valuePlaceHolder+"}");
				IValue value = valueMap.get(valuePlaceHolder);
				Validate.notNull(value);
				this.values.add(value.convert());
				nextIdx = endIdx + 1;
			}
			startIdx = buildQueryText.indexOf("${", nextIdx);
        }
		for(String placeHolder : placeHolders) {
			buildQueryText = buildQueryText.replace(placeHolder, "?");
		}
		this.queryText = buildQueryText;
	}

	public String buildOrderByPart() {
		if(this.orderBy != null) {
			return this.orderBy.toText();
		}else {
			return "";
		}
	}

	@Override
	public List<Object> getValues() {
		return values;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}
	
	public void setValueMap(Map<String, IValue> valueMap) {
		this.valueMap = valueMap;
	}

	public String getFirstResultKey() {
		return firstResultKey;
	}

	public void setFirstResultKey(String firstResultKey) {
		this.firstResultKey = firstResultKey;
	}

	public String getMaxResultsKey() {
		return maxResultsKey;
	}

	public void setMaxResultsKey(String maxResultsKey) {
		this.maxResultsKey = maxResultsKey;
	}

	public abstract String buildCloumns();
	public abstract String buildQueryObjects();
	public abstract String buildConditions();
}
