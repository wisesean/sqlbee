package site.autzone.sqlbee.sql.query;

import java.util.List;

import site.autzone.sqlbee.model.ITextable;

public class QueryUtils {
	public static <T extends ITextable> String joinTextableWithStr(List<T> list, String skipChar) {
		StringBuffer sb = new StringBuffer();
		for(ITextable textable : list) {
			if(sb.length() == 0) {
				sb.append(textable.toText());
			}else {
				sb.append(skipChar).append(textable.toText());
			}
		}
		return sb.toString();
	}
}
