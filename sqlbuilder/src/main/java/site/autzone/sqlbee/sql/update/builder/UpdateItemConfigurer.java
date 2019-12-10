package site.autzone.sqlbee.sql.update.builder;

import java.util.ArrayList;
import java.util.List;

import site.autzone.sqlbee.builder.AbstractConfigurer;
import site.autzone.sqlbee.model.Field;
import site.autzone.sqlbee.model.IField;
import site.autzone.sqlbee.model.ITextable;
import site.autzone.sqlbee.model.IValue;
import site.autzone.sqlbee.model.Value;
import site.autzone.sqlbee.sql.update.UpdateItem;

public class UpdateItemConfigurer extends AbstractConfigurer<SqlUpateBuilder> {
	private List<UpdateItem> updateItems = new ArrayList<>();

	public UpdateItemConfigurer item(IField field, IValue value) {
		updateItems.add(new UpdateItem(field, value));
		return this;
	}
	
	public UpdateItemConfigurer item(String field, IValue value) {
		updateItems.add(new UpdateItem(new Field(field), value));
		return this;
	}
	
	public UpdateItemConfigurer item(String field, String value) {
		updateItems.add(new UpdateItem(new Field(field), new Value(value)));
		return this;
	}
	
	@Override
	public void configure(SqlUpateBuilder parent) {
		parent.addUpdateItems(this.updateItems);
		for(UpdateItem child : this.updateItems) {
			for(ITextable itemField : child.getAllChild()) {
				if(itemField instanceof IValue) {
					parent.manageValue((IValue)itemField);
				}
			}
		}
	}

}
