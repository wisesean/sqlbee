package site.autzone.sqlbee.configurer;

import site.autzone.configurer.AbstractConfigurerAbleConfigurer;
import site.autzone.sqlbee.ICondition;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.sql.Having;

import java.util.ArrayList;
import java.util.List;

public class HavingConfigurer extends AbstractConfigurerAbleConfigurer<SqlBuilder> {
	private List<ICondition> conditions = new ArrayList<>();

	public HavingConfigurer condition(ICondition condition) {
		this.conditions.add(condition);
		return this;
	}

	public HavingConfigurer columns(List<ICondition> conditions) {
		this.conditions.addAll(conditions);
		return this;
	}

	@Override
	public void doConfigure(SqlBuilder parent) {
		Having having = new Having();
		having.addAll(this.conditions);
		parent.having(having);
		parent.manageAllValue(new ArrayList<>(having.conditions()));
	}
}
