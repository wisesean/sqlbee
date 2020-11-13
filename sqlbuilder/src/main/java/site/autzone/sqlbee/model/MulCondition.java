package site.autzone.sqlbee.model;

import site.autzone.sqlbee.sql.query.QueryUtils;

public class MulCondition extends AbstractCondition {
	private String connector;
	public MulCondition(String connector) {
		this.connector = connector;
	}
	@Override
	public String toText() {
		return "("+QueryUtils.joinTextableWithStr(super.getChildren(), " "+connector+" ")+")";
	}
}
