package site.autzone.sqlbee.model;

import org.apache.commons.lang3.Validate;

public class IsNullCondition extends AbstractCondition {
	public IsNullCondition() {
	}
	
	public IsNullCondition(String field) {
		super.add(0, new Field(field));
	}
	
	public IsNullCondition(IField field) {
		super.add(0, field);
	}
	
	@Override
	public String toText() {
		Validate.notNull(super.conditions.get(0));
		StringBuffer sb = new StringBuffer("(");
		sb.append(super.conditions.get(0).toText())
		  .append(" IS NULL)");
		return sb.toString();
	}
}
