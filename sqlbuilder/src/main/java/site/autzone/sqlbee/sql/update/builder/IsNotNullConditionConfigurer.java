package site.autzone.sqlbee.sql.update.builder;

import site.autzone.sqlbee.model.AbstractCondition;
import site.autzone.sqlbee.model.IField;
import site.autzone.sqlbee.model.IsNotNullCondition;

public class IsNotNullConditionConfigurer extends AbstractConditionConfigurer {
	public IsNotNullConditionConfigurer field(IField field) {
		super.add(0, field);
		return this;
	}
	@Override
	public void doConditionConfigure(SqlUpateBuilder parent) {
		AbstractCondition isNotNullCondition = new IsNotNullCondition();
		isNotNullCondition.addAll(super.conditions);
		parent.addCondition(isNotNullCondition);
	}
	
}
