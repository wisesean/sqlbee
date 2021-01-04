package site.autzone.sqlbee.configurer;

import site.autzone.configurer.AbstractConfigurerAbleConfigurer;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.value.Value;

public class FirstResultsConfigurer extends AbstractConfigurerAbleConfigurer<SqlBuilder> {
	private Value firstResults;
	public FirstResultsConfigurer firstResult(Integer firstResults) {
		this.firstResults = new Value(firstResults);
		return this;
	}
	
	@Override
	public void doConfigure(SqlBuilder parent) {
		//管理到builder
		parent.manageValue(this.firstResults);
		//字符串设置到builder中
		parent.setFirstResults(this.firstResults);
	}
}
