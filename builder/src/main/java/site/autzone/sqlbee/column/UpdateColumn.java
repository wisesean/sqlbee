package site.autzone.sqlbee.column;

import org.apache.commons.lang3.Validate;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.IValue;
import site.autzone.sqlbee.condition.Condition;

public class UpdateColumn extends Condition {
	public UpdateColumn() {
		super("=");
	}
	
	public UpdateColumn(IColumn column, IValue value) {
		super("=");
		super.setLeftField(column);
		super.setRightField(value);
	}

	@Override
	public String output() {
		Validate.isTrue(super.getChildren().size() == 2);
		Validate.notNull(super.getChildren().get(0));
		Validate.notNull(super.getChildren().get(1));
		StringBuffer sb = new StringBuffer();
		sb.append(super.getChildren().get(0).output());
		sb.append(" = ");
		sb.append(super.getChildren().get(1).output());
		return sb.toString();
	}
}
