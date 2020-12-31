package site.autzone.sqlbee.condition;

import org.apache.commons.lang3.Validate;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.IColumn;

public class IsNull extends AbstractCondition {
	public IsNull() {
	}
	
	public IsNull(String column) {
		super.add(0, new Column(column));
	}
	
	public IsNull(IColumn column) {
		super.add(0, column);
	}
	
	@Override
	public String output() {
		Validate.notNull(super.conditions.get(0));
		StringBuffer sb = new StringBuffer("(");
		sb.append(super.conditions.get(0).output())
		  .append(" IS NULL)");
		return sb.toString();
	}
}
