package site.autzone.sqlbee.model;

import org.apache.commons.lang3.Validate;

public class IsNotNullCondition extends AbstractCondition {
	@Override
	public String toText() {
		Validate.notNull(super.conditions.get(0));
		StringBuffer sb = new StringBuffer("(");
		sb.append(super.conditions.get(0).toText())
		  .append(" IS NOT NULL)");
		return sb.toString();
	}

}
