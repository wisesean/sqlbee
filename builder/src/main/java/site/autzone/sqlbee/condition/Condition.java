package site.autzone.sqlbee.condition;

import org.apache.commons.lang3.Validate;
import site.autzone.sqlbee.ITextAble;
import site.autzone.sqlbee.IValue;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.condition.AbstractCondition;
import site.autzone.sqlbee.value.Value;

public class Condition extends AbstractCondition {
	private String operator;
	public Condition() {
	}
	public Condition(String operator) {
		this.operator = operator;
	}

	public Condition(String operator, String left, String right) {
		this.operator = operator;
		this.setLeftField(new Column(left));
		this.setRightField(new Column(right));
	}

	public Condition(String operator, String left, IValue right) {
		this.operator = operator;
		this.setLeftField(new Column(left));
		this.setRightField(right);
	}
	
	public void setLeftField(ITextAble leftField) {
		if(super.conditions.size() == 0) {
			super.conditions.add(leftField);
		}else {
			super.conditions.set(0, leftField);
		}
	}
	
	public void setRightField(ITextAble rightField) {
		if(super.conditions.size() == 0) {
			super.conditions.add(null);
			super.conditions.add(rightField);
		}else {
			if(super.conditions.size() == 1) {
				super.conditions.add(rightField);
			}else {
				super.conditions.set(1, rightField);
			}
		}
	}
	
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	@Override
	public String output() {
		Validate.isTrue(super.conditions.size() == 2);
		Validate.notNull(super.conditions.get(0));
		Validate.notNull(super.conditions.get(1));
		StringBuffer sb = new StringBuffer("(");
		sb.append(super.conditions.get(0).output());
		sb.append(" ").append(operator).append(" ");
		sb.append(super.conditions.get(1).output()).append(")");
		return sb.toString();
	}
}
