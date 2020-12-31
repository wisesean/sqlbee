package site.autzone.sqlbee.configurer;

import site.autzone.configurer.AbstractConfigurer;
import site.autzone.sqlbee.column.Column;

public class ConditionColumnConfigurer extends AbstractConfigurer<ConditionConfigurer>{
	private String prefix;
	private String name;
	private String alias;
	
	public ConditionColumnConfigurer prefix(String prefix) {
		this.prefix = prefix;
		return this;
	}
	
	public ConditionColumnConfigurer column(String name) {
		this.name = name;
		return this;
	}
	
	public ConditionColumnConfigurer alias(String alias) {
		this.alias = alias;
		return this;
	}
	
	public ConditionColumnConfigurer column(String prefix, String name, String alias) {
		this.prefix = prefix;
		this.name = name;
		this.alias = alias;
		return this;
	}
	
	public ConditionColumnConfigurer column(String name, String alias) {
		this.name = name;
		this.alias = alias;
		return this;
	}
	
	@Override
	public void configure(ConditionConfigurer parent) {
		if(parent.map.get(this) == 0) {
			parent.condition.setLeftField(new Column(this.prefix, this.name, this.alias));
		}else {
			parent.condition.setRightField(new Column(this.prefix, this.name, this.alias));
		}
	}

}
