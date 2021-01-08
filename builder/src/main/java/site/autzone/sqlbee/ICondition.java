package site.autzone.sqlbee;

import java.util.List;

/**
 * 条件
 */
public interface ICondition extends ITextAble, HasChildren {
	void add(ITextAble in);
	void add(int idx, ITextAble in);
	boolean remove(ITextAble in);
}
