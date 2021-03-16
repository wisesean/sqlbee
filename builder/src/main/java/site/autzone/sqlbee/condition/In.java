package site.autzone.sqlbee.condition;

import java.util.List;

import org.apache.commons.lang3.Validate;
import site.autzone.sqlbee.ITextAble;
import site.autzone.sqlbee.injection.SqlCheck;
import site.autzone.sqlbee.sql.TextAbleJoin;

public class In extends AbstractCondition {
	private static int maxInValues = 999;
	/**
	 * IN查询条件
	 */
	@Override
	public String output() {
		Validate.isTrue(super.conditions.size() >= 2);
		Validate.notNull(super.conditions.get(0));
		Validate.notNull(super.conditions.get(1));
		
		StringBuffer sb = new StringBuffer("(");
		List<ITextAble> inValues = super.conditions.subList(1, super.conditions.size());
		for(int i = 0;i < (inValues.size() / maxInValues + 1);i ++) {
			int startIdx = i*maxInValues;
			int endIdx = inValues.size();
			if(inValues.size() > (i+1)*maxInValues) {
				endIdx = (i+1)*maxInValues;
			}
			if(i > 0) {
				sb.append(" OR ");
			}
			sb.append(super.conditions.get(0).output())
			  .append(" IN(").append(TextAbleJoin.joinWithSkip(SqlCheck.MODE.LOOSELY,
							inValues.subList(startIdx, endIdx),
							","))
			  .append(")");
		}
		sb.append(")");
		return sb.toString();
	}
}
