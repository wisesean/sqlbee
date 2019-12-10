package site.autzone.sqlbee.sql.update.builder;

import site.autzone.sqlbee.model.AbstractCondition;
import site.autzone.sqlbee.model.BetweenCondition;
import site.autzone.sqlbee.model.IField;
import site.autzone.sqlbee.model.IValue;

public class BetweenConditionConfigurer extends AbstractConditionConfigurer {
	public BetweenConditionConfigurer field(IField field) {
		super.add(0, field);
		return this;
	}
	public BetweenConditionConfigurer leftValue(IValue value) {
		super.add(1, value);
		return this;
	}
	public BetweenConditionConfigurer rightValue(IValue value) {
		super.add(2, value);
		return this;
	}
	@Override
	public void doConditionConfigure(SqlUpateBuilder parent) {
		AbstractCondition betweenCondition = new BetweenCondition();
		betweenCondition.addAll(conditions);
		parent.addCondition(betweenCondition);
	}
	
}
