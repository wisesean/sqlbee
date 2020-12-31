package site.autzone.sqlbee.sql;

import org.apache.commons.lang3.Validate;
import site.autzone.sqlbee.ITextAble;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.IColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * Group By
 */
public class Group implements ITextAble {
	private List<IColumn> columns = new ArrayList<>();
	public Group() {
	}
	public Group(Column column) {
		this.columns.add(column);
	}

	public void add(Column column) {
		this.columns.add(column);
	}

	public void addAll(List<IColumn> columns) {
		this.columns.addAll(columns);
	}

	public List<IColumn> columns() {
		return this.columns;
	}

	@Override
	public String output() {
		Validate.isTrue(this.columns.size() > 0);
		StringBuffer sb = new StringBuffer(" GROUP BY ");
		sb.append(TextAbleJoin.joinWithSkip(columns, ","));
		return sb.toString();
	}
}
