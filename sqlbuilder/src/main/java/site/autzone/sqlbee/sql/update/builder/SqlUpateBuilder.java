package site.autzone.sqlbee.sql.update.builder;

import java.util.ArrayList;
import java.util.List;

import site.autzone.sqlbee.builder.Configurer;
import site.autzone.sqlbee.model.AbstractCondition;
import site.autzone.sqlbee.model.ICondition;
import site.autzone.sqlbee.model.IQueryObject;
import site.autzone.sqlbee.sql.builder.AbstractSqlBuilder;
import site.autzone.sqlbee.sql.query.builder.MulConditionConfigurer;
import site.autzone.sqlbee.sql.update.SqlUpdate;
import site.autzone.sqlbee.sql.update.UpdateItem;

public class SqlUpateBuilder  extends AbstractSqlBuilder<SqlUpdate> {
	private IQueryObject updateObject;
	private List<UpdateItem> updateItems = new ArrayList<>();
	private List<ICondition> conditions = new ArrayList<>();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public UpdateItemConfigurer updateItem() throws Exception {
		Configurer updateItemConfigurer = new UpdateItemConfigurer();
		updateItemConfigurer.init(this);
		this.add(updateItemConfigurer);
		return (UpdateItemConfigurer)updateItemConfigurer;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SqlUpdateObjectConfigurer updateObject() throws Exception {
		Configurer sqlUpdateObjectConfigurer = new SqlUpdateObjectConfigurer();
		sqlUpdateObjectConfigurer.init(this);
		this.add(sqlUpdateObjectConfigurer);
		return (SqlUpdateObjectConfigurer)sqlUpdateObjectConfigurer;
	}
	
	public SqlUpdateObjectConfigurer from() throws Exception {
		return updateObject();
	}
	
	protected void addAllConditions(List<ICondition> conditions) {
		this.conditions.addAll(conditions);
	}
	
	public void addCondition(AbstractCondition binaryCondition) {
		this.conditions.add(binaryCondition);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MulConditionConfigurer mulCondition(String connector) throws Exception {
		Configurer mulConditionConfigurer = new MulConditionConfigurer(connector);
		mulConditionConfigurer.init(this);
		this.add(mulConditionConfigurer);
		return (MulConditionConfigurer)mulConditionConfigurer;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BinaryConditionConfigurer binaryCondition() throws Exception {
		Configurer binaryConditionConfigurer = new BinaryConditionConfigurer();
		binaryConditionConfigurer.init(this);
		this.add(binaryConditionConfigurer);
		return (BinaryConditionConfigurer)binaryConditionConfigurer;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BinaryConditionConfigurer binaryCondition(String connector) throws Exception {
		Configurer binaryConditionConfigurer = new BinaryConditionConfigurer(connector);
		binaryConditionConfigurer.init(this);
		this.add(binaryConditionConfigurer);
		return (BinaryConditionConfigurer)binaryConditionConfigurer;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BetweenConditionConfigurer betweenCondition() throws Exception {
		Configurer betweenConditionConfigurer = new BetweenConditionConfigurer();
		betweenConditionConfigurer.init(this);
		this.add(betweenConditionConfigurer);
		return (BetweenConditionConfigurer)betweenConditionConfigurer;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InConditionConfigurer inCondition() throws Exception {
		Configurer inConditionConfigurer = new InConditionConfigurer();
		inConditionConfigurer.init(this);
		this.add(inConditionConfigurer);
		return (InConditionConfigurer)inConditionConfigurer;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IsNullConditionConfigurer isNullCondition() throws Exception {
		Configurer isNullConditionConfigurer = new IsNullConditionConfigurer();
		isNullConditionConfigurer.init(this);
		this.add(isNullConditionConfigurer);
		return (IsNullConditionConfigurer)isNullConditionConfigurer;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IsNotNullConditionConfigurer isNotNullCondition() throws Exception {
		Configurer isNotNullConditionConfigurer = new IsNotNullConditionConfigurer();
		isNotNullConditionConfigurer.init(this);
		this.add(isNotNullConditionConfigurer);
		return (IsNotNullConditionConfigurer)isNotNullConditionConfigurer;
	}
	
	public void addUpdateItems(List<UpdateItem> updateItems) {
		this.updateItems.addAll(updateItems);
	}
	
	@Override
	protected SqlUpdate performBuild() {
		SqlUpdate update = new SqlUpdate();
		
		update.updateObject(this.updateObject);
		
		for(ICondition condition : conditions) {
			update.addCondition(condition);
		}
		
		if(!super.getValueMap().isEmpty()) {
			update.setValueMap(super.getValueMap());
			update.setValues(super.getValues());
		}
		
		for(UpdateItem updateItem : this.updateItems) {
			update.addUpdateItem(updateItem);
		}
		
		return update;
	}
	
	@Override
	protected void afterBuild() {
		super.getQueryObjects().clear();
		this.updateItems.clear();
		this.conditions.clear();
		super.getValues().clear();
	}

	public void updateObject(IQueryObject updateObject) {
		this.updateObject = updateObject;
		super.getQueryObjects().add(0, updateObject);
	}

}
