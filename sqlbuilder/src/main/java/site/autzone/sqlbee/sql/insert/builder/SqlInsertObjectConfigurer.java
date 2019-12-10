package site.autzone.sqlbee.sql.insert.builder;

import site.autzone.sqlbee.builder.AbstractConfigurer;
import site.autzone.sqlbee.model.IQueryObject;
import site.autzone.sqlbee.sql.query.SqlQueryObject;

public class SqlInsertObjectConfigurer extends AbstractConfigurer<SqlInsertBuilder> {
	private IQueryObject insertObject;
	
	public SqlInsertObjectConfigurer() {
	}
	
	public SqlInsertObjectConfigurer(SqlInsertBuilder parent) {
		init(parent);
	}
	
	public SqlInsertObjectConfigurer insertObject(String name) {
		insertObject = new SqlQueryObject(name, null);
		return this;
	}

	@Override
	public void configure(SqlInsertBuilder parent) {
		parent.updateObject(this.insertObject);
	}

}
