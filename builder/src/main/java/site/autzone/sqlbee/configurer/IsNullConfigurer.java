package site.autzone.sqlbee.configurer;

import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.condition.AbstractCondition;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.condition.IsNull;

public class IsNullConfigurer extends AbstractConditionConfigurer {
	public IsNullConfigurer column(String column) {
		super.add(0, new Column(column));
		return this;
	}
	
	public IsNullConfigurer column(IColumn column) {
		super.add(0, column);
		return this;
	}
	@Override
	public void doConditionConfigure(SqlBuilder parent) {
		AbstractCondition isNullCondition = new IsNull();
		isNullCondition.addAll(super.conditions);
		parent.addCondition(isNullCondition);
	}
	
}
