package site.autzone.sqlbee.model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCondition implements ICondition {
	List<ITextable> conditions = new ArrayList<>();
	
	@Override
	public void add(ITextable in) {
		conditions.add(in);
	}
	
	@Override
	public void add(int idx, ITextable in) {
		if(conditions.size() < idx+1) {
			for(int i = 0;i <= (idx - conditions.size()); i++) {
				conditions.add(null);
			}
		}
		conditions.set(idx, in);
	}

	@Override
	public boolean remove(ITextable in) {
		return conditions.remove(in);
	}

	@Override
	public ITextable getChild(int index) {
		return conditions.get(index);
	}
	
	@Override
	public List<ITextable> getAllChild() {
		return conditions;
	}

	public void addAll(List<ITextable> conditions) {
		this.conditions.addAll(conditions);
	}

}
