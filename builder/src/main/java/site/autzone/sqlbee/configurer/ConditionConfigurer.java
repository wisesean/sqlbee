package site.autzone.sqlbee.configurer;

import java.util.HashMap;
import java.util.Map;

import site.autzone.configurer.AbstractConfigAbleConfigurer;
import site.autzone.configurer.Configurer;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.condition.Condition;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.IValue;

public class ConditionConfigurer extends AbstractConfigAbleConfigurer<SqlBuilder> {
	private final static int leftIdx = 0;
	private final static int rightIdx = 1;
	
	Condition condition = new Condition();
	
	@SuppressWarnings("rawtypes")
	Map<Configurer, Integer> map = new HashMap<>();
	
	private String operator = "=";
	
	public ConditionConfigurer() {
	}
	
	public ConditionConfigurer(String operator) {
		this.operator = operator;
	}

	public ConditionConfigurer operator(String operator) {
		condition.setOperator(operator);
		return this;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ConditionColumnConfigurer left() {
		Configurer fieldConfigurer = new ConditionColumnConfigurer();
		fieldConfigurer.init(this);
		this.add(fieldConfigurer);
		map.put(fieldConfigurer, leftIdx);
		return (ConditionColumnConfigurer)fieldConfigurer;
	}
	
	public ConditionConfigurer left(IValue value) {
		this.condition.setLeftField(value);
		return this;
	}
	
	public ConditionConfigurer left(String column) {
		this.condition.setLeftField(new Column(column));
		return this;
	}
	
	public ConditionConfigurer left(IColumn column) {
		this.condition.setLeftField(column);
		return this;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ConditionColumnConfigurer right() {
		Configurer fieldConfigurer = new ConditionColumnConfigurer();
		fieldConfigurer.init(this);
		this.add(fieldConfigurer);
		map.put(fieldConfigurer, rightIdx);
		return (ConditionColumnConfigurer)fieldConfigurer;
	}
	
	public ConditionConfigurer right(IColumn column) {
		this.condition.setRightField(column);
		return this;
	}
	
	public ConditionConfigurer right(IValue value) {
		this.condition.setRightField(value);
		return this;
	}
	
	public ConditionConfigurer right(String column) {
		this.condition.setRightField(new Column(column));
		return this;
	}
	
	@Override
	public void doConfigure(SqlBuilder parent) {
		if (this.condition.getOperator() == null) {
			this.condition.setOperator(this.operator);
		}

		//若条件是值的需要纳入管理
		parent.manageAllValue(this.condition.getChildren());

		parent.addCondition(this.condition);
	}
}