package site.autzone.sqlbee.sql.insert.builder;

import java.util.ArrayList;
import java.util.List;

import site.autzone.sqlbee.builder.Configurer;
import site.autzone.sqlbee.model.IQueryObject;
import site.autzone.sqlbee.sql.builder.AbstractSqlBuilder;
import site.autzone.sqlbee.sql.insert.InsertItem;
import site.autzone.sqlbee.sql.insert.SqlInsert;

public class SqlInsertBuilder  extends AbstractSqlBuilder<SqlInsert> {
	private IQueryObject isnertObject;
	private List<InsertItem> insertItems = new ArrayList<>();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InsertItemConfigurer insertItem() throws Exception {
		Configurer insertItemConfigurer = new InsertItemConfigurer();
		insertItemConfigurer.init(this);
		this.add(insertItemConfigurer);
		return (InsertItemConfigurer)insertItemConfigurer;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SqlInsertObjectConfigurer insertObject() throws Exception {
		Configurer sqlInsertObjectConfigurer = new SqlInsertObjectConfigurer();
		sqlInsertObjectConfigurer.init(this);
		this.add(sqlInsertObjectConfigurer);
		return (SqlInsertObjectConfigurer)sqlInsertObjectConfigurer;
	}
	
	public SqlInsertObjectConfigurer from() throws Exception {
		return insertObject();
	}
	
	public void addInsertItems(List<InsertItem> updateItems) {
		this.insertItems.addAll(updateItems);
	}
	
	@Override
	protected SqlInsert performBuild() {
		SqlInsert insert = new SqlInsert();
		
		insert.insertObject(this.isnertObject);
		
		if(!super.getValueMap().isEmpty()) {
			insert.setValueMap(super.getValueMap());
			insert.setValues(super.getValues());
		}
		
		for(InsertItem insertItem : this.insertItems) {
			insert.addInsertItem(insertItem);
		}
		
		return insert;
	}
	
	@Override
	protected void afterBuild() {
		super.getQueryObjects().clear();
		this.insertItems.clear();
		super.getValues().clear();
	}

	public void updateObject(IQueryObject updateObject) {
		this.isnertObject = updateObject;
		super.getQueryObjects().add(0, updateObject);
	}

}
