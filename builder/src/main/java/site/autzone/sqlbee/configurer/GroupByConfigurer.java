package site.autzone.sqlbee.configurer;

import site.autzone.configurer.AbstractConfigAbleConfigurer;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.sql.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupByConfigurer extends AbstractConfigAbleConfigurer<SqlBuilder> {
	private List<IColumn> columns = new ArrayList<>();

	public GroupByConfigurer column(Column column) {
		this.columns.add(column);
		return this;
	}

	public GroupByConfigurer columns(List<IColumn> columns) {
		this.columns.addAll(columns);
		return this;
	}

	@Override
	public void doConfigure(SqlBuilder parent) {
		Group group = new Group();
		group.addAll(this.columns);
		parent.group(group);
	}
}
