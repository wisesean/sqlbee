package site.autzone.sqlbee.configurer;

import java.util.ArrayList;
import java.util.List;

import site.autzone.configurer.AbstractConfigurerAbleConfigurer;
import site.autzone.sqlbee.ITextAble;
import site.autzone.sqlbee.builder.SqlBuilder;

public abstract class AbstractConditionConfigurer extends AbstractConfigurerAbleConfigurer<SqlBuilder> {
	List<ITextAble> conditions = new ArrayList<>();

	public void add(ITextAble in) {
		conditions.add(in);
	}
	
	public void add(int idx, ITextAble in) {
		if(conditions.size() < idx+1) {
			for(int i = 0;i <= (idx - conditions.size()); i++) {
				conditions.add(null);
			}
		}
		conditions.set(idx, in);
	}
	
	@Override
	public void doConfigure(SqlBuilder parent) {
		//若条件是值的需要纳入管理
		parent.manageAllValue(this.conditions);
		doConditionConfigure(parent);
	}
	
	public abstract void doConditionConfigure(SqlBuilder parent);
}
