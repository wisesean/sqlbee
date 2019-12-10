package site.autzone.sqlbee.sql.query.builder;

import site.autzone.sqlbee.model.Field;
import site.autzone.sqlbee.model.IField;
import site.autzone.sqlbee.model.ITextable;
import site.autzone.sqlbee.sql.query.OrderBy;

public class OrderByConfigurer extends AbstractConditionConfigurer {
	private String order;
	
	public OrderByConfigurer field(String field) {
		super.add(0, new Field(field));
		return this;
	}
	
	public OrderByConfigurer field(IField field) {
		super.add(0, field);
		return this;
	}
	
	public OrderByConfigurer order(String order) {
		this.order = order;
		return this;
	}
	
	@Override
	public void doConditionConfigure(SqlQueryBuilder parent) {
		OrderBy orderBy = new OrderBy();
		for(ITextable field : super.conditions) {
			orderBy.addField((IField)field);
		}
		if(order != null) {
			orderBy.setOrder(order);
		}
		parent.orderBy(orderBy);
	}
	
}
