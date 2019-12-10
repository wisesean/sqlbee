package site.autzone.sqlbee.sql.query.builder;

import site.autzone.sqlbee.builder.AbstractConfigurer;
import site.autzone.sqlbee.model.Field;

public class BinaryConditionFieldConfigurer extends AbstractConfigurer<BinaryConditionConfigurer>{
	private String prefix;
	private String name;
	private String alias;
	
	public BinaryConditionFieldConfigurer prefix(String prefix) {
		this.prefix = prefix;
		return this;
	}
	
	public BinaryConditionFieldConfigurer field(String name) {
		this.name = name;
		return this;
	}
	
	public BinaryConditionFieldConfigurer alias(String alias) {
		this.alias = alias;
		return this;
	}
	
	public BinaryConditionFieldConfigurer field(String prefix, String name, String alias) {
		this.prefix = prefix;
		this.name = name;
		this.alias = alias;
		return this;
	}
	
	public BinaryConditionFieldConfigurer field(String name, String alias) {
		this.name = name;
		this.alias = alias;
		return this;
	}
	
	@Override
	public void configure(BinaryConditionConfigurer parent) {
		if(parent.map.get(this) == 0) {
			parent.binaryCondition.setLeftField(new Field(this.prefix, this.name, this.alias));
		}else {
			parent.binaryCondition.setRightField(new Field(this.prefix, this.name, this.alias));
		}
	}

}
