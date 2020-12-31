package site.autzone.sqlbee.configurer;

import site.autzone.configurer.AbstractConfigAbleConfigurer;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.value.Value;

public class FirstResultsConfigurer extends AbstractConfigAbleConfigurer<SqlBuilder> {
	private Integer firstResults;
	public FirstResultsConfigurer firstResult(Integer firstResults) {
		this.firstResults = firstResults;
		return this;
	}
	
	@Override
	public void doConfigure(SqlBuilder parent) {
		//管理到builder
		parent.manageValue(new Value(firstResults));
		//字符串设置到builder中
		parent.setFirstResults(firstResults);
	}
}
