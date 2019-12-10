package site.autzone.sqlbee.sql.query.builder;

import java.util.ArrayList;
import java.util.List;

import site.autzone.sqlbee.builder.AbstractConfigurer;
import site.autzone.sqlbee.model.Field;
import site.autzone.sqlbee.model.IField;

public class CloumnConfigurer extends AbstractConfigurer<SqlQueryBuilder> {
	private List<IField> cloumns = new ArrayList<>();
	
	public CloumnConfigurer() {
	}
	
	public CloumnConfigurer(SqlQueryBuilder parent) {
		init(parent);
	}
	
	
	public CloumnConfigurer cloumn(String name) {
		cloumns.add(new Field(null, name, null));
		return this;
	}
	
	public CloumnConfigurer cloumn(String name, String alias) {
		cloumns.add(new Field(null, name, alias));
		return this;
	}
	
	public CloumnConfigurer cloumn(String prefix, String name, String alias) {
		cloumns.add(new Field(prefix, name, alias));
		return this;
	}
	
	@Override
	public void configure(SqlQueryBuilder parent) {
		parent.addAllCloumn(this.cloumns);
	}

}
