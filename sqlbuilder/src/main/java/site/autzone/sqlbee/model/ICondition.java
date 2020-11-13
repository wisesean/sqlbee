package site.autzone.sqlbee.model;

import java.util.List;

public interface ICondition extends ITextable{
	void add(ITextable in);
	void add(int idx, ITextable in);
	boolean remove(ITextable in);
	ITextable getChild(int index);
	List<ITextable> getChildren();
}
