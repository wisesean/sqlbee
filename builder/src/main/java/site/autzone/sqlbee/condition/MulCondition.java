package site.autzone.sqlbee.condition;

import site.autzone.sqlbee.sql.TextAbleJoin;

public class MulCondition extends AbstractCondition {
	private String operator;
	public MulCondition(String operator) {
		this.operator = operator;
	}
	@Override
	public String output() {
		return "("+ TextAbleJoin.joinWithSkip(super.getChildren(), " "+operator+" ")+")";
	}
}
