package site.autzone.sqlbee.configurer;

import site.autzone.configurer.AbstractConfigAbleConfigurer;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.value.Value;

public class MaxResultsConfigurer extends AbstractConfigAbleConfigurer<SqlBuilder> {
	private Integer maxResults;
	public MaxResultsConfigurer maxResults(Integer maxResults) {
		this.maxResults = maxResults;
		return this;
	}
	
	@Override
	public void doConfigure(SqlBuilder parent) {
		//管理到builder
		parent.manageValue(new Value(maxResults));
		//字符串设置到builder中
		parent.setMaxResults(maxResults);
	}
}
