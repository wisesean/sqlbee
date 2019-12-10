package site.autzone.sqlbee.sql.query.builder;

import java.util.ArrayList;
import java.util.List;

import site.autzone.sqlbee.builder.Configurer;
import site.autzone.sqlbee.model.AbstractCondition;
import site.autzone.sqlbee.model.ICondition;
import site.autzone.sqlbee.model.IField;
import site.autzone.sqlbee.model.IQueryObject;
import site.autzone.sqlbee.sql.builder.AbstractSqlBuilder;
import site.autzone.sqlbee.sql.query.OrderBy;
import site.autzone.sqlbee.sql.query.SqlQuery;

public class SqlQueryBuilder extends AbstractSqlBuilder<SqlQuery> {
	private List<IField> cloumns = new ArrayList<>();
	private List<ICondition> conditions = new ArrayList<>();
	
	//数据集限制
	private String firstResultKey;
	private String maxResultsKey;
	
	private OrderBy orderBy;
	
	private boolean isMysql = false;
	private boolean isCount = false;
	
	protected String getFirstResultKey() {
		return firstResultKey;
	}

	protected void setFirstResultKey(String firstResultKey) {
		this.firstResultKey = firstResultKey;
	}

	protected String getMaxResultsKey() {
		return maxResultsKey;
	}

	protected void setMaxResultsKey(String maxResultsKey) {
		this.maxResultsKey = maxResultsKey;
	}
	
