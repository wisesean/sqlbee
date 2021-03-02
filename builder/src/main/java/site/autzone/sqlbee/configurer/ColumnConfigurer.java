package site.autzone.sqlbee.configurer;

import java.util.ArrayList;
import java.util.List;

import site.autzone.configurer.AbstractConfigurer;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.builder.SqlBuilder;

public class ColumnConfigurer extends AbstractConfigurer<SqlBuilder> {
	private List<IColumn> columns = new ArrayList<>();
	private List<IColumn> countColumns = new ArrayList<>();
	
	public ColumnConfigurer() {
	}
	
	public ColumnConfigurer(SqlBuilder parent) {
		init(parent);
	}

	public ColumnConfigurer countColumn(String name) {
		countColumns.add(new Column(name));
		return this;
	}
	
	public ColumnConfigurer column(String name) {
		columns.add(new Column(null, name, null));
		return this;
	}
	
	public ColumnConfigurer column(String name, String alias) {
		columns.add(new Column(null, name, alias));
		return this;
	}
	
	public ColumnConfigurer column(String prefix, String name, String alias) {
		columns.add(new Column(prefix, name, alias));
		return this;
	}
	
	@Override
	public void configure(SqlBuilder parent) {
		parent.addAllColumn(this.columns);
		parent.addAllCountColumn(this.countColumns);
	}

}
