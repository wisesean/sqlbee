package site.autzone.sqlbee.sql.query;

import java.util.ArrayList;
import java.util.List;

import site.autzone.sqlbee.model.Field;
import site.autzone.sqlbee.model.IField;
import site.autzone.sqlbee.model.ITextable;

public class OrderBy implements ITextable {
	private List<IField> fields = new ArrayList<>();
	private String order = "DESC";
	
	public OrderBy() {
		
	}
	
	public OrderBy(Field field) {
		this.addField(field);
	}
	
	public OrderBy(IField field, String order) {
		this.addField(field);
		this.order = order;
	}
	
	public void addField(IField field) {
		fields.add(field);
	}
	
	public void setOrder(String order) {
		this.order = order;
	}
	@Override
	public String toText() {
		if(fields.size() == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer(" ORDER BY ");
		for(IField field : fields) {
			if(" ORDER BY ".equals(sb.toString())) {
				sb.append(field.toText());
			}else {
				sb.append(",").append(field.toText());
			}
		}
		sb.append(" ").append(order);
		return sb.toString();
	}
}
