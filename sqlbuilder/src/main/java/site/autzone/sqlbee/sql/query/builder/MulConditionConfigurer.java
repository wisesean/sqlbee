package site.autzone.sqlbee.sql.query.builder;

import site.autzone.sqlbee.model.AbstractCondition;
import site.autzone.sqlbee.model.BinaryCondition;
import site.autzone.sqlbee.model.Field;
import site.autzone.sqlbee.model.IValue;
import site.autzone.sqlbee.model.MulCondition;

public class MulConditionConfigurer extends AbstractConditionConfigurer {
	private String connector;
	public MulConditionConfigurer(String connector) {
		this.connector = connector;
	}
	
	public MulConditionConfigurer addBinaryCondition(String leftField, String connector, IValue rightValue) {
		BinaryCondition binaryCondition = new BinaryCondition(connector);
		binaryCondition.setLeftField(new Field(leftField));
		binaryCondition.setRightField(rightValue);
		super.add(binaryCondition);
		
		return this;
	}
	
	public MulConditionConfigurer addBinaryCondition(String leftField, IValue rightValue) {
		BinaryCondition binaryCondition = new BinaryCondition("=");
		binaryCondition.setLeftField(new Field(leftField));
		binaryCondition.setRightField(rightValue);
		super.add(binaryCondition);
		
		return this;
	}
	
	public MulConditionConfigurer addBinaryCondition(String leftField, String connector, String rightField) {
		BinaryCondition binaryCondition = new BinaryCondition(connector);
		binaryCondition.setLeftField(new Field(leftField));
		binaryCondition.setRightField(new Field(rightField));
		super.add(binaryCondition);
		
		return this;
	}
	
	public MulConditionConfigurer addBinaryCondition(String leftField, String rightField) {
		BinaryCondition binaryCondition = new BinaryCondition("=");
		binaryCondition.setLeftField(new Field(leftField));
		binaryCondition.setRightField(new Field(rightField));
		super.add(binaryCondition);
		
		return this;
	}

	@Override
	public void doConditionConfigure(SqlQueryBuilder parent) {
		AbstractCondition mulCondition = new MulCondition(this.connector);
		mulCondition.addAll(conditions);
		parent.addCondition(mulCondition);
	}
	
}