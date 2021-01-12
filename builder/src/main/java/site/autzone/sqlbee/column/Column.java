package site.autzone.sqlbee.column;

import org.apache.commons.lang3.Validate;
import site.autzone.sqlbee.IColumn;

public class Column implements IColumn {
	private String prefix;
	private String name;
	private String alias;
	
	public Column() {
		
	}
	
	public Column(String name) {
		this.name = name;
	}
	
	public Column(String name, String alias) {
		this.name = name;
		this.alias = alias;
	}
	
	public Column(String prefix, String name, String alias) {
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
	public String output() {
		Validate.notNull(this.name);
		StringBuffer sb = new StringBuffer();
		if(this.prefix != null) {
			sb.append(this.prefix).append(".");
		}
		sb.append(this.name);
		if(this.alias != null) {
			sb.append(" AS ").append(this.alias);
		}
		return sb.toString();
	}
}
