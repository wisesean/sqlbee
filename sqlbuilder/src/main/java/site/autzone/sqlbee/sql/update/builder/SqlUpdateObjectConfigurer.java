package site.autzone.sqlbee.sql.update.builder;

import site.autzone.sqlbee.builder.AbstractConfigurer;
import site.autzone.sqlbee.model.IQueryObject;
import site.autzone.sqlbee.sql.query.SqlQueryObject;

public class SqlUpdateObjectConfigurer extends AbstractConfigurer<SqlUpateBuilder> {
	private IQueryObject updateObject;
	
	public SqlUpdateObjectConfigurer() {
	}
	
	public SqlUpdateObjectConfigurer(SqlUpateBuilder parent) {
		init(parent);
	}
	
	public SqlUpdateObjectConfigurer updateObject(String name) {
		updateObject = new SqlQueryObject(name, null);
		return this;
	}

	@Override
	public void configure(SqlUpateBuilder parent) {
		parent.updateObject(this.updateObject);
	}

}
