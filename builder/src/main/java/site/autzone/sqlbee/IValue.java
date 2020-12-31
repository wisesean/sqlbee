package site.autzone.sqlbee;

public interface IValue extends ITextAble {
	Object convert();
	void setValue(Object value);
	Object getValue();
	void setIdx(Integer idx);
	Integer getIdx();
}
