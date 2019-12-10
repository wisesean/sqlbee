package site.autzone.sqlbee.sql.query;

import site.autzone.sqlbee.model.IQueryObject;

public class SqlQueryObject implements IQueryObject {
	private String name;
	private String alias;
	
	public SqlQueryObject() {
		
	}
	
	public SqlQueryObject(String name) {
		this.name = name;
	}
	
	public SqlQueryObject(String name, String alias) {
		this.name = name;
		this.alias = alias;
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
		if(this.alias != null) {
			return this.name+" "+this.alias;
		}else {
			return this.name;
		}
	}
}
