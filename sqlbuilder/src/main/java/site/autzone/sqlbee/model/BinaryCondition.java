package site.autzone.sqlbee.model;

import org.apache.commons.lang3.Validate;

public class BinaryCondition extends AbstractCondition {
	private String connector;
	public BinaryCondition() {
	}
	public BinaryCondition(String connector) {
		this.connector = connector;
	}
	
	public void setLeftField(ITextable leftField) {
		if(super.conditions.size() == 0) {
			super.conditions.add(leftField);
		}else {
			super.conditions.set(0, leftField);
		}
	}
	
	public void setRightField(ITextable rightField) {
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
	
	public String getConnector() {
		return connector;
	}
	public void setConnector(String connector) {
		this.connector = connector;
	}
	
	@Override
	public String toText() {
		Validate.isTrue(super.conditions.size() == 2);
		Validate.notNull(super.conditions.get(0));
		Validate.notNull(super.conditions.get(1));
		StringBuffer sb = new StringBuffer("(");
		sb.append(super.conditions.get(0).toText());
		sb.append(" ").append(connector).append(" ");
		sb.append(super.conditions.get(1).toText()).append(")");
		return sb.toString();
	}
}
