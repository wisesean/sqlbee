package site.autzone.sqlbee.configurer;

import site.autzone.configurer.AbstractConfigurerAbleConfigurer;
import site.autzone.sqlbee.IValue;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.value.Value;

public class MaxResultsConfigurer extends AbstractConfigurerAbleConfigurer<SqlBuilder> {
	private IValue maxResults;
	public MaxResultsConfigurer maxResults(Integer maxResults) {
		this.maxResults = new Value(maxResults);
		return this;
	}
	
	@Override
	public void doConfigure(SqlBuilder parent) {
		//管理到builder
		parent.manageValue(this.maxResults);
		//字符串设置到builder中
		parent.setMaxResults(this.maxResults);
	}
}
