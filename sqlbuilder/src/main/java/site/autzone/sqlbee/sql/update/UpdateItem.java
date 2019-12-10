package site.autzone.sqlbee.sql.update;

import org.apache.commons.lang3.Validate;

import site.autzone.sqlbee.model.BinaryCondition;
import site.autzone.sqlbee.model.IField;
import site.autzone.sqlbee.model.IValue;

public class UpdateItem extends BinaryCondition {
	public UpdateItem() {
		super("=");
	}
	
	public UpdateItem(IField field, IValue value) {
		super("=");
		super.setLeftField(field);
		super.setRightField(value);
	}

	@Override
	public String toText() {
		Validate.isTrue(super.getAllChild().size() == 2);
		Validate.notNull(super.getAllChild().get(0));
		Validate.notNull(super.getAllChild().get(1));
		StringBuffer sb = new StringBuffer();
		sb.append(super.getAllChild().get(0).toText());
		sb.append(" = ");
		sb.append(super.getAllChild().get(1).toText());
		return sb.toString();
	}
}
