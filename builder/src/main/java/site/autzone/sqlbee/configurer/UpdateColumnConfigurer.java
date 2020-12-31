package site.autzone.sqlbee.configurer;

import java.util.ArrayList;
import java.util.List;

import site.autzone.configurer.AbstractConfigurer;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.ITextAble;
import site.autzone.sqlbee.IValue;
import site.autzone.sqlbee.column.UpdateColumn;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.value.Value;

public class UpdateColumnConfigurer extends AbstractConfigurer<SqlBuilder> {
	private List<UpdateColumn> updateColumns = new ArrayList<>();

	public UpdateColumnConfigurer updateColumn(IColumn column, IValue value) {
		updateColumns.add(new UpdateColumn(column, value));
		return this;
	}
	
	public UpdateColumnConfigurer updateColumn(String column, IValue value) {
		updateColumns.add(new UpdateColumn(new Column(column), value));
		return this;
	}
	
	public UpdateColumnConfigurer updateColumn(String column, String value) {
		updateColumns.add(new UpdateColumn(new Column(column), new Value(value)));
		return this;
	}
	
	@Override
	public void configure(SqlBuilder parent) {
		parent.addUpdateColumns(this.updateColumns);
		for(UpdateColumn child : this.updateColumns) {
			for(ITextAble itemField : child.getChildren()) {
				if(itemField instanceof IValue) {
					parent.manageValue((IValue)itemField);
				}
			}
		}
	}

}