	protected void addAllQueryObject(List<IQueryObject> sqlQueryObjects) {
		super.getQueryObjects().addAll(sqlQueryObjects);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SqlQueryObjectConfigurer queryObject() {
		Configurer sqlQueryObjectConfigurer = new SqlQueryObjectConfigurer();
		sqlQueryObjectConfigurer.init(this);
		this.add(sqlQueryObjectConfigurer);
		return (SqlQueryObjectConfigurer)sqlQueryObjectConfigurer;
	}
	
	public SqlQueryObjectConfigurer from() {
		return queryObject();
	}
	
	protected void addAllCloumn(List<IField> cloumns) {
		this.cloumns.addAll(cloumns);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CloumnConfigurer select() {
		Configurer cloumnConfigurer = new CloumnConfigurer();
		cloumnConfigurer.init(this);
		this.add(cloumnConfigurer);
		return (CloumnConfigurer)cloumnConfigurer;
	}
	
	protected void addAllConditions(List<ICondition> conditions) {
		this.conditions.addAll(conditions);
	}
	
	public void addCondition(AbstractCondition binaryCondition) {
		this.conditions.add(binaryCondition);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BinaryConditionConfigurer binaryCondition() {
		Configurer binaryConditionConfigurer = new BinaryConditionConfigurer();
		binaryConditionConfigurer.init(this);
		this.add(binaryConditionConfigurer);
		return (BinaryConditionConfigurer)binaryConditionConfigurer;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BinaryConditionConfigurer binaryCondition(String connector) {
		Configurer binaryConditionConfigurer = new BinaryConditionConfigurer(connector);
		binaryConditionConfigurer.init(this);
		this.add(binaryConditionConfigurer);
		return (BinaryConditionConfigurer)binaryConditionConfigurer;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BetweenConditionConfigurer betweenCondition() {
		Configurer betweenConditionConfigurer = new BetweenConditionConfigurer();
		betweenConditionConfigurer.init(this);
		this.add(betweenConditionConfigurer);
		return (BetweenConditionConfigurer)betweenConditionConfigurer;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MulConditionConfigurer mulCondition(String connector) {
		Configurer mulConditionConfigurer = new MulConditionConfigurer(connector);
		mulConditionConfigurer.init(this);
		this.add(mulConditionConfigurer);
		return (MulConditionConfigurer)mulConditionConfigurer;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InConditionConfigurer inCondition() {
		Configurer inConditionConfigurer = new InConditionConfigurer();
		inConditionConfigurer.init(this);
		this.add(inConditionConfigurer);
		return (InConditionConfigurer)inConditionConfigurer;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IsNullConditionConfigurer isNullCondition() {
		Configurer isNullConditionConfigurer = new IsNullConditionConfigurer();
		isNullConditionConfigurer.init(this);
		this.add(isNullConditionConfigurer);
		return (IsNullConditionConfigurer)isNullConditionConfigurer;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IsNotNullConditionConfigurer isNotNullCondition() {
		Configurer isNotNullConditionConfigurer = new IsNotNullConditionConfigurer();
		isNotNullConditionConfigurer.init(this);
		this.add(isNotNullConditionConfigurer);
		return (IsNotNullConditionConfigurer)isNotNullConditionConfigurer;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FirstResultConfigurer firstResult(Integer firstResult) {
		Configurer firstResultConfigurer = new FirstResultConfigurer();
		FirstResultConfigurer first = (FirstResultConfigurer)firstResultConfigurer;
		first.firstResult(firstResult);
		firstResultConfigurer.init(this);
		this.add(firstResultConfigurer);
		return (FirstResultConfigurer)firstResultConfigurer;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MaxResultsConfigurer maxResults(Integer maxResults) {
		Configurer maxResultsConfigurer = new MaxResultsConfigurer();
		MaxResultsConfigurer max = (MaxResultsConfigurer)maxResultsConfigurer;
		max.maxResults(maxResults);
		maxResultsConfigurer.init(this);
		this.add(maxResultsConfigurer);
		return (MaxResultsConfigurer)maxResultsConfigurer;
	}
	
	protected void orderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public OrderByConfigurer orderBy() {
		Configurer orderByConfigurer = new OrderByConfigurer();
		orderByConfigurer.init(this);
		this.add(orderByConfigurer);
		return (OrderByConfigurer)orderByConfigurer;
	}
	
	public SqlQueryBuilder isCount() {
		this.isCount = true;
		return this;
	}
	public SqlQueryBuilder isCount(boolean isCount) {
		this.isCount = isCount;
		return this;
	}
	
	public SqlQueryBuilder removeOrderBy() {
		this.configurers.remove(OrderByConfigurer.class);
		this.orderBy = null;
		return this;
	}
	
	public SqlQueryBuilder removeMaxResults() {
		this.configurers.remove(MaxResultsConfigurer.class);
		super.getValues().remove(super.getValueMap().get(this.maxResultsKey));
		super.getValueMap().remove(this.maxResultsKey);
		this.maxResultsKey = null;
		return this;
	}
	
	public SqlQueryBuilder removeFirstResult() {
		this.configurers.remove(FirstResultConfigurer.class);
		super.getValues().remove(super.getValueMap().get(this.firstResultKey));
		super.getValueMap().remove(this.firstResultKey);
		this.firstResultKey = null;
		return this;
	}
	
	public SqlQueryBuilder isMysql() {
		this.isMysql = true;
		return this;
	}
	
	public SqlQueryBuilder isMysql(boolean isMysql) {
		this.isMysql = isMysql;
		return this;
	}
	
	@Override
	protected SqlQuery performBuild() {
		SqlQuery query = new SqlQuery();
		for(IQueryObject queryObject : super.getQueryObjects()) {
			query.addQueryObject(queryObject);
		}
		
		for(IField cloumn : cloumns) {
			query.addCloumn(cloumn);
		}
		
		for(ICondition condition : conditions) {
			query.addCondition(condition);
		}
		
		if(!super.getValueMap().isEmpty()) {
			query.setValueMap(super.getValueMap());
			query.setValues(super.getValues());
		}
		
		query.setFirstResultKey(this.firstResultKey);
		query.setMaxResultsKey(this.maxResultsKey);
		query.isCount(this.isCount);
		query.setOrderBy(this.orderBy);
		query.isMySql(this.isMysql);
		
		return query;
	}
	
	@Override
	protected void afterBuild() {
		super.getQueryObjects().clear();
		this.cloumns.clear();
		this.conditions.clear();
		super.getValues().clear();
	}
}
