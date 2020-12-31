package site.autzone.sqlbee.configurer;

import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.condition.AbstractCondition;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.condition.Condition;
import site.autzone.sqlbee.IValue;
import site.autzone.sqlbee.condition.MulCondition;

public class MulConditionConfigurer extends AbstractConditionConfigurer {
	private String operator;
	public MulConditionConfigurer(String operator) {
		this.operator = operator;
	}
	
	public MulConditionConfigurer addCondition(String leftField, String operator, IValue rightValue) {
		Condition condition = new Condition(operator);
		condition.setLeftField(new Column(leftField));
		condition.setRightField(rightValue);
		super.add(condition);
		
		return this;
	}
	
	public MulConditionConfigurer addCondition(String leftField, IValue rightValue) {
		Condition condition = new Condition("=");
		condition.setLeftField(new Column(leftField));
		condition.setRightField(rightValue);
		super.add(condition);
		
		return this;
	}
	
	public MulConditionConfigurer addCondition(String leftField, String operator, String rightField) {
		Condition condition = new Condition(operator);
		condition.setLeftField(new Column(leftField));
		condition.setRightField(new Column(rightField));
		super.add(condition);
		
		return this;
	}
	
	public MulConditionConfigurer addCondition(String leftField, String rightField) {
		Condition condition = new Condition("=");
		condition.setLeftField(new Column(leftField));
		condition.setRightField(new Column(rightField));
		super.add(condition);
		
		return this;
	}

	@Override
	public void doConditionConfigure(SqlBuilder parent) {
		AbstractCondition mulCondition = new MulCondition(this.operator);
		mulCondition.addAll(conditions);
		parent.addCondition(mulCondition);
	}
	
}