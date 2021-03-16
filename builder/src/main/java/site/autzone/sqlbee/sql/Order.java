package site.autzone.sqlbee.sql;

import site.autzone.sqlbee.ITextAble;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.injection.SqlCheck;

import java.util.ArrayList;
import java.util.List;

/**
 * order by
 */
public class Order implements ITextAble {
	private List<IColumn> columns = new ArrayList<>();
	private String order = "DESC";
	
	public Order() {
	}
	
	public Order(Column column) {
		this.add(column);
	}
	
	public Order(IColumn column, String order) {
		this.add(column);
		this.order = order;
	}
	
	public void add(IColumn column) {
		this.columns.add(column);
	}

	public void addAll(List<IColumn> columns) {
		this.columns.addAll(columns);
	}
	
	public void order(String order) {
		this.order = order;
	}

	public List<IColumn> columns() {
		return this.columns;
	}

	@Override
	public String output() {
		if(this.columns.size() == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer(" ORDER BY ").append(TextAbleJoin.joinWithSkip(SqlCheck.MODE.STRICT, this.columns, ",")).append(" ").append(order);
		return sb.toString();
	}
}
