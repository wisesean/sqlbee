package site.autzone.sqlbee.model;

public interface IValue extends ITextable {
	Object convert();
	void setValue(Object value);
	Object getValue();
	void setIdx(Integer idx);
	Integer getIdx();
}
