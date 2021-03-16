package site.autzone.sqlbee.sql;

import org.apache.commons.lang3.Validate;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.ICondition;
import site.autzone.sqlbee.ITextAble;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.injection.SqlCheck;

import java.util.ArrayList;
import java.util.List;

public class Having implements ITextAble {
	private List<ICondition> conditions = new ArrayList<>();
	public Having() {
	}
	public Having(ICondition condition) {
		this.conditions.add(condition);
	}

	public void add(ICondition condition) {
		this.conditions.add(condition);
	}

	public void addAll(List<ICondition> conditions) {
		this.conditions.addAll(conditions);
	}

	public List<ICondition> conditions() {
		return this.conditions;
	}

	@Override
	public String output() {
		Validate.isTrue(this.conditions.size() > 0);
		StringBuffer sb = new StringBuffer(" HAVING ");
		sb.append(TextAbleJoin.joinWithSkip(SqlCheck.MODE.STRICT, conditions, ","));
		return sb.toString();
	}
}
