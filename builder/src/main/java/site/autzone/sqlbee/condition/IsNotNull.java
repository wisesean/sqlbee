package site.autzone.sqlbee.condition;

import org.apache.commons.lang3.Validate;
import site.autzone.sqlbee.condition.AbstractCondition;

public class IsNotNull extends AbstractCondition {
	@Override
	public String output() {
		Validate.notNull(super.conditions.get(0));
		StringBuffer sb = new StringBuffer("(");
		sb.append(super.conditions.get(0).output())
		  .append(" IS NOT NULL)");
		return sb.toString();
	}

}
