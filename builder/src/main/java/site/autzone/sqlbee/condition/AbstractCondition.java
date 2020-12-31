package site.autzone.sqlbee.condition;

import site.autzone.sqlbee.ICondition;
import site.autzone.sqlbee.ITextAble;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCondition implements ICondition {
	List<ITextAble> conditions = new ArrayList<>();

	@Override
	public void add(ITextAble in) {
		conditions.add(in);
	}
	
	@Override
	public void add(int idx, ITextAble in) {
		if(conditions.size() < idx+1) {
			for(int i = 0;i <= (idx - conditions.size()); i++) {
				conditions.add(null);
			}
		}
		conditions.set(idx, in);
	}

	@Override
	public boolean remove(ITextAble in) {
		return conditions.remove(in);
	}

	@Override
	public ITextAble getChild(int index) {
		return conditions.get(index);
	}
	
	@Override
	public List<ITextAble> getChildren() {
		return conditions;
	}

	public void addAll(List<ITextAble> conditions) {
		this.conditions.addAll(conditions);
	}

}
