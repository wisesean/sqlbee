package site.autzone.sqlbee.sql.query.builder;

import site.autzone.sqlbee.model.AbstractCondition;
import site.autzone.sqlbee.model.Field;
import site.autzone.sqlbee.model.IField;
import site.autzone.sqlbee.model.IsNullCondition;

public class IsNullConditionConfigurer extends AbstractConditionConfigurer {
	public IsNullConditionConfigurer field(String field) {
		super.add(0, new Field(field));
		return this;
	}
	
	public IsNullConditionConfigurer field(IField field) {
		super.add(0, field);
		return this;
	}
	@Override
	public void doConditionConfigure(SqlQueryBuilder parent) {
		AbstractCondition isNullCondition = new IsNullCondition();
		isNullCondition.addAll(super.conditions);
		parent.addCondition(isNullCondition);
	}
	
}
