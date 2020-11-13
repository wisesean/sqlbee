package site.autzone.sqlbee.sql.query.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import site.autzone.sqlbee.builder.AbstractConfigableConfigurer;
import site.autzone.sqlbee.builder.Configurer;
import site.autzone.sqlbee.model.AbstractCondition;
import site.autzone.sqlbee.model.BinaryCondition;
import site.autzone.sqlbee.model.Field;
import site.autzone.sqlbee.model.IField;
import site.autzone.sqlbee.model.ITextable;
import site.autzone.sqlbee.model.IValue;

public class BinaryConditionConfigurer extends AbstractConfigableConfigurer<SqlQueryBuilder> {
	private final static int leftIdx = 0;
	private final static int rightIdx = 1;
	
	BinaryCondition binaryCondition = new BinaryCondition();
	
	@SuppressWarnings("rawtypes")
	Map<Configurer, Integer> map = new HashMap<>();
	
	private String connector = "=";
	
	public BinaryConditionConfigurer() {
	}
	
	public BinaryConditionConfigurer(String connector) {
		this.connector = connector;
	}

	public BinaryConditionConfigurer connector(String connector) {
		binaryCondition.setConnector(connector);
		return this;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BinaryConditionFieldConfigurer left() {
		Configurer fieldConfigurer = new BinaryConditionFieldConfigurer();
		fieldConfigurer.init(this);
		this.add(fieldConfigurer);
		map.put(fieldConfigurer, leftIdx);
		return (BinaryConditionFieldConfigurer)fieldConfigurer;
	}
	
	public BinaryConditionConfigurer left(IValue value) {
		this.binaryCondition.setLeftField(value);
		return this;
	}
	
	public BinaryConditionConfigurer left(String field) {
		this.binaryCondition.setLeftField(new Field(field));
		return this;
	}
	
	public BinaryConditionConfigurer left(IField field) {
		this.binaryCondition.setLeftField(field);
		return this;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BinaryConditionFieldConfigurer right() {
		Configurer fieldConfigurer = new BinaryConditionFieldConfigurer();
		fieldConfigurer.init(this);
		this.add(fieldConfigurer);
		map.put(fieldConfigurer, rightIdx);
		return (BinaryConditionFieldConfigurer)fieldConfigurer;
	}
	
	public BinaryConditionConfigurer right(IField field) {
		this.binaryCondition.setRightField(field);
		return this;
	}
	
	public BinaryConditionConfigurer right(IValue value) {
		this.binaryCondition.setRightField(value);
		return this;
	}
	
	public BinaryConditionConfigurer right(String field) {
		this.binaryCondition.setRightField(new Field(field));
		return this;
	}
	
	@Override
	public void doConfigure(SqlQueryBuilder parent) {
		if(this.binaryCondition.getConnector() == null) {
			this.binaryCondition.setConnector(this.connector);
		}
		
		//若条件是值的需要纳入管理
		manageAllValue(parent, this.binaryCondition.getChildren());
		
		parent.addCondition(this.binaryCondition);
	}
	
	private void manageAllValue(SqlQueryBuilder parent, List<ITextable> children) {
		for(ITextable child : children) {
			if(child instanceof IValue) {
				parent.manageValue((IValue)child);
			}else if(child instanceof AbstractCondition) {
				AbstractCondition condition = (AbstractCondition)child;
				manageAllValue(parent, condition.getChildren());
			}
		}
	}
}