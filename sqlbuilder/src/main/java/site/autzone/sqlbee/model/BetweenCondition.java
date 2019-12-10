package site.autzone.sqlbee.model;

import org.apache.commons.lang3.Validate;

public class BetweenCondition extends AbstractCondition {
	public void addField(IField field) {
		super.conditions.set(0, field);
	}
	public void setLeftValue(IValue leftValue) {
		super.conditions.set(1, leftValue);
	}
	
	public void setRightValue(Field rightValue) {
		super.conditions.set(2, rightValue);
	}
	
	@Override
	public String toText() {
		Validate.isTrue(super.conditions.size() == 3);
		Validate.notNull(super.conditions.get(0));
		Validate.notNull(super.conditions.get(1));
		Validate.notNull(super.conditions.get(2));
		StringBuffer sb = new StringBuffer("(");
		sb.append(super.conditions.get(0).toText());
		sb.append(" BETWEEN ");
		sb.append(super.conditions.get(1).toText());
		sb.append(" AND ");
		sb.append(super.conditions.get(2).toText()).append(")");
		return sb.toString();
	}
}
