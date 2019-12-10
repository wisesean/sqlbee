package site.autzone.sqlbee.sql.query.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import site.autzone.sqlbee.builder.AbstractConfigableConfigurer;
import site.autzone.sqlbee.builder.Configurer;
import site.autzone.sqlbee.model.AbstractCondition;
import site.autzone.sqlbee.model.ITextable;
import site.autzone.sqlbee.model.IValue;

public abstract class AbstractConditionConfigurer extends AbstractConfigableConfigurer<SqlQueryBuilder> {
	@SuppressWarnings("rawtypes")
	Map<Configurer, Integer> map = new HashMap<>();
	List<ITextable> conditions = new ArrayList<>();

	public void add(ITextable in) {
		conditions.add(in);
	}
	
	public void add(int idx, ITextable in) {
		if(conditions.size() < idx+1) {
			for(int i = 0;i <= (idx - conditions.size()); i++) {
				conditions.add(null);
			}
		}
		conditions.set(idx, in);
	}
	
	@Override
	public void doConfigure(SqlQueryBuilder parent) {
		//若条件是值的需要纳入管理
		manageAllValue(parent, this.conditions);
		doConditionConfigure(parent);
	}
	
	private void manageAllValue(SqlQueryBuilder parent, List<ITextable> children) {
		for(ITextable child : children) {
			if(child instanceof IValue) {
				parent.manageValue((IValue)child);
			}else if(child instanceof AbstractCondition) {
				AbstractCondition condition = (AbstractCondition)child;
				manageAllValue(parent, condition.getAllChild());
			}
		}
	}
	
	public abstract void doConditionConfigure(SqlQueryBuilder parent);
}
