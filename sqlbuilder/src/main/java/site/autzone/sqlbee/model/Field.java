package site.autzone.sqlbee.model;

import org.apache.commons.lang3.Validate;

public class Field implements IField {
	private String prefix;
	private String name;
	private String alias;
	
	public Field() {
		
	}
	
	public Field(String name) {
		this.name = name;
	}
	
	public Field(String name, String alias) {
		this.name = name;
		this.alias = alias;
	}
	
	public Field(String prefix, String name, String alias) {
		this.prefix = prefix;
		this.name = name;
		this.alias = alias;
	}
	
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	@Override
	public String toText() {
		Validate.notNull(this.name);
		StringBuffer sb = new StringBuffer();
		if(this.prefix != null) {
			sb.append(this.prefix).append(".");
		}
		sb.append(this.name);
		if(this.alias != null) {
			sb.append(" ").append(this.alias);
		}
		return sb.toString();
	}
}
