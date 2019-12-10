package site.autzone.sqlbee.sql.query.builder;

import site.autzone.sqlbee.model.Value;

public class MaxResultsConfigurer extends AbstractConditionConfigurer {
	private Value maxResultsValue;
	public MaxResultsConfigurer maxResults(Integer maxResults) {
		this.maxResultsValue = new Value(maxResults, Value.TYPE.INT.name());
		return this;
	}
	
	@Override
	public void doConfigure(SqlQueryBuilder parent) {
		//管理到builder
		maxResultsValue = (Value) parent.manageValue(maxResultsValue);
		//字符串设置到builder中
		parent.setMaxResultsKey(maxResultsValue.toText());
	}

	@Override
	public void doConditionConfigure(SqlQueryBuilder parent) {
		//已经重写父类方法
	}
}
