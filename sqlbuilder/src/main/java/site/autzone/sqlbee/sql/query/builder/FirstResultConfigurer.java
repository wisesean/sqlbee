package site.autzone.sqlbee.sql.query.builder;

import site.autzone.sqlbee.model.Value;

public class FirstResultConfigurer extends AbstractConditionConfigurer {
	private Value firstResultValue;
	public FirstResultConfigurer firstResult(Integer firstResult) {
		this.firstResultValue = new Value(firstResult, Value.TYPE.INT.name());
		return this;
	}
	
	@Override
	public void doConfigure(SqlQueryBuilder parent) {
		//管理到builder
		firstResultValue = (Value) parent.manageValue(firstResultValue);
		//字符串设置到builder中
		parent.setFirstResultKey(firstResultValue.toText());
	}

	@Override
	public void doConditionConfigure(SqlQueryBuilder parent) {
		//已经重写父类方法
	}
}
