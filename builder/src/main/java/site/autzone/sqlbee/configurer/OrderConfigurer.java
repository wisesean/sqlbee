package site.autzone.sqlbee.configurer;

import site.autzone.configurer.AbstractConfigAbleConfigurer;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.sql.Order;
import site.autzone.sqlbee.builder.SqlBuilder;

import java.util.ArrayList;
import java.util.List;

public class OrderConfigurer extends AbstractConfigAbleConfigurer<SqlBuilder> {
	private String order = "DESC";
	private List<IColumn> columns = new ArrayList<>();

	public OrderConfigurer column(String column) {
		this.columns.add(new Column(column));
		return this;
	}

	public OrderConfigurer column(Column column) {
		this.columns.add(column);
		return this;
	}

	public OrderConfigurer columns(List<IColumn> columns) {
		this.columns.addAll(columns);
		return this;
	}
	
	public OrderConfigurer asc() {
		this.order = "ASC";
		return this;
	}

	public OrderConfigurer desc() {
		this.order = "DESC";
		return this;
	}

	@Override
	public void doConfigure(SqlBuilder parent) {
		Order order = new Order();
		order.addAll(this.columns);
		order.order(this.order);
		parent.order(order);
	}
}
